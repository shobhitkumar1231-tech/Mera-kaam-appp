package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.JobItemCard
import com.example.ui.components.LanguageToggle
import com.example.ui.components.ScamWarningCard
import com.example.ui.components.dialPhoneNumber
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.GreenPrimaryMedium
import com.example.ui.viewmodel.MeraKaamViewModel
import com.example.ui.viewmodel.Screen

data class CategoryItem(val id: String, val nameHi: String, val nameEn: String, val icon: ImageVector)

val popularCategoriesList = listOf(
    CategoryItem("construction", "निर्माण", "Construction", Icons.Default.Apartment),
    CategoryItem("farming", "कृषि", "Farming", Icons.Default.Agriculture),
    CategoryItem("painting", "पेंटिंग", "Painting", Icons.Default.FormatPaint),
    CategoryItem("plumbing", "प्लंबिंग", "Plumbing", Icons.Default.Plumbing),
    CategoryItem("electrical", "इलेक्ट्रीशियन", "Electrical", Icons.Default.Handyman),
    CategoryItem("loading", "लोडिंग", "Loading", Icons.Default.LocalShipping),
    CategoryItem("cleaning", "सफाई", "Cleaning", Icons.Default.CleaningServices),
    CategoryItem("carpentry", "कारपेंटर", "Carpentry", Icons.Default.Build)
)

@Composable
fun HomeScreen(
    viewModel: MeraKaamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val activeJobs by viewModel.activeApprovedJobs.collectAsState()
    val savedJobIds by viewModel.savedJobIds.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6)),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Banner with Image
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Mera Kaam Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xCC002204), Color(0xF0002204))
                            )
                        )
                )
                // Language Toggle in Hero Banner
                LanguageToggle(
                    currentLanguage = language,
                    onLanguageSelected = { viewModel.setLanguage(it) },
                    isDarkThemeOrContrast = true,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .testTag("home_hero_language_toggle")
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (language == Language.HINDI) "भारत का अपना मजदूर मंच" else "India's Labor & Work Marketplace",
                        color = Color(0xFFFFD54F),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = AppStrings.appName(language),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = if (language == Language.HINDI) "सीधे काम पाएं • बिना किसी दलाली या कमीशन के" else "Direct work • Zero middleman fee or commission",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Safety scam notice
        item {
            ScamWarningCard(
                language = language,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        // 4 Big Primary Action Buttons (2x2 Grid)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Button 1: काम खोजें (Find Work)
                    QuickActionButton(
                        title = AppStrings.findWork(language),
                        subtitle = if (language == Language.HINDI) "दैनिक मजदूरी पाएं" else "Daily jobs near you",
                        icon = Icons.Default.Work,
                        backgroundColor = GreenPrimaryDark,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("home_find_work_button"),
                        onClick = { viewModel.navigateTo(Screen.FindWork) }
                    )

                    // Button 2: मजदूर खोजें (Find Workers)
                    QuickActionButton(
                        title = AppStrings.findWorkers(language),
                        subtitle = if (language == Language.HINDI) "कारीगर व मजदूर संपर्क" else "Hire skilled workers",
                        icon = Icons.Default.PersonAdd,
                        backgroundColor = GreenPrimaryMedium,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("home_find_workers_button"),
                        onClick = { viewModel.navigateTo(Screen.FindWorkers) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Button 3: काम पोस्ट करें (Post Job)
                    QuickActionButton(
                        title = AppStrings.postJob(language),
                        subtitle = if (language == Language.HINDI) "मजदूरों की जरूरत है" else "Post requirement",
                        icon = Icons.Default.AddCircle,
                        backgroundColor = AmberAccent,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("home_post_job_button"),
                        onClick = { viewModel.navigateTo(Screen.PostJob) }
                    )

                    // Button 4: मजदूर रजिस्ट्रेशन (Worker Register)
                    QuickActionButton(
                        title = AppStrings.registerWorker(language),
                        subtitle = if (language == Language.HINDI) "अपना प्रोफाइल बनाएं" else "Create worker profile",
                        icon = Icons.Default.Business,
                        backgroundColor = Color(0xFF263238),
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("home_register_worker_button"),
                        onClick = { viewModel.navigateTo(Screen.WorkerRegister) }
                    )
                }
            }
        }

        // Search Bar
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        viewModel.searchQuery.value = it
                    },
                    placeholder = {
                        Text(AppStrings.searchPlaceholder(language), fontSize = 13.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = GreenPrimaryDark)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            Text(
                                text = if (language == Language.HINDI) "खोजें" else "Go",
                                color = GreenPrimaryDark,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable { viewModel.navigateTo(Screen.FindWork) }
                                    .padding(8.dp)
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        // Popular Categories
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = AppStrings.popularCategories(language),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF1E261E)
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    popularCategoriesList.forEach { category ->
                        CategoryChip(
                            category = category,
                            language = language,
                            onClick = {
                                viewModel.selectedCategory.value = category.nameHi
                                viewModel.navigateTo(Screen.FindWork)
                            }
                        )
                    }
                }
            }
        }

        // Recent Jobs Header & Link
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.recentJobs(language),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF1E261E)
                    )
                )
                Text(
                    text = if (language == Language.HINDI) "सभी देखें →" else "View All →",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = GreenPrimaryDark,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { viewModel.navigateTo(Screen.FindWork) }
                        .padding(4.dp)
                )
            }
        }

        // Recent Job Cards Preview (first 4)
        items(activeJobs.take(4)) { job ->
            JobItemCard(
                job = job,
                language = language,
                isSaved = savedJobIds.contains(job.id),
                onJobClick = { viewModel.navigateTo(Screen.JobDetail(job.id)) },
                onSaveClick = { viewModel.toggleSaveJob(job.id) },
                onCallClick = { dialPhoneNumber(context, job.employerPhone) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // Secondary Links: Employer Registration, Report Problem, Admin
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(Screen.EmployerRegister) }
                        .testTag("home_employer_register_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(GreenContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Business,
                                    contentDescription = "Employer",
                                    tint = GreenPrimaryDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = AppStrings.registerEmployer(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (language == Language.HINDI) "ठेकेदार, किसान और दूकानदार यहां रजिस्टर करें" else "Contractors, farmers, shop owners register here",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                                )
                            }
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = "Go", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Report Problem / Complaints
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.navigateTo(
                                    Screen.ReportComplaint("GENERAL", "APP", "सामान्य समस्या / शिकायत")
                                )
                            }
                            .testTag("home_report_problem_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE9E7)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Report, contentDescription = "Report", tint = Color(0xFFD84315), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.reportProblem(language),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFBF360C))
                            )
                        }
                    }

                    // Admin Dashboard
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.navigateTo(Screen.AdminDashboard) }
                            .testTag("home_admin_dashboard_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Build, contentDescription = "Admin", tint = Color(0xFF455A64), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.adminPanel(language),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF263238))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.22f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = contentColor
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = contentColor.copy(alpha = 0.85f)
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: CategoryItem,
    language: Language,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(GreenContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.nameEn,
                    tint = GreenPrimaryDark,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == Language.HINDI) category.nameHi else category.nameEn,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            )
        }
    }
}
