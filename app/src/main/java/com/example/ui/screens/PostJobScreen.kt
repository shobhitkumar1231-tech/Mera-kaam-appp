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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobEntity
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.viewmodel.MeraKaamViewModel
import com.example.ui.viewmodel.Screen

val availableJobCategories = listOf(
    "निर्माण (Construction)",
    "कृषि (Farming)",
    "पेंटिंग (Painting)",
    "प्लंबिंग (Plumbing)",
    "इलेक्ट्रीशियन (Electrical)",
    "लोडिंग (Loading & Transport)",
    "कारपेंटर (Carpentry)",
    "सफाई (Cleaning)",
    "सामान्य मजदूरी (General Labour)"
)

val availableDistrictsList = listOf(
    "वाराणसी",
    "पटना",
    "जयपुर",
    "लखनऊ",
    "इंदौर",
    "दिल्ली NCR",
    "कानपुर",
    "मेरठ",
    "भोपाल",
    "प्रयागराज",
    "गोरखपुर",
    "मुजफ्फरपुर",
    "गया",
    "आगरा"
)

@Composable
fun PostJobScreen(
    viewModel: MeraKaamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()

    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(availableJobCategories[0]) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }

    var description by remember { mutableStateOf("") }
    var district by remember { mutableStateOf(availableDistrictsList[0]) }
    var isDistrictDropdownExpanded by remember { mutableStateOf(false) }

    var locality by remember { mutableStateOf("") }
    var dailyWageText by remember { mutableStateOf("") }
    var workersNeededText by remember { mutableStateOf("1") }
    var startDate by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var workingHours by remember { mutableStateOf("सुबह 9:00 - शाम 5:30") }

    var employerName by remember { mutableStateOf("") }
    var employerPhone by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var postedJobSuccess by remember { mutableStateOf<JobEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = AppStrings.postJob(language),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
        )
        Text(
            text = if (language == Language.HINDI) "नया काम पोस्ट करें ताकि मजदूर आपसे सीधे संपर्क कर सकें।" else "Post a job to connect directly with daily-wage workers.",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
        )

        // Error Banner
        if (errorMessage != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Error, contentDescription = "Error", tint = Color.Red, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = errorMessage!!, color = Color(0xFFC62828), style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // Section 1: Job Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "1. काम का विवरण" else "1. Job Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "काम का नाम / शीर्षक *" else "Job Title *") },
                    placeholder = { Text(if (language == Language.HINDI) "उदा: 3 पेंटर और 2 हेल्पर चाहिए" else "e.g. Need 3 Painters & 2 Helpers") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_job_title_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Dropdown
                Box {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (language == Language.HINDI) "कार्य श्रेणी *" else "Work Category *") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select Category",
                                modifier = Modifier.clickable { isCategoryDropdownExpanded = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isCategoryDropdownExpanded = true }
                            .testTag("post_job_category_dropdown"),
                        shape = RoundedCornerShape(8.dp)
                    )
                    DropdownMenu(
                        expanded = isCategoryDropdownExpanded,
                        onDismissRequest = { isCategoryDropdownExpanded = false }
                    ) {
                        availableJobCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    selectedCategory = cat
                                    isCategoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "काम का पूरा ब्यौरा *" else "Job Description *") },
                    placeholder = { Text(if (language == Language.HINDI) "काम की प्रकृति, औजार, खाना/चाय की सुविधा आदि लिखें" else "Nature of work, tools, meals, etc.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("post_job_description_input"),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 4
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section 2: Location
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "2. कार्य स्थल (स्थान)" else "2. Work Location",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // District Dropdown
                Box {
                    OutlinedTextField(
                        value = district,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (language == Language.HINDI) "जिला *" else "District *") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select District",
                                modifier = Modifier.clickable { isDistrictDropdownExpanded = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isDistrictDropdownExpanded = true }
                            .testTag("post_job_district_dropdown"),
                        shape = RoundedCornerShape(8.dp)
                    )
                    DropdownMenu(
                        expanded = isDistrictDropdownExpanded,
                        onDismissRequest = { isDistrictDropdownExpanded = false }
                    ) {
                        availableDistrictsList.forEach { dist ->
                            DropdownMenuItem(
                                text = { Text(dist) },
                                onClick = {
                                    district = dist
                                    isDistrictDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Locality / Village
                OutlinedTextField(
                    value = locality,
                    onValueChange = { locality = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "गांव / कस्बा / मोहल्ला / लैंडमार्क *" else "Village / Town / Locality / Landmark *") },
                    placeholder = { Text(if (language == Language.HINDI) "उदा: शिवपुर बाजार, कबीर मठ के पास" else "e.g. Main Market, Near Bus Stand") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_job_locality_input"),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section 3: Wages & Timeline
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "3. मजदूरी व समय" else "3. Wage & Timings",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Daily Wage
                    OutlinedTextField(
                        value = dailyWageText,
                        onValueChange = { dailyWageText = it; errorMessage = null },
                        label = { Text(if (language == Language.HINDI) "दैनिक दिहाड़ी (₹) *" else "Daily Wage (₹) *") },
                        placeholder = { Text("उदा: 650") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("post_job_wage_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Workers Needed
                    OutlinedTextField(
                        value = workersNeededText,
                        onValueChange = { workersNeededText = it; errorMessage = null },
                        label = { Text(if (language == Language.HINDI) "मजदूरों की संख्या *" else "Workers Needed *") },
                        placeholder = { Text("उदा: 3") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("post_job_workers_needed_input"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Start Date
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it; errorMessage = null },
                        label = { Text(if (language == Language.HINDI) "शुरुआत तारीख *" else "Start Date *") },
                        placeholder = { Text(if (language == Language.HINDI) "उदा: कल से" else "e.g. Tomorrow") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("post_job_start_date_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Duration
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it; errorMessage = null },
                        label = { Text(if (language == Language.HINDI) "अवधि *" else "Duration *") },
                        placeholder = { Text(if (language == Language.HINDI) "उदा: 7 दिन" else "e.g. 7 days") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("post_job_duration_input"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Working Hours
                OutlinedTextField(
                    value = workingHours,
                    onValueChange = { workingHours = it },
                    label = { Text(if (language == Language.HINDI) "काम के घंटे" else "Working Hours") },
                    placeholder = { Text("उदा: सुबह 9:00 - शाम 5:30") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_job_hours_input"),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section 4: Employer Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "4. संपर्क जानकारी" else "4. Contact Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Employer Name
                OutlinedTextField(
                    value = employerName,
                    onValueChange = { employerName = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "आपका नाम या फर्म का नाम *" else "Your Name or Firm *") },
                    placeholder = { Text(if (language == Language.HINDI) "उदा: रमेश शर्मा (ठेकेदार)" else "e.g. Ramesh Sharma") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_job_employer_name_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Employer Phone
                OutlinedTextField(
                    value = employerPhone,
                    onValueChange = { employerPhone = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "मोबाइल नंबर (10 अंक) *" else "Mobile Number (10 digits) *") },
                    placeholder = { Text("उदा: 9876543210") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_job_employer_phone_input"),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                // Validation
                if (title.isBlank()) {
                    errorMessage = if (language == Language.HINDI) "कृपया काम का शीर्षक लिखें" else "Please enter job title"
                    return@Button
                }
                if (description.isBlank()) {
                    errorMessage = if (language == Language.HINDI) "कृपया काम का विवरण लिखें" else "Please enter description"
                    return@Button
                }
                if (locality.isBlank()) {
                    errorMessage = if (language == Language.HINDI) "कृपया गांव/कस्बा/स्थान लिखें" else "Please enter work locality"
                    return@Button
                }
                val wage = dailyWageText.toIntOrNull()
                if (wage == null || wage <= 0) {
                    errorMessage = if (language == Language.HINDI) "कृपया वैध दैनिक मजदूरी दर्ज करें (उदा: 500)" else "Please enter a valid daily wage"
                    return@Button
                }
                val workers = workersNeededText.toIntOrNull()
                if (workers == null || workers <= 0) {
                    errorMessage = if (language == Language.HINDI) "कृपया वैध मजदूरों की संख्या दर्ज करें" else "Please enter valid number of workers"
                    return@Button
                }
                if (startDate.isBlank()) {
                    errorMessage = if (language == Language.HINDI) "कृपया शुरुआत की तारीख लिखें" else "Please enter start date"
                    return@Button
                }
                if (duration.isBlank()) {
                    errorMessage = if (language == Language.HINDI) "कृपया काम की अवधि लिखें" else "Please enter duration"
                    return@Button
                }
                if (employerName.isBlank()) {
                    errorMessage = if (language == Language.HINDI) "कृपया अपना नाम लिखें" else "Please enter employer name"
                    return@Button
                }
                if (employerPhone.trim().length < 10) {
                    errorMessage = if (language == Language.HINDI) "कृपया 10 अंकों का वैध मोबाइल नंबर लिखें" else "Please enter a valid 10-digit mobile number"
                    return@Button
                }

                viewModel.postJob(
                    title = title.trim(),
                    category = selectedCategory,
                    description = description.trim(),
                    district = district,
                    locality = locality.trim(),
                    dailyWage = wage,
                    workersNeeded = workers,
                    startDate = startDate.trim(),
                    duration = duration.trim(),
                    workingHours = workingHours.trim(),
                    employerName = employerName.trim(),
                    employerPhone = employerPhone.trim(),
                    onSuccess = { createdJob ->
                        postedJobSuccess = createdJob
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_post_job_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Post", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == Language.HINDI) "काम पोस्ट करें (डेटाबेस में सेव करें)" else "Publish Job (Save to Database)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Success Dialog
    postedJobSuccess?.let { job ->
        AlertDialog(
            onDismissRequest = {
                postedJobSuccess = null
                viewModel.navigateTo(Screen.FindWork)
            },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = GreenPrimaryDark, modifier = Modifier.size(48.dp))
            },
            title = {
                Text(
                    text = if (language == Language.HINDI) "काम सफलतापूर्वक पोस्ट हो गया!" else "Job Posted Successfully!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (language == Language.HINDI)
                        "आपका काम डेटाबेस में सुरक्षित हो गया है और अब सभी मजदूरों को दिख रहा है। आप इसे कभी भी 'मेरा प्रोफाइल' से बंद या डिलीट कर सकते हैं।"
                    else
                        "Your job has been saved to the database and is now visible to workers across India."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        postedJobSuccess = null
                        viewModel.navigateTo(Screen.JobDetail(job.id))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
                    modifier = Modifier.testTag("view_posted_job_button")
                ) {
                    Text(if (language == Language.HINDI) "पोस्ट किया काम देखें" else "View Job")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        postedJobSuccess = null
                        viewModel.navigateTo(Screen.FindWork)
                    }
                ) {
                    Text(if (language == Language.HINDI) "काम सूची देखें" else "All Jobs")
                }
            }
        )
    }
}
