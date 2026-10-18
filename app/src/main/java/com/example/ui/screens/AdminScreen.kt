package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.GreenPrimaryMedium
import com.example.ui.viewmodel.MeraKaamViewModel
import com.example.ui.viewmodel.UserRole

@Composable
fun AdminScreen(
    viewModel: MeraKaamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val allJobs by viewModel.allJobs.collectAsState()
    val allWorkers by viewModel.allWorkers.collectAsState()
    val allEmployers by viewModel.allEmployers.collectAsState()
    val complaints by viewModel.complaints.collectAsState()

    var isAuthenticated by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Jobs, 1: Complaints, 2: Employers

    if (!isAuthenticated) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF7FAF6))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color(0xFFFFEBEE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = "Admin Security",
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (language == Language.HINDI) "प्रशासक / एडमिन लॉगिन" else "Administrator Access",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (language == Language.HINDI)
                            "केवल अधिकृत मॉडरेटर्स के लिए। सुरक्षित पिन दर्ज करें।"
                        else
                            "Authorized personnel only. Enter secure PIN.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it; pinError = null },
                        label = { Text("Admin PIN") },
                        placeholder = { Text("उदा: 1234") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_pin_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    if (pinError != null) {
                        Text(
                            text = pinError!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (pinInput == "1234" || pinInput == "admin" || pinInput == "2026") {
                                isAuthenticated = true
                                viewModel.setRole(UserRole.ADMIN)
                            } else {
                                pinError = if (language == Language.HINDI) "गलत पिन! डेमो के लिए '1234' दर्ज करें।" else "Incorrect PIN! Use '1234' for demo."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_login_submit_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (language == Language.HINDI) "प्रवेश करें" else "Unlock Dashboard", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == Language.HINDI) "(परीक्षण कोड: 1234)" else "(Test code: 1234)",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                }
            }
        }
        return
    }

    // Authenticated Admin Dashboard
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6))
    ) {
        // Statistics Grid
        Surface(
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.adminPanel(language),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                    )
                    Surface(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Admin Mode",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats 4 items
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AdminStatCard(
                        count = allJobs.count { !it.isClosed },
                        label = if (language == Language.HINDI) "सक्रिय काम" else "Active Jobs",
                        color = GreenPrimaryDark,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        count = allWorkers.size,
                        label = if (language == Language.HINDI) "मजदूर" else "Workers",
                        color = GreenPrimaryMedium,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        count = allEmployers.size,
                        label = if (language == Language.HINDI) "नियोक्ता" else "Employers",
                        color = Color(0xFF00695C),
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        count = complaints.count { it.status == "PENDING" },
                        label = if (language == Language.HINDI) "शिकायतें" else "Complaints",
                        color = AmberAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Tabs
        val tabs = listOf(
            if (language == Language.HINDI) "काम मॉडरेशन (${allJobs.size})" else "Jobs (${allJobs.size})",
            if (language == Language.HINDI) "शिकायतें (${complaints.size})" else "Complaints (${complaints.size})",
            if (language == Language.HINDI) "नियोक्ता (${allEmployers.size})" else "Employers (${allEmployers.size})"
        )

        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = GreenPrimaryDark,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) },
                    modifier = Modifier.testTag("admin_tab_$index")
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Job Moderation
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allJobs) { job ->
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_job_card_${job.id}"),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = job.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Surface(
                                        color = if (job.isApproved) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = if (job.isApproved) "स्वीकृत (Approved)" else "अस्वीकृत (Rejected)",
                                            color = if (job.isApproved) GreenPrimaryDark else Color.Red,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "${job.category} • ${job.district} • ₹${job.dailyWage}/दिन • नियोक्ता: ${job.employerName}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    if (!job.isApproved) {
                                        Button(
                                            onClick = {
                                                viewModel.setJobApproval(job.id, true)
                                                Toast.makeText(context, "काम स्वीकृत किया गया", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
                                            modifier = Modifier.testTag("admin_approve_${job.id}")
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = "Approve", modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Approve", fontSize = 12.sp)
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                viewModel.setJobApproval(job.id, false)
                                                Toast.makeText(context, "काम अस्वीकृत किया गया", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD84315)),
                                            modifier = Modifier.testTag("admin_reject_${job.id}")
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Reject", modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Reject", fontSize = 12.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            viewModel.deleteJob(job.id)
                                            Toast.makeText(context, "काम हटा दिया गया", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                                        modifier = Modifier.testTag("admin_delete_${job.id}")
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Delete", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Complaints Review
                if (complaints.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(if (language == Language.HINDI) "कोई शिकायत लंबित नहीं है।" else "No complaints submitted.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(complaints) { complaint ->
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("admin_complaint_${complaint.id}"),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "कारण: ${complaint.reason}",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                                        )
                                        Surface(
                                            color = if (complaint.status == "PENDING") Color(0xFFFFF3E0) else Color(0xFFE8F5E9),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = complaint.status,
                                                color = if (complaint.status == "PENDING") AmberAccent else GreenPrimaryDark,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "लक्षित: ${complaint.targetTitle} (ID: ${complaint.targetId})",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = "विवरण: ${complaint.details}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    if (complaint.reporterPhone.isNotBlank()) {
                                        Text(
                                            text = "शिकायतकर्ता फोन: ${complaint.reporterPhone}",
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        if (complaint.status == "PENDING") {
                                            Button(
                                                onClick = {
                                                    viewModel.updateComplaintStatus(complaint.id, "RESOLVED")
                                                    Toast.makeText(context, "शिकायत निस्तारित (Resolved) मार्क की गई", Toast.LENGTH_SHORT).show()
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
                                                modifier = Modifier.testTag("admin_resolve_complaint_${complaint.id}")
                                            ) {
                                                Text("Mark Resolved", fontSize = 12.sp)
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Button(
                                            onClick = {
                                                viewModel.deleteComplaint(complaint.id)
                                                Toast.makeText(context, "शिकायत हटाई गई", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                            modifier = Modifier.testTag("admin_delete_complaint_${complaint.id}")
                                        ) {
                                            Text("Dismiss", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Employers Directory & Suspension
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allEmployers) { employer ->
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_employer_${employer.id}"),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = employer.name,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "${employer.employerType} • ${employer.district}, ${employer.locality}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                        )
                                        Text(
                                            text = "फोन: ${employer.phone}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = GreenPrimaryDark)
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.toggleSuspendEmployer(employer.id, !employer.isSuspended)
                                            Toast.makeText(
                                                context,
                                                if (employer.isSuspended) "खाता पुनः सक्रिय" else "खाता निलंबित (Suspended)",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (employer.isSuspended) GreenPrimaryDark else Color(0xFFC62828)
                                        ),
                                        modifier = Modifier.testTag("admin_suspend_emp_${employer.id}")
                                    ) {
                                        Icon(
                                            imageVector = if (employer.isSuspended) Icons.Default.Check else Icons.Default.Block,
                                            contentDescription = "Suspend",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (employer.isSuspended) "Reactivate" else "Suspend",
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    count: Int,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF1F5F0),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = color)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color.DarkGray),
                maxLines = 1
            )
        }
    }
}
