package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.viewmodel.UserRole

@Composable
fun JobDetailScreen(
    jobId: String,
    viewModel: MeraKaamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val allJobs by viewModel.allJobs.collectAsState()
    val savedJobIds by viewModel.savedJobIds.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val currentEmployerId by viewModel.currentEmployerId.collectAsState()

    val job = allJobs.find { it.id == jobId }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    if (job == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(if (language == Language.HINDI) "काम नहीं मिला" else "Job not found")
        }
        return
    }

    val isSaved = savedJobIds.contains(job.id)
    val isOwnerOrAdmin = (job.employerId == currentEmployerId && currentRole == UserRole.EMPLOYER) || currentRole == UserRole.ADMIN

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Badges & Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = GreenContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = job.category,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimaryDark
                    )
                )
            }

            Row {
                IconButton(
                    onClick = { viewModel.toggleSaveJob(job.id) },
                    modifier = Modifier.testTag("detail_save_job_button")
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save job",
                        tint = if (isSaved) AmberAccent else Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Text(
            text = job.title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                lineHeight = 28.sp
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Daily Wage & Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == Language.HINDI) "नकद दैनिक मजदूरी" else "Cash Daily Wage",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                        )
                        Text(
                            text = "₹${job.dailyWage} ${AppStrings.perDay(language)}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimaryDark
                            )
                        )
                    }

                    Surface(
                        color = if (job.isClosed) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (job.isClosed) AppStrings.statusClosed(language) else AppStrings.statusActive(language),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (job.isClosed) Color(0xFFC62828) else Color(0xFF2E7D32)
                            )
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

                // 2x2 Key specs
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Group, contentDescription = "Workers", tint = GreenPrimaryMedium, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == Language.HINDI) "मजदूर चाहिए:" else "Workers needed:",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                        Text(
                            text = "${job.workersNeeded} व्यक्ति",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 22.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Start", tint = GreenPrimaryMedium, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == Language.HINDI) "शुरुआत:" else "Start date:",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                        Text(
                            text = job.startDate,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DateRange, contentDescription = "Duration", tint = GreenPrimaryMedium, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == Language.HINDI) "अवधि:" else "Duration:",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                        Text(
                            text = job.duration,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 22.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = "Hours", tint = GreenPrimaryMedium, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == Language.HINDI) "समय:" else "Hours:",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                        Text(
                            text = job.workingHours,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 22.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Location Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = GreenPrimaryDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == Language.HINDI) "कार्य स्थल का पता" else "Work Location",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${job.district}, ${job.locality}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Job Description Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "काम का पूरा विवरण" else "Job Description",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = job.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp,
                        color = Color(0xFF333333)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Employer Information & Direct Call Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = job.employerName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Verified Employer",
                                tint = GreenPrimaryDark,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = if (language == Language.HINDI) "सत्यापित काम प्रदाता (नियोक्ता)" else "Verified Employer",
                            style = MaterialTheme.typography.bodySmall.copy(color = GreenPrimaryDark)
                        )
                    }

                    Button(
                        onClick = { dialPhoneNumber(context, job.employerPhone) },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
                        modifier = Modifier.testTag("detail_call_employer_button")
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.callEmployer(language), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Scam Warning Banner
        ScamWarningCard(language = language)

        Spacer(modifier = Modifier.height(16.dp))

        // Report Suspicious Job Button
        OutlinedButton(
            onClick = {
                viewModel.navigateTo(
                    Screen.ReportComplaint(
                        targetType = "JOB",
                        targetId = job.id,
                        targetTitle = job.title
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("report_job_button"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828))
        ) {
            Icon(Icons.Default.Report, contentDescription = "Report", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (language == Language.HINDI) "संदिग्ध काम या धोखाधड़ी की रिपोर्ट करें" else "Report Suspicious Job")
        }

        // Owner / Admin Actions: Close Job / Delete Job
        if (isOwnerOrAdmin) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (language == Language.HINDI) "नियोक्ता नियंत्रण (आपके अधिकार)" else "Employer Controls",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = AmberAccent)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.setJobClosed(job.id, !job.isClosed)
                                Toast.makeText(
                                    context,
                                    if (job.isClosed) "काम दोबारा सक्रिय किया गया" else "काम बंद किया गया (भर्ती पूरी)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (job.isClosed) GreenPrimaryDark else Color(0xFFD84315)
                            ),
                            modifier = Modifier.weight(1f).testTag("owner_close_job_button")
                        ) {
                            Text(
                                text = if (job.isClosed) {
                                    if (language == Language.HINDI) "दोबारा खोलें" else "Re-open"
                                } else {
                                    if (language == Language.HINDI) "काम बंद करें" else "Close Job"
                                },
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = { showDeleteConfirmDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                            modifier = Modifier.weight(1f).testTag("owner_delete_job_button")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (language == Language.HINDI) "डिलीट करें" else "Delete", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Text(if (language == Language.HINDI) "काम डिलीट करें?" else "Delete this job?")
            },
            text = {
                Text(if (language == Language.HINDI) "क्या आप वाकई इस काम की पोस्ट को हटाना चाहते हैं? यह क्रिया वापस नहीं होगी।" else "Are you sure you want to permanently delete this job post?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteJob(job.id)
                        showDeleteConfirmDialog = false
                        viewModel.navigateBack()
                        Toast.makeText(context, "काम डिलीट कर दिया गया", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(if (language == Language.HINDI) "हां, हटाएं" else "Yes, Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text(if (language == Language.HINDI) "रद्द करें" else "Cancel")
                }
            }
        )
    }
}
