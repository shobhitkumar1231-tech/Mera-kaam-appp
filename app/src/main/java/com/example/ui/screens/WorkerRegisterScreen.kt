package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.model.WorkerEntity
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.viewmodel.MeraKaamViewModel
import com.example.ui.viewmodel.Screen

val allSkillOptions = listOf(
    "राजमिस्त्री (Mason)",
    "निर्माण मजदूर (Construction Labour)",
    "पेंटर (Painter)",
    "पुट्टी कारीगर (Putty Worker)",
    "प्लंबर (Plumber)",
    "इलेक्ट्रीशियन (Electrician)",
    "कारपेंटर (Carpenter)",
    "कृषि मजदूर (Farm Labour)",
    "लोडिंग / पल्लेदार (Loading)",
    "सफाईकर्मी (Cleaner)",
    "वेल्डर (Welder)",
    "ड्राइवर (Driver)"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkerRegisterScreen(
    viewModel: MeraKaamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var district by remember { mutableStateOf(availableDistrictsList[0]) }
    var isDistrictDropdownExpanded by remember { mutableStateOf(false) }
    var locality by remember { mutableStateOf("") }

    val selectedSkills = remember { mutableStateOf(setOf("राजमिस्त्री (Mason)")) }
    var experienceYearsText by remember { mutableStateOf("3") }
    var expectedWageText by remember { mutableStateOf("600") }
    var isAvailable by remember { mutableStateOf(true) }
    var preferredLocation by remember { mutableStateOf("") }
    var allowPhoneShare by remember { mutableStateOf(true) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var registeredSuccessWorker by remember { mutableStateOf<WorkerEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = AppStrings.registerWorker(language),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
        )
        Text(
            text = if (language == Language.HINDI)
                "पंजीकरण बिल्कुल मुफ्त है। अपना प्रोफाइल बनाएं ताकि स्थानीय ठेकेदार और मालिक आपको काम दे सकें।"
            else
                "100% Free registration. Create your profile so nearby employers and contractors can hire you.",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
        )

        // Error message
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

        // Section 1: Basic Information
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "1. व्यक्तिगत जानकारी" else "1. Personal Information",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "आपका पूरा नाम *" else "Full Name *") },
                    placeholder = { Text(if (language == Language.HINDI) "उदा: राजू कुमार पासवान" else "e.g. Raju Kumar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("worker_reg_name_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "मोबाइल नंबर (10 अंक) *" else "Mobile Number (10 digits) *") },
                    placeholder = { Text("उदा: 9835123456") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("worker_reg_phone_input"),
                    shape = RoundedCornerShape(8.dp)
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
                            .testTag("worker_reg_district_dropdown"),
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

                OutlinedTextField(
                    value = locality,
                    onValueChange = { locality = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "गांव / कस्बा / मोहल्ला *" else "Village / Town / Locality *") },
                    placeholder = { Text(if (language == Language.HINDI) "उदा: दानापुर, आनंद बाजार" else "e.g. Danapur, Anand Bazaar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("worker_reg_locality_input"),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section 2: Skills & Work Experience
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "2. आपका हुनर और काम (एक या अधिक चुनें) *" else "2. Work Skills (Select one or more) *",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    allSkillOptions.forEach { skill ->
                        val isSelected = selectedSkills.value.contains(skill)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val current = selectedSkills.value.toMutableSet()
                                if (isSelected) current.remove(skill) else current.add(skill)
                                selectedSkills.value = current
                            },
                            label = { Text(skill, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenPrimaryDark,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("skill_chip_${skill.take(4)}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Experience
                    OutlinedTextField(
                        value = experienceYearsText,
                        onValueChange = { experienceYearsText = it; errorMessage = null },
                        label = { Text(if (language == Language.HINDI) "अनुभव (वर्ष) *" else "Experience (Yrs) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("worker_reg_exp_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // Expected Daily Wage
                    OutlinedTextField(
                        value = expectedWageText,
                        onValueChange = { expectedWageText = it; errorMessage = null },
                        label = { Text(if (language == Language.HINDI) "अपेक्षित दिहाड़ी (₹) *" else "Expected Wage (₹) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("worker_reg_wage_input"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = preferredLocation,
                    onValueChange = { preferredLocation = it },
                    label = { Text(if (language == Language.HINDI) "पसंदीदा कार्य क्षेत्र" else "Preferred Work Area") },
                    placeholder = { Text(if (language == Language.HINDI) "उदा: पटना और 15 किमी का दायरा" else "e.g. Within 15km of Patna") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("worker_reg_preferred_loc_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Availability Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == Language.HINDI) "अभी काम के लिए उपलब्ध हैं?" else "Available for work right now?",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isAvailable) {
                                if (language == Language.HINDI) "हां, मुझे तुरंत काम चाहिए" else "Yes, ready to work"
                            } else {
                                if (language == Language.HINDI) "नहीं, अभी किसी काम में व्यस्त हूं" else "Currently busy"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(color = if (isAvailable) GreenPrimaryDark else Color.Gray)
                        )
                    }
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { isAvailable = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = GreenPrimaryDark, checkedTrackColor = GreenContainer)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section 3: Privacy Consent
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "3. गोपनीयता व सहमति" else "3. Privacy & Consent",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { allowPhoneShare = !allowPhoneShare },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = allowPhoneShare,
                        onCheckedChange = { allowPhoneShare = it },
                        colors = CheckboxDefaults.colors(checkedColor = GreenPrimaryDark)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.HINDI)
                            "मैं काम देने वाले नियोक्ताओं को सीधे संपर्क करने के लिए मेरा फोन नंबर दिखाने की अनुमति देता/देती हूँ।"
                        else
                            "I give permission to show my contact number so employers can call me directly.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                if (name.isBlank()) {
                    errorMessage = if (language == Language.HINDI) "कृपया अपना नाम लिखें" else "Please enter your name"
                    return@Button
                }
                if (phone.trim().length < 10) {
                    errorMessage = if (language == Language.HINDI) "कृपया 10 अंकों का वैध मोबाइल नंबर दर्ज करें" else "Please enter a valid 10-digit mobile number"
                    return@Button
                }
                if (locality.isBlank()) {
                    errorMessage = if (language == Language.HINDI) "कृपया अपना गांव या कस्बा लिखें" else "Please enter your village or locality"
                    return@Button
                }
                if (selectedSkills.value.isEmpty()) {
                    errorMessage = if (language == Language.HINDI) "कृपया कम से कम एक हुनर चुनें" else "Please select at least one skill"
                    return@Button
                }
                val exp = experienceYearsText.toIntOrNull() ?: 0
                val wage = expectedWageText.toIntOrNull() ?: 500

                val skillsJoined = selectedSkills.value.joinToString(", ") { it.substringBefore(" (") }

                viewModel.registerWorker(
                    name = name.trim(),
                    phone = phone.trim(),
                    district = district,
                    locality = locality.trim(),
                    skills = skillsJoined,
                    experienceYears = exp,
                    expectedDailyWage = wage,
                    isAvailable = isAvailable,
                    preferredLocation = if (preferredLocation.isNotBlank()) preferredLocation.trim() else "$district और आसपास",
                    allowPhoneShare = allowPhoneShare,
                    onSuccess = { createdWorker ->
                        registeredSuccessWorker = createdWorker
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_worker_reg_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = "Register", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == Language.HINDI) "मजदूर रजिस्ट्रेशन पूरा करें" else "Complete Worker Registration",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Success Dialog
    registeredSuccessWorker?.let { worker ->
        AlertDialog(
            onDismissRequest = {
                registeredSuccessWorker = null
                viewModel.navigateTo(Screen.FindWorkers)
            },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = GreenPrimaryDark, modifier = Modifier.size(48.dp))
            },
            title = {
                Text(
                    text = if (language == Language.HINDI) "रजिस्ट्रेशन सफल!" else "Registration Successful!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (language == Language.HINDI)
                        "बधाई हो ${worker.name}! आपका प्रोफाइल 'मेरा काम' डेटाबेस में सुरक्षित हो गया है। अब स्थानीय ठेकेदार और मालिक आपसे संपर्क कर सकेंगे।"
                    else
                        "Congratulations ${worker.name}! Your worker profile is now active and visible to contractors."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        registeredSuccessWorker = null
                        viewModel.navigateTo(Screen.FindWorkers)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark)
                ) {
                    Text(if (language == Language.HINDI) "मजदूर सूची देखें" else "View Worker Directory")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        registeredSuccessWorker = null
                        viewModel.navigateTo(Screen.FindWork)
                    }
                ) {
                    Text(if (language == Language.HINDI) "काम खोजें" else "Find Work")
                }
            }
        )
    }
}
