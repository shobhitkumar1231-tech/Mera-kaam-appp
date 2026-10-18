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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Security
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
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.viewmodel.MeraKaamViewModel

val complaintReasonsList = listOf(
    "अग्रिम पैसे की मांग (एडवांस फीस घोटाला)",
    "फर्जी काम / गलत पता या फोन नंबर",
    "काम कराने के बाद मजदूरी नहीं दी",
    "दुर्व्यवहार या अभद्र भाषा",
    "अन्य तकनीकी या सामान्य समस्या"
)

@Composable
fun ReportComplaintScreen(
    targetType: String,
    targetId: String,
    targetTitle: String,
    viewModel: MeraKaamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()

    var selectedReason by remember { mutableStateOf(complaintReasonsList[0]) }
    var isReasonDropdownExpanded by remember { mutableStateOf(false) }
    var details by remember { mutableStateOf("") }
    var reporterPhone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Report, contentDescription = "Report", tint = Color(0xFFC62828), modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = AppStrings.reportProblem(language),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
            )
        }

        Text(
            text = if (language == Language.HINDI)
                "हम सभी मजदूरों और नियोक्ताओं की सुरक्षा के लिए प्रतिबद्ध हैं। फर्जी विज्ञापनों और धोखेबाजों के खिलाफ तुरंत कार्रवाई की जाती है।"
            else
                "We take fraud and scam prevention seriously. Reports are reviewed immediately by moderators.",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
        )

        // Target Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = if (language == Language.HINDI) "रिपोर्ट का विषय:" else "Subject:",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                )
                Text(
                    text = targetTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "ID: $targetId • Type: $targetType",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

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
                // Reason Dropdown
                Text(
                    text = if (language == Language.HINDI) "शिकायत का मुख्य कारण चुनें *" else "Select Reason *",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    OutlinedTextField(
                        value = selectedReason,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select",
                                modifier = Modifier.clickable { isReasonDropdownExpanded = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isReasonDropdownExpanded = true }
                            .testTag("complaint_reason_dropdown"),
                        shape = RoundedCornerShape(8.dp)
                    )
                    DropdownMenu(
                        expanded = isReasonDropdownExpanded,
                        onDismissRequest = { isReasonDropdownExpanded = false }
                    ) {
                        complaintReasonsList.forEach { reason ->
                            DropdownMenuItem(
                                text = { Text(reason) },
                                onClick = {
                                    selectedReason = reason
                                    isReasonDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Details
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it; errorMessage = null },
                    label = { Text(if (language == Language.HINDI) "समस्या का पूरा विवरण लिखें *" else "Full Description *") },
                    placeholder = { Text(if (language == Language.HINDI) "घटना की पूरी जानकारी, क्या हुआ, कब हुआ आदि लिखें" else "Explain what happened in detail") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("complaint_details_input"),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Reporter phone
                OutlinedTextField(
                    value = reporterPhone,
                    onValueChange = { reporterPhone = it },
                    label = { Text(if (language == Language.HINDI) "आपका मोबाइल नंबर (वैकल्पिक)" else "Your Phone Number (Optional)") },
                    placeholder = { Text("उदा: 9876543210") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_phone_input"),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (details.isBlank()) {
                    errorMessage = if (language == Language.HINDI) "कृपया समस्या का विवरण लिखें" else "Please describe the problem"
                    return@Button
                }

                viewModel.submitComplaint(
                    targetType = targetType,
                    targetId = targetId,
                    targetTitle = targetTitle,
                    reason = selectedReason,
                    details = details.trim(),
                    reporterPhone = reporterPhone.trim(),
                    onSuccess = {
                        isSubmittedSuccess = true
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_complaint_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (language == Language.HINDI) "शिकायत दर्ज करें" else "Submit Report", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (isSubmittedSuccess) {
        AlertDialog(
            onDismissRequest = {
                isSubmittedSuccess = false
                viewModel.navigateBack()
            },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = GreenPrimaryDark, modifier = Modifier.size(48.dp))
            },
            title = {
                Text(
                    text = if (language == Language.HINDI) "शिकायत सफलतापूर्वक दर्ज हुई" else "Report Submitted",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (language == Language.HINDI)
                        "आपकी शिकायत डेटाबेस में दर्ज हो गई है। हमारी टीम इसकी जांच करेगी और अनुचित गतिविधियों के विरुद्ध आवश्यक कदम उठाएगी।"
                    else
                        "Your complaint has been registered in the database. Our moderation team will investigate."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        isSubmittedSuccess = false
                        viewModel.navigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark)
                ) {
                    Text(if (language == Language.HINDI) "ठीक है" else "OK")
                }
            }
        )
    }
}
