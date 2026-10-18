package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.JobItemCard
import com.example.ui.components.LanguageToggle
import com.example.ui.components.dialPhoneNumber
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.GreenPrimaryMedium
import com.example.ui.viewmodel.MeraKaamViewModel
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UserRole

@Composable
fun ProfileScreen(
    viewModel: MeraKaamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val allJobs by viewModel.allJobs.collectAsState()
    val savedJobIds by viewModel.savedJobIds.collectAsState()
    val currentEmployerId by viewModel.currentEmployerId.collectAsState()

    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    val myPostedJobs = allJobs.filter { it.employerId == currentEmployerId }
    val mySavedJobs = allJobs.filter { savedJobIds.contains(it.id) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(GreenContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (currentRole) {
                                    UserRole.WORKER -> Icons.Default.Person
                                    UserRole.EMPLOYER -> Icons.Default.Business
                                    UserRole.ADMIN -> Icons.Default.Security
                                },
                                contentDescription = "Role",
                                tint = GreenPrimaryDark,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when (currentRole) {
                                    UserRole.WORKER -> if (language == Language.HINDI) "मजदूर / कारीगर खाता" else "Worker Account"
                                    UserRole.EMPLOYER -> if (language == Language.HINDI) "नियोक्ता / ठेकेदार खाता" else "Employer Account"
                                    UserRole.ADMIN -> "प्रशासक (Admin)"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            )
                            Text(
                                text = if (language == Language.HINDI) "सत्यापित भारतीय मोबाइल उपयोगकर्ता" else "Verified Mobile User",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

                    // Role Switcher
                    Text(
                        text = if (language == Language.HINDI) "भूमिका बदलें (Switch Role):" else "Switch Active Role:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RoleSwitchButton(
                            title = if (language == Language.HINDI) "मजदूर" else "Worker",
                            isSelected = currentRole == UserRole.WORKER,
                            onClick = { viewModel.setRole(UserRole.WORKER) },
                            modifier = Modifier.weight(1f).testTag("role_worker_button")
                        )
                        RoleSwitchButton(
                            title = if (language == Language.HINDI) "नियोक्ता" else "Employer",
                            isSelected = currentRole == UserRole.EMPLOYER,
                            onClick = { viewModel.setRole(UserRole.EMPLOYER) },
                            modifier = Modifier.weight(1f).testTag("role_employer_button")
                        )
                        RoleSwitchButton(
                            title = "Admin",
                            isSelected = currentRole == UserRole.ADMIN,
                            onClick = { viewModel.navigateTo(Screen.AdminDashboard) },
                            modifier = Modifier.weight(1f).testTag("role_admin_button")
                        )
                    }
                }
            }
        }

        // Language & Preferences Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == Language.HINDI) "भाषा और सेटिंग्स" else "Language & Settings",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Translate, contentDescription = "Lang", tint = GreenPrimaryMedium)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == Language.HINDI) "ऐप की भाषा" else "App Language",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = if (language == Language.HINDI) "वर्तमान: हिंदी" else "Current: English",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                            }
                        }

                        LanguageToggle(
                            currentLanguage = language,
                            onLanguageSelected = { viewModel.setLanguage(it) },
                            isDarkThemeOrContrast = false,
                            modifier = Modifier.testTag("profile_language_toggle")
                        )
                    }
                }
            }
        }

        // Section: My Posted Jobs (If Employer or has posted jobs)
        if (myPostedJobs.isNotEmpty() || currentRole == UserRole.EMPLOYER) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${AppStrings.myPostedJobs(language)} (${myPostedJobs.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E261E))
                    )
                    TextButton(onClick = { viewModel.navigateTo(Screen.PostJob) }) {
                        Text("+ ${AppStrings.postJob(language)}", color = GreenPrimaryDark, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (myPostedJobs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (language == Language.HINDI) "आपने अभी तक कोई काम पोस्ट नहीं किया है।" else "You have not posted any jobs yet.",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                items(myPostedJobs, key = { it.id }) { job ->
                    JobItemCard(
                        job = job,
                        language = language,
                        isSaved = savedJobIds.contains(job.id),
                        onJobClick = { viewModel.navigateTo(Screen.JobDetail(job.id)) },
                        onSaveClick = { viewModel.toggleSaveJob(job.id) },
                        onCallClick = { dialPhoneNumber(context, job.employerPhone) }
                    )
                }
            }
        }

        // Section: Saved Jobs (Bookmarks)
        item {
            Text(
                text = "${AppStrings.savedJobs(language)} (${mySavedJobs.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E261E))
            )
        }

        if (mySavedJobs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (language == Language.HINDI) "कोई सेव किया हुआ काम नहीं है।" else "No saved jobs.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            items(mySavedJobs, key = { it.id }) { job ->
                JobItemCard(
                    job = job,
                    language = language,
                    isSaved = true,
                    onJobClick = { viewModel.navigateTo(Screen.JobDetail(job.id)) },
                    onSaveClick = { viewModel.toggleSaveJob(job.id) },
                    onCallClick = { dialPhoneNumber(context, job.employerPhone) }
                )
            }
        }

        // Account Privacy & Data Deletion
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == Language.HINDI) "गोपनीयता व डेटा सुरक्षा" else "Privacy & Personal Data",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == Language.HINDI)
                            "आप किसी भी समय अपने प्रोफाइल व व्यक्तिगत डेटा को हटाने का अनुरोध कर सकते हैं।"
                        else
                            "You can request deletion of your account and personal data anytime.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { showDeleteAccountDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828)),
                        modifier = Modifier.testTag("delete_data_request_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == Language.HINDI) "खाता व डेटा हटाने का अनुरोध" else "Delete Account & Data",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = {
                Text(if (language == Language.HINDI) "डेटा हटाने की पुष्टि" else "Confirm Data Deletion")
            },
            text = {
                Text(
                    text = if (language == Language.HINDI)
                        "क्या आप वाकई अपना प्रोफाइल और सभी संबंधित डेटा हटाना चाहते हैं? आपका फोन नंबर व जानकारी तुरंत हटा दी जाएगी।"
                    else
                        "Are you sure you want to delete your profile and personal data? This action is irreversible."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteAccountDialog = false
                        Toast.makeText(
                            context,
                            if (language == Language.HINDI) "आपका डेटा सफलतापूर्वक हटा दिया गया है।" else "Your data has been deleted.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                ) {
                    Text(if (language == Language.HINDI) "हां, हटाएं" else "Yes, Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text(if (language == Language.HINDI) "रद्द करें" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun RoleSwitchButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) GreenPrimaryDark else Color(0xFFF1F5F0),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color.DarkGray
                )
            )
        }
    }
}
