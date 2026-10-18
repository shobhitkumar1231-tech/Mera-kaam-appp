package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobEntity
import com.example.data.model.WorkerEntity
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.GreenPrimaryMedium
import com.example.ui.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeraKaamTopBar(
    title: String,
    language: Language,
    currentRole: UserRole,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    onLanguageSelected: (Language) -> Unit,
    onRoleClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = AppStrings.tagline(language),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            // Segmented Language Toggle
            LanguageToggle(
                currentLanguage = language,
                onLanguageSelected = onLanguageSelected,
                isDarkThemeOrContrast = true,
                modifier = Modifier.padding(end = 4.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            // User Role Tag
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onRoleClick() }
                    .padding(end = 8.dp),
                color = when (currentRole) {
                    UserRole.WORKER -> Color(0xFFC8E6C9)
                    UserRole.EMPLOYER -> Color(0xFFFFECB3)
                    UserRole.ADMIN -> Color(0xFFFFCDD2)
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = when (currentRole) {
                        UserRole.WORKER -> if (language == Language.HINDI) "मजदूर" else "Worker"
                        UserRole.EMPLOYER -> if (language == Language.HINDI) "नियोक्ता" else "Employer"
                        UserRole.ADMIN -> "Admin"
                    },
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    color = Color.Black,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = GreenPrimaryDark,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Composable
fun ScamWarningCard(
    language: Language,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(AmberAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Security Alert",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = AppStrings.scamWarning(language),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF823C00),
                        lineHeight = 16.sp
                    )
                )
                Text(
                    text = AppStrings.zeroFeeGuarantee(language),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = GreenPrimaryDark,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun JobItemCard(
    job: JobEntity,
    language: Language,
    isSaved: Boolean,
    onJobClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onJobClick() }
            .testTag("job_item_${job.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Category Badge, Closed status, and Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = GreenContainer,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = job.category,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimaryDark
                            )
                        )
                    }
                    if (job.isClosed) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = AppStrings.statusClosed(language),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFC62828)
                                )
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier.size(36.dp).testTag("save_job_btn_${job.id}")
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save job",
                        tint = if (isSaved) AmberAccent else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Job Title
            Text(
                text = job.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    lineHeight = 22.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Location & Workers Needed
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = GreenPrimaryMedium,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${job.district}, ${job.locality}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF555555)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = AppStrings.workersNeeded(language, job.workersNeeded),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1B5E20)
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Wage and Call Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == Language.HINDI) "दिहाड़ी मजदूरी" else "Daily Wage",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                    Text(
                        text = "₹${job.dailyWage} ${AppStrings.perDay(language)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = GreenPrimaryDark,
                            fontSize = 18.sp
                        )
                    )
                }

                Row {
                    OutlinedButton(
                        onClick = onJobClick,
                        modifier = Modifier.testTag("details_btn_${job.id}")
                    ) {
                        Text(AppStrings.viewDetails(language), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Button(
                        onClick = onCallClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
                        modifier = Modifier.testTag("call_employer_btn_${job.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(AppStrings.callEmployer(language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkerItemCard(
    worker: WorkerEntity,
    language: Language,
    onCallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("worker_item_${worker.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(GreenContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Worker",
                            tint = GreenPrimaryDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = worker.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${worker.district}, ${worker.locality}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666))
                            )
                        }
                    }
                }

                // Availability badge
                Surface(
                    color = if (worker.isAvailable) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (worker.isAvailable) {
                            if (language == Language.HINDI) "उपलब्ध" else "Available"
                        } else {
                            if (language == Language.HINDI) "व्यस्त" else "Busy"
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (worker.isAvailable) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Skills chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                worker.skills.split(",").forEach { skill ->
                    val cleanSkill = skill.trim()
                    if (cleanSkill.isNotBlank()) {
                        Surface(
                            color = Color(0xFFF1F5F0),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = cleanSkill,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF333333))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Wage and Experience & Contact
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == Language.HINDI) "अनुभव: ${worker.experienceYears} वर्ष" else "Exp: ${worker.experienceYears} yrs",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                    Text(
                        text = "₹${worker.expectedDailyWage} ${AppStrings.perDay(language)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimaryDark,
                            fontSize = 16.sp
                        )
                    )
                }

                if (worker.allowPhoneShare) {
                    Button(
                        onClick = onCallClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
                        modifier = Modifier.testTag("call_worker_btn_${worker.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == Language.HINDI) "संपर्क करें" else "Contact",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Surface(
                        color = Color(0xFFEEEEEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Private",
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == Language.HINDI) "नंबर सुरक्षित" else "Phone hidden",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun dialPhoneNumber(context: Context, phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "फोन डायल नहीं हो सका: $phoneNumber", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Reusable, accessible segmented Language Toggle component.
 * Allows instant 1-tap switching between Hindi and English with crisp visual feedback.
 */
@Composable
fun LanguageToggle(
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    modifier: Modifier = Modifier,
    isDarkThemeOrContrast: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = if (isDarkThemeOrContrast) Color.White.copy(alpha = 0.2f) else Color(0xFFE8F5E9),
        border = BorderStroke(
            1.dp,
            if (isDarkThemeOrContrast) Color.White.copy(alpha = 0.35f) else Color(0xFFC8E6C9)
        ),
        modifier = modifier.testTag("language_toggle_segmented")
    ) {
        Row(
            modifier = Modifier.padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hindi option
            val isHindi = currentLanguage == Language.HINDI
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isHindi) {
                    if (isDarkThemeOrContrast) Color.White else GreenPrimaryDark
                } else Color.Transparent,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onLanguageSelected(Language.HINDI) }
                    .testTag("lang_toggle_hindi")
            ) {
                Text(
                    text = "हिंदी",
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isHindi) FontWeight.ExtraBold else FontWeight.Medium,
                        fontSize = 12.sp,
                        color = if (isHindi) {
                            if (isDarkThemeOrContrast) GreenPrimaryDark else Color.White
                        } else {
                            if (isDarkThemeOrContrast) Color.White.copy(alpha = 0.85f) else Color(0xFF2E7D32)
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.width(2.dp))

            // English option
            val isEnglish = currentLanguage == Language.ENGLISH
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isEnglish) {
                    if (isDarkThemeOrContrast) Color.White else GreenPrimaryDark
                } else Color.Transparent,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onLanguageSelected(Language.ENGLISH) }
                    .testTag("lang_toggle_english")
            ) {
                Text(
                    text = "English",
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isEnglish) FontWeight.ExtraBold else FontWeight.Medium,
                        fontSize = 12.sp,
                        color = if (isEnglish) {
                            if (isDarkThemeOrContrast) GreenPrimaryDark else Color.White
                        } else {
                            if (isDarkThemeOrContrast) Color.White.copy(alpha = 0.85f) else Color(0xFF2E7D32)
                        }
                    )
                )
            }
        }
    }
}

