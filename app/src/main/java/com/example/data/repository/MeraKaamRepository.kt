package com.example.data.repository

import com.example.data.local.MeraKaamDao
import com.example.data.model.ComplaintEntity
import com.example.data.model.EmployerEntity
import com.example.data.model.JobEntity
import com.example.data.model.SavedJobEntity
import com.example.data.model.WorkerEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MeraKaamRepository(private val dao: MeraKaamDao) {

    // Jobs
    fun getAllJobs(): Flow<List<JobEntity>> = dao.getAllJobs()

    fun getActiveApprovedJobs(): Flow<List<JobEntity>> = dao.getActiveApprovedJobs()

    fun getJobsByEmployer(employerId: String): Flow<List<JobEntity>> = dao.getJobsByEmployer(employerId)

    suspend fun getJobById(jobId: String): JobEntity? = dao.getJobById(jobId)

    suspend fun postJob(
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
        employerId: String
    ): JobEntity {
        val newJob = JobEntity(
            id = "job_" + UUID.randomUUID().toString().take(8),
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
            employerId = employerId,
            isClosed = false,
            isApproved = true,
            createdAt = System.currentTimeMillis()
        )
        dao.insertJob(newJob)
        return newJob
    }

    suspend fun updateJob(job: JobEntity) = dao.updateJob(job)

    suspend fun setJobClosedStatus(jobId: String, isClosed: Boolean) {
        dao.setJobClosedStatus(jobId, isClosed)
    }

    suspend fun setJobApprovalStatus(jobId: String, isApproved: Boolean) {
        dao.setJobApprovalStatus(jobId, isApproved)
    }

    suspend fun deleteJob(jobId: String) = dao.deleteJobById(jobId)

    // Workers
    fun getAllWorkers(): Flow<List<WorkerEntity>> = dao.getAllWorkers()

    suspend fun getWorkerById(workerId: String): WorkerEntity? = dao.getWorkerById(workerId)

    suspend fun registerWorker(
        name: String,
        phone: String,
        district: String,
        locality: String,
        skills: String,
        experienceYears: Int,
        expectedDailyWage: Int,
        isAvailable: Boolean,
        preferredLocation: String,
        allowPhoneShare: Boolean
    ): WorkerEntity {
        val existing = dao.getWorkerByPhone(phone)
        val worker = WorkerEntity(
            id = existing?.id ?: ("wrk_" + UUID.randomUUID().toString().take(8)),
            name = name,
            phone = phone,
            district = district,
            locality = locality,
            skills = skills,
            experienceYears = experienceYears,
            expectedDailyWage = expectedDailyWage,
            isAvailable = isAvailable,
            preferredLocation = preferredLocation,
            allowPhoneShare = allowPhoneShare,
            createdAt = existing?.createdAt ?: System.currentTimeMillis()
        )
        dao.insertWorker(worker)
        return worker
    }

    suspend fun deleteWorker(worker: WorkerEntity) = dao.deleteWorker(worker)

    // Employers
    fun getAllEmployers(): Flow<List<EmployerEntity>> = dao.getAllEmployers()

    suspend fun getEmployerById(employerId: String): EmployerEntity? = dao.getEmployerById(employerId)

    suspend fun registerEmployer(
        name: String,
        phone: String,
        district: String,
        locality: String,
        employerType: String,
        businessDetails: String
    ): EmployerEntity {
        val existing = dao.getEmployerByPhone(phone)
        val employer = EmployerEntity(
            id = existing?.id ?: ("emp_" + UUID.randomUUID().toString().take(8)),
            name = name,
            phone = phone,
            district = district,
            locality = locality,
            employerType = employerType,
            businessDetails = businessDetails,
            isVerified = true,
            createdAt = existing?.createdAt ?: System.currentTimeMillis()
        )
        dao.insertEmployer(employer)
        return employer
    }

    suspend fun setEmployerSuspension(employerId: String, isSuspended: Boolean) {
        dao.setEmployerSuspension(employerId, isSuspended)
    }

    // Saved Jobs
    fun getSavedJobIds(): Flow<List<String>> = dao.getSavedJobIds()

    suspend fun toggleSaveJob(jobId: String, currentlySaved: Boolean) {
        if (currentlySaved) {
            dao.removeSavedJob(jobId)
        } else {
            dao.saveJob(SavedJobEntity(jobId = jobId))
        }
    }

    // Complaints
    fun getAllComplaints(): Flow<List<ComplaintEntity>> = dao.getAllComplaints()

    suspend fun submitComplaint(
        targetType: String,
        targetId: String,
        targetTitle: String,
        reason: String,
        details: String,
        reporterPhone: String
    ): ComplaintEntity {
        val complaint = ComplaintEntity(
            id = "cmp_" + UUID.randomUUID().toString().take(8),
            targetType = targetType,
            targetId = targetId,
            targetTitle = targetTitle,
            reason = reason,
            details = details,
            reporterPhone = reporterPhone,
            status = "PENDING",
            createdAt = System.currentTimeMillis()
        )
        dao.insertComplaint(complaint)
        return complaint
    }

    suspend fun updateComplaintStatus(complaintId: String, status: String) {
        dao.updateComplaintStatus(complaintId, status)
    }

    suspend fun deleteComplaint(complaintId: String) = dao.deleteComplaint(complaintId)
}
