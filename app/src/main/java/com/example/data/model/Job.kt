package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val district: String,
    val locality: String,
    val dailyWage: Int,
    val workersNeeded: Int,
    val startDate: String,
    val duration: String,
    val workingHours: String,
    val employerName: String,
    val employerPhone: String,
    val employerId: String,
    val isClosed: Boolean = false,
    val isApproved: Boolean = true,
    val isPromoted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_jobs")
data class SavedJobEntity(
    @PrimaryKey
    val jobId: String,
    val savedAt: Long = System.currentTimeMillis()
)
