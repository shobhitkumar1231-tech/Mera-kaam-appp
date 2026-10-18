package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.ComplaintEntity
import com.example.data.model.EmployerEntity
import com.example.data.model.JobEntity
import com.example.data.model.WorkerEntity
import com.example.data.repository.MeraKaamRepository
import com.example.ui.strings.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object FindWork : Screen()
    data class JobDetail(val jobId: String) : Screen()
    object FindWorkers : Screen()
    object PostJob : Screen()
    object WorkerRegister : Screen()
    object EmployerRegister : Screen()
    object AdminDashboard : Screen()
    object Profile : Screen()
    data class ReportComplaint(val targetType: String, val targetId: String, val targetTitle: String) : Screen()
}

enum class UserRole {
    WORKER,
    EMPLOYER,
    ADMIN
}

enum class JobSort {
    NEWEST,
    HIGHEST_WAGE
}

class MeraKaamViewModel(private val repository: MeraKaamRepository) : ViewModel() {

    // Language setting: Hindi default
    private val _language = MutableStateFlow(Language.HINDI)
    val language: StateFlow<Language> = _language.asStateFlow()

    fun toggleLanguage() {
        _language.value = if (_language.value == Language.HINDI) Language.ENGLISH else Language.HINDI
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
    }

    // Navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<Screen>()

    fun navigateTo(screen: Screen) {
        screenBackStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
            return true
        }
        if (_currentScreen.value !is Screen.Home) {
            _currentScreen.value = Screen.Home
            return true
        }
        return false
    }

    // Role state
    private val _currentRole = MutableStateFlow(UserRole.WORKER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    // Current Employer ID for posting & managing jobs
    val currentEmployerId = MutableStateFlow("emp_1")
    val currentWorkerId = MutableStateFlow("wrk_1")

    // Database flows
    val allJobs: StateFlow<List<JobEntity>> = repository.getAllJobs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeApprovedJobs: StateFlow<List<JobEntity>> = repository.getActiveApprovedJobs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWorkers: StateFlow<List<WorkerEntity>> = repository.getAllWorkers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEmployers: StateFlow<List<EmployerEntity>> = repository.getAllEmployers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedJobIds: StateFlow<List<String>> = repository.getSavedJobIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val complaints: StateFlow<List<ComplaintEntity>> = repository.getAllComplaints()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Job Search & Filters
    val searchQuery = MutableStateFlow("")
    val selectedDistrict = MutableStateFlow("सभी")
    val selectedCategory = MutableStateFlow("सभी")
    val sortOption = MutableStateFlow(JobSort.NEWEST)

    // Filtered Jobs StateFlow
    val filteredJobs: StateFlow<List<JobEntity>> = combine(
        activeApprovedJobs,
        searchQuery,
        selectedDistrict,
        selectedCategory,
        sortOption
    ) { jobs, query, district, category, sort ->
        var list = jobs

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.locality.lowercase().contains(q) ||
                it.district.lowercase().contains(q) ||
                it.category.lowercase().contains(q)
            }
        }

        if (district != "सभी" && district != "All") {
            list = list.filter { it.district.equals(district, ignoreCase = true) }
        }

        if (category != "सभी" && category != "All") {
            list = list.filter { it.category.contains(category, ignoreCase = true) }
        }

        when (sort) {
            JobSort.NEWEST -> list.sortedByDescending { it.createdAt }
            JobSort.HIGHEST_WAGE -> list.sortedByDescending { it.dailyWage }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Worker Search & Filters
    val workerSearchQuery = MutableStateFlow("")
    val workerSelectedDistrict = MutableStateFlow("सभी")
    val workerSelectedSkill = MutableStateFlow("सभी")

    val filteredWorkers: StateFlow<List<WorkerEntity>> = combine(
        allWorkers,
        workerSearchQuery,
        workerSelectedDistrict,
        workerSelectedSkill
    ) { workers, query, district, skill ->
        var list = workers
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                it.skills.lowercase().contains(q) ||
                it.locality.lowercase().contains(q) ||
                it.district.lowercase().contains(q)
            }
        }
        if (district != "सभी" && district != "All") {
            list = list.filter { it.district.equals(district, ignoreCase = true) }
        }
        if (skill != "सभी" && skill != "All") {
            list = list.filter { it.skills.contains(skill, ignoreCase = true) }
        }
        list.sortedByDescending { it.createdAt }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Actions
    fun toggleSaveJob(jobId: String) {
        viewModelScope.launch {
            val isSaved = savedJobIds.value.contains(jobId)
            repository.toggleSaveJob(jobId, isSaved)
        }
    }

    fun postJob(
        title: String,
        category: String,
        description: String,
        district: String,
        locality: String,
        dailyWage: Int,
        workersNeeded: Int,
        startDate: String,
        duration: String,
        workingHours: String,
        employerName: String,
        employerPhone: String,
        onSuccess: (JobEntity) -> Unit
    ) {
        viewModelScope.launch {
            val job = repository.postJob(
                title = title,
                category = category,
                description = description,
                district = district,
                locality = locality,
                dailyWage = dailyWage,
                workersNeeded = workersNeeded,
                startDate = startDate,
                duration = duration,
                workingHours = workingHours,
                employerName = employerName,
                employerPhone = employerPhone,
                employerId = currentEmployerId.value
            )
            onSuccess(job)
        }
    }

    fun registerWorker(
        name: String,
        phone: String,
        district: String,
        locality: String,
        skills: String,
        experienceYears: Int,
        expectedDailyWage: Int,
        isAvailable: Boolean,
        preferredLocation: String,
        allowPhoneShare: Boolean,
        onSuccess: (WorkerEntity) -> Unit
    ) {
        viewModelScope.launch {
            val worker = repository.registerWorker(
                name = name,
                phone = phone,
                district = district,
                locality = locality,
                skills = skills,
                experienceYears = experienceYears,
                expectedDailyWage = expectedDailyWage,
                isAvailable = isAvailable,
                preferredLocation = preferredLocation,
                allowPhoneShare = allowPhoneShare
            )
            currentWorkerId.value = worker.id
            setRole(UserRole.WORKER)
            onSuccess(worker)
        }
    }

    fun registerEmployer(
        name: String,
        phone: String,
        district: String,
        locality: String,
        employerType: String,
        businessDetails: String,
        onSuccess: (EmployerEntity) -> Unit
    ) {
        viewModelScope.launch {
            val employer = repository.registerEmployer(
                name = name,
                phone = phone,
                district = district,
                locality = locality,
                employerType = employerType,
                businessDetails = businessDetails
            )
            currentEmployerId.value = employer.id
            setRole(UserRole.EMPLOYER)
            onSuccess(employer)
        }
    }

    fun setJobClosed(jobId: String, isClosed: Boolean) {
        viewModelScope.launch {
            repository.setJobClosedStatus(jobId, isClosed)
        }
    }

    fun deleteJob(jobId: String) {
        viewModelScope.launch {
            repository.deleteJob(jobId)
        }
    }

    fun setJobApproval(jobId: String, isApproved: Boolean) {
        viewModelScope.launch {
            repository.setJobApprovalStatus(jobId, isApproved)
        }
    }

    fun submitComplaint(
        targetType: String,
        targetId: String,
        targetTitle: String,
        reason: String,
        details: String,
        reporterPhone: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.submitComplaint(
                targetType = targetType,
                targetId = targetId,
                targetTitle = targetTitle,
                reason = reason,
                details = details,
                reporterPhone = reporterPhone
            )
            onSuccess()
        }
    }

    fun updateComplaintStatus(complaintId: String, status: String) {
        viewModelScope.launch {
            repository.updateComplaintStatus(complaintId, status)
        }
    }

    fun deleteComplaint(complaintId: String) {
        viewModelScope.launch {
            repository.deleteComplaint(complaintId)
        }
    }

    fun toggleSuspendEmployer(employerId: String, isSuspended: Boolean) {
        viewModelScope.launch {
            repository.setEmployerSuspension(employerId, isSuspended)
        }
    }
}

class MeraKaamViewModelFactory(
    private val repository: MeraKaamRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeraKaamViewModel::class.java)) {
            return MeraKaamViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
