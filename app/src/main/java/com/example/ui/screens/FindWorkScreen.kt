package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WorkOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ui.components.JobItemCard
import com.example.ui.components.dialPhoneNumber
import com.example.ui.strings.AppStrings
import com.example.ui.strings.Language
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.viewmodel.JobSort
import com.example.ui.viewmodel.MeraKaamViewModel
import com.example.ui.viewmodel.Screen

val popularDistricts = listOf("सभी", "वाराणसी", "पटना", "जयपुर", "लखनऊ", "इंदौर", "दिल्ली NCR", "कानपुर", "मेरठ", "भोपाल")

val workCategoriesFilter = listOf(
    "सभी",
    "निर्माण",
    "कृषि",
    "पेंटिंग",
    "प्लंबिंग",
    "इलेक्ट्रीशियन",
    "लोडिंग",
    "कारपेंटर",
    "सफाई"
)

@Composable
fun FindWorkScreen(
    viewModel: MeraKaamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val filteredJobs by viewModel.filteredJobs.collectAsState()
    val savedJobIds by viewModel.savedJobIds.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedDistrict by viewModel.selectedDistrict.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF6))
    ) {
        // Search & Filter Header Box
        Surface(
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = {
                        Text(AppStrings.searchPlaceholder(language), fontSize = 13.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = GreenPrimaryDark)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("find_work_search_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // District Filter Chips (Horizontal)
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
                            onClick = { viewModel.selectedDistrict.value = dist },
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
                            modifier = Modifier.testTag("filter_district_${dist}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Work Category Filter Chips (Horizontal)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == Language.HINDI) "कार्य:" else "Work:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Gray
                    )
                    workCategoriesFilter.forEach { cat ->
                        val isSelected = selectedCategory == cat || (cat == "सभी" && selectedCategory == "All")
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectedCategory.value = cat },
                            label = {
                                Text(
                                    text = if (cat == "सभी" && language == Language.ENGLISH) "All" else cat,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2E7D32),
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("filter_category_${cat}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Sorting Row & Count
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == Language.HINDI) "कुल ${filteredJobs.size} काम उपलब्ध" else "${filteredJobs.size} jobs found",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FilterList, contentDescription = "Sort", modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (sortOption == JobSort.NEWEST) {
                                if (language == Language.HINDI) "नया काम पहले" else "Newest First"
                            } else {
                                if (language == Language.HINDI) "ज्यादा दिहाड़ी" else "Highest Wage"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimaryDark
                            ),
                            modifier = Modifier
                                .testTag("toggle_sort_button")
                                .background(GreenContainer, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .clickable {
                                    viewModel.sortOption.value = if (sortOption == JobSort.NEWEST) {
                                        JobSort.HIGHEST_WAGE
                                    } else {
                                        JobSort.NEWEST
                                    }
                                }
                        )
                    }
                }
            }
        }

        // Job List or Empty State
        if (filteredJobs.isEmpty()) {
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
                        imageVector = Icons.Default.WorkOff,
                        contentDescription = "No jobs",
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (language == Language.HINDI) "इस खोज में कोई काम नहीं मिला" else "No jobs found for this search",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == Language.HINDI) "कृपया जिला या श्रेणी बदलकर दोबारा प्रयास करें।" else "Please try changing district or category filters.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.searchQuery.value = ""
                            viewModel.selectedDistrict.value = "सभी"
                            viewModel.selectedCategory.value = "सभी"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryDark),
                        modifier = Modifier.testTag("reset_filters_button")
                    ) {
                        Text(if (language == Language.HINDI) "सभी फ़िल्टर हटाएं" else "Clear All Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredJobs, key = { it.id }) { job ->
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
    }
}
