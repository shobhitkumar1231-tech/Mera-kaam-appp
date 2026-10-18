package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.WorkerItemCard
import com.example.ui.components.dialPhoneNumber
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.viewmodel.MeraKaamViewModel

val popularWorkerSkills = listOf(
    "सभी",
    "राजमिस्त्री",
    "पेंटर",
    "प्लंबर",
    "इलेक्ट्रीशियन",
    "कारपेंटर",
    "कृषि",
    "लोडिंग",
    "सफाई"
)

@Composable
fun FindWorkersScreen(
    viewModel: MeraKaamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val filteredWorkers by viewModel.filteredWorkers.collectAsState()
    val searchQuery by viewModel.workerSearchQuery.collectAsState()
    val selectedDistrict by viewModel.workerSelectedDistrict.collectAsState()
    val selectedSkill by viewModel.workerSelectedSkill.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6))
    ) {
        // Search & Filters Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.workerSearchQuery.value = it },
                    placeholder = {
                        Text(
                            text = if (language == Language.HINDI) "कारीगर, हुनर या जिला खोजें (जैसे: पेंटर, पटना)..." else "Search worker, skill or district...",
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = GreenPrimaryDark)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("find_workers_search_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // District selector chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == Language.HINDI) "जिला:" else "District:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Gray
                    )
                    popularDistricts.forEach { dist ->
                        val isSelected = selectedDistrict == dist || (dist == "सभी" && selectedDistrict == "All")
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.workerSelectedDistrict.value = dist },
                            label = {
                                Text(
                                    text = if (dist == "सभी" && language == Language.ENGLISH) "All" else dist,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenPrimaryDark,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("filter_worker_dist_${dist}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Skill selector chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == Language.HINDI) "हुनर:" else "Skill:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Gray
                    )
                    popularWorkerSkills.forEach { skill ->
                        val isSelected = selectedSkill == skill || (skill == "सभी" && selectedSkill == "All")
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.workerSelectedSkill.value = skill },
                            label = {
                                Text(
                                    text = if (skill == "सभी" && language == Language.ENGLISH) "All" else skill,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2E7D32),
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("filter_worker_skill_${skill}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (language == Language.HINDI) "कुल ${filteredWorkers.size} कारीगर / मजदूर उपलब्ध" else "${filteredWorkers.size} workers available",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                )
            }
        }

        // Workers List or Empty
        if (filteredWorkers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonOff,
                        contentDescription = "No workers",
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (language == Language.HINDI) "कोई मजदूर नहीं मिला" else "No workers found",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == Language.HINDI) "कृपया जिला या हुनर बदलकर दोबारा खोजें।" else "Please try changing filters.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.workerSearchQuery.value = ""
                            viewModel.workerSelectedDistrict.value = "सभी"
                            viewModel.workerSelectedSkill.value = "सभी"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
                        modifier = Modifier.testTag("reset_worker_filters_button")
                    ) {
                        Text(if (language == Language.HINDI) "फ़िल्टर हटाएं" else "Clear Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredWorkers, key = { it.id }) { worker ->
                    WorkerItemCard(
                        worker = worker,
                        language = language,
                        onCallClick = { dialPhoneNumber(context, worker.phone) }
                    )
                }
            }
        }
    }
}
