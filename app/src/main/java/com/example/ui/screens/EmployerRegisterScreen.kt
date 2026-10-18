package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.EmployerEntity
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.viewmodel.MeraKaamViewModel
import com.example.ui.viewmodel.Screen

val employerTypesList = listOf(
    "ठेकेदार (Contractor)",
    "किसान (Farmer)",
    "दूकानदार / व्यापारी (Shop Owner / Trader)",
    "घरेलू / मकान मालिक (Household / Home Owner)",
    "कंपनी / फैक्ट्री (Company / Factory)"
)

@Composable
fun EmployerRegisterScreen(
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

    var selectedEmployerType by remember { mutableStateOf(employerTypesList[0]) }
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }
    var businessDetails by remember { mutableStateOf("") }
    var antiFraudAgreed by remember { mutableStateOf(true) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var registeredEmployerSuccess by remember { mutableStateOf<EmployerEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = AppStrings.registerEmployer(language),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
        )
        Text(
            text = if (language == Language.HINDI)
                "ठेकेदार, किसान, दूकानदार और गृहस्वामी यहां रजिस्टर करें ताकि आप आसानी से काम पोस्ट कर सकें और मजदूरों से संपर्क कर सकें।"
            else
                "Contractors, farmers, shop owners and builders register here to post jobs and hire workers directly.",
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

        // Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "आपका नाम या व्यापार / फर्म का नाम *" else "Name or Business Name *") },
                    placeholder = { Text(if (language == Language.HINDI) "उदा: शर्मा कंस्ट्रक्शन या रमेश शर्मा" else "e.g. Ramesh Sharma") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("employer_reg_name_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Phone
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "मोबाइल नंबर (10 अंक) *" else "Mobile Number (10 digits) *") },
                    placeholder = { Text("उदा: 9876543210") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("employer_reg_phone_input"),
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
                            .testTag("employer_reg_district_dropdown"),
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

                // Locality
                OutlinedTextField(
                    value = locality,
                    onValueChange = { locality = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "स्थान / पता / लैंडमार्क *" else "Locality / Address / Landmark *") },
                    placeholder = { Text(if (language == Language.HINDI) "उदा: मेन मार्केट, जीटी रोड" else "e.g. Main Market, GT Road") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("employer_reg_locality_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Employer Type Dropdown
                Box {
                    OutlinedTextField(
                        value = selectedEmployerType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (language == Language.HINDI) "नियोक्ता का प्रकार *" else "Employer Type *") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select Type",
                                modifier = Modifier.clickable { isTypeDropdownExpanded = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isTypeDropdownExpanded = true }
                            .testTag("employer_reg_type_dropdown"),
                        shape = RoundedCornerShape(8.dp)
                    )
                    DropdownMenu(
                        expanded = isTypeDropdownExpanded,
                        onDismissRequest = { isTypeDropdownExpanded = false }
                    ) {
                        employerTypesList.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    selectedEmployerType = type
                                    isTypeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Optional business details
                OutlinedTextField(
                    value = businessDetails,
                    onValueChange = { businessDetails = it },
                    label = { Text(if (language == Language.HINDI) "व्यापार या कार्य का विवरण (वैकल्पिक)" else "Business or Project Details (Optional)") },
                    placeholder = { Text(if (language == Language.HINDI) "उदा: भवन निर्माण और सिविल कार्य" else "e.g. Building Construction & Civil Works") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .testTag("employer_reg_details_input"),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 3
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Fraud Prevention & Safety Checklist
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == Language.HINDI) "सत्यापन एवं सुरक्षा नियम" else "Verification & Safety Rules",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { antiFraudAgreed = !antiFraudAgreed },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = antiFraudAgreed,
                        onCheckedChange = { antiFraudAgreed = it },
                        colors = CheckboxDefaults.colors(checkedColor = GreenPrimaryDark)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.HINDI)
                            "मैं प्रमाणित करता/करती हूँ कि मैं वैध व वास्तविक काम हेतु मजदूर रखूंगा तथा समय पर पूरी मजदूरी नकद दूंगा। मजदूरों से किसी भी प्रकार की कोई एडवांस फीस नहीं ली जाएगी।"
                        else
                            "I certify that all jobs posted are genuine and fair wages will be paid on time without any upfront deductions.",
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
                    errorMessage = if (language == Language.HINDI) "कृपया अपना स्थान/पता लिखें" else "Please enter your address"
                    return@Button
                }
                if (!antiFraudAgreed) {
                    errorMessage = if (language == Language.HINDI) "कृपया सुरक्षा नियमों की सहमति दें" else "Please accept the safety declaration"
                    return@Button
                }

                val typeClean = selectedEmployerType.substringBefore(" (")

                viewModel.registerEmployer(
                    name = name.trim(),
                    phone = phone.trim(),
                    district = district,
                    locality = locality.trim(),
                    employerType = typeClean,
                    businessDetails = businessDetails.trim(),
                    onSuccess = { createdEmployer ->
                        registeredEmployerSuccess = createdEmployer
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_employer_reg_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Business, contentDescription = "Register", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == Language.HINDI) "नियोक्ता रजिस्ट्रेशन पूरा करें" else "Complete Employer Registration",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Success Dialog
    registeredEmployerSuccess?.let { employer ->
        AlertDialog(
            onDismissRequest = {
                registeredEmployerSuccess = null
                viewModel.navigateTo(Screen.PostJob)
            },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = GreenPrimaryDark, modifier = Modifier.size(48.dp))
            },
            title = {
                Text(
                    text = if (language == Language.HINDI) "नियोक्ता रजिस्ट्रेशन सफल!" else "Registration Successful!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (language == Language.HINDI)
                        "बधाई हो ${employer.name}! आप 'मेरा काम' पर सत्यापित नियोक्ता के रूप में पंजीकृत हो गए हैं। अब आप तुरंत नए काम पोस्ट कर सकते हैं।"
                    else
                        "Congratulations ${employer.name}! You are registered as an employer. You can now post jobs."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        registeredEmployerSuccess = null
                        viewModel.navigateTo(Screen.PostJob)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark)
                ) {
                    Text(if (language == Language.HINDI) "काम पोस्ट करें" else "Post a Job")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        registeredEmployerSuccess = null
                        viewModel.navigateTo(Screen.Home)
                    }
                ) {
                    Text(if (language == Language.HINDI) "होम स्क्रीन" else "Home")
                }
            }
        )
    }
}
