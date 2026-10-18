package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.data.local.MeraKaamDatabase
import com.example.data.repository.MeraKaamRepository
import com.example.ui.components.MeraKaamTopBar
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.EmployerRegisterScreen
import com.example.ui.screens.FindWorkScreen
import com.example.ui.screens.FindWorkersScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JobDetailScreen
import com.example.ui.screens.PostJobScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReportComplaintScreen
import com.example.ui.screens.WorkerRegisterScreen
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.MeraKaamTheme
import com.example.ui.viewmodel.MeraKaamViewModel
import com.example.ui.viewmodel.MeraKaamViewModelFactory
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UserRole

data class NavItem(
    val screen: Screen,
    val titleHi: String,
    val titleEn: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {

    private val viewModel: MeraKaamViewModel by viewModels {
        val database = MeraKaamDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = MeraKaamRepository(database.dao())
        MeraKaamViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MeraKaamTheme {
                MeraKaamApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MeraKaamApp(viewModel: MeraKaamViewModel) {
    val language by viewModel.language.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()

    // Back button handling
    BackHandler(enabled = currentScreen !is Screen.Home) {
        viewModel.navigateBack()
    }

    val navItems = listOf(
        NavItem(
            screen = Screen.Home,
            titleHi = "होम",
            titleEn = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "nav_home"
        ),
        NavItem(
            screen = Screen.FindWork,
            titleHi = "काम खोजें",
            titleEn = "Find Work",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            testTag = "nav_find_work"
        ),
        NavItem(
            screen = Screen.PostJob,
            titleHi = "काम पोस्ट",
            titleEn = "Post Job",
            selectedIcon = Icons.Filled.AddCircle,
            unselectedIcon = Icons.Outlined.AddCircleOutline,
            testTag = "nav_post_job"
        ),
        NavItem(
            screen = Screen.FindWorkers,
            titleHi = "मजदूर खोजें",
            titleEn = "Workers",
            selectedIcon = Icons.Filled.People,
            unselectedIcon = Icons.Outlined.PeopleOutline,
            testTag = "nav_find_workers"
        ),
        NavItem(
            screen = Screen.Profile,
            titleHi = "प्रोफाइल",
            titleEn = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.PersonOutline,
            testTag = "nav_profile"
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MeraKaamTopBar(
                title = when (currentScreen) {
                    Screen.Home -> AppStrings.appName(language)
                    Screen.FindWork -> AppStrings.findWork(language)
                    is Screen.JobDetail -> if (language == Language.HINDI) "काम का विवरण" else "Job Details"
                    Screen.FindWorkers -> AppStrings.findWorkers(language)
                    Screen.PostJob -> AppStrings.postJob(language)
                    Screen.WorkerRegister -> AppStrings.registerWorker(language)
                    Screen.EmployerRegister -> AppStrings.registerEmployer(language)
                    Screen.AdminDashboard -> AppStrings.adminPanel(language)
                    Screen.Profile -> AppStrings.myProfile(language)
                    is Screen.ReportComplaint -> AppStrings.reportProblem(language)
                },
                language = language,
                currentRole = currentRole,
                canNavigateBack = currentScreen !is Screen.Home,
                onBackClick = { viewModel.navigateBack() },
                onLanguageSelected = { viewModel.setLanguage(it) },
                onRoleClick = { viewModel.navigateTo(Screen.Profile) }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                navItems.forEach { item ->
                    val isSelected = when (item.screen) {
                        Screen.Home -> currentScreen is Screen.Home
                        Screen.FindWork -> currentScreen is Screen.FindWork
                        Screen.PostJob -> currentScreen is Screen.PostJob
                        Screen.FindWorkers -> currentScreen is Screen.FindWorkers
                        Screen.Profile -> currentScreen is Screen.Profile
                        else -> false
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.navigateTo(item.screen) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.titleEn,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = if (language == Language.HINDI) item.titleHi else item.titleEn,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreenPrimaryDark,
                            selectedTextColor = GreenPrimaryDark,
                            indicatorColor = GreenContainer,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        val screenModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (val screen = currentScreen) {
            Screen.Home -> HomeScreen(viewModel = viewModel, modifier = screenModifier)
            Screen.FindWork -> FindWorkScreen(viewModel = viewModel, modifier = screenModifier)
            is Screen.JobDetail -> JobDetailScreen(jobId = screen.jobId, viewModel = viewModel, modifier = screenModifier)
            Screen.FindWorkers -> FindWorkersScreen(viewModel = viewModel, modifier = screenModifier)
            Screen.PostJob -> PostJobScreen(viewModel = viewModel, modifier = screenModifier)
            Screen.WorkerRegister -> WorkerRegisterScreen(viewModel = viewModel, modifier = screenModifier)
            Screen.EmployerRegister -> EmployerRegisterScreen(viewModel = viewModel, modifier = screenModifier)
            Screen.AdminDashboard -> AdminScreen(viewModel = viewModel, modifier = screenModifier)
            Screen.Profile -> ProfileScreen(viewModel = viewModel, modifier = screenModifier)
            is Screen.ReportComplaint -> ReportComplaintScreen(
                targetType = screen.targetType,
                targetId = screen.targetId,
                targetTitle = screen.targetTitle,
                viewModel = viewModel,
                modifier = screenModifier
            )
        }
    }
}
