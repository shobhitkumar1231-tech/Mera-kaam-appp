package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workers")
data class WorkerEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val phone: String,
    val district: String,
    val locality: String,
    val skills: String, // comma separated skill names
    val experienceYears: Int,
    val expectedDailyWage: Int,
    val isAvailable: Boolean = true,
    val preferredLocation: String,
    val allowPhoneShare: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
