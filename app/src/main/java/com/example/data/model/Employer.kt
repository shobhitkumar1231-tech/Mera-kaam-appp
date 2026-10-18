package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employers")
data class EmployerEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val phone: String,
    val district: String,
    val locality: String,
    val employerType: String, // Contractor, Farmer, Shop Owner, Household, Company
    val businessDetails: String,
    val isVerified: Boolean = true,
    val isSuspended: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
