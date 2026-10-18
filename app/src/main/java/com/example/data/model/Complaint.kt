package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey
    val id: String,
    val targetType: String, // "JOB" or "USER"
    val targetId: String,
    val targetTitle: String,
    val reason: String,
    val details: String,
    val reporterPhone: String,
    val status: String = "PENDING", // "PENDING", "RESOLVED", "ACTION_TAKEN"
    val createdAt: Long = System.currentTimeMillis()
)
