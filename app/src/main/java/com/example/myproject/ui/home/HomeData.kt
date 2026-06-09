package com.example.myproject.ui.home

data class HomeData(
    val userName: String,
    val surgeryDayHint: String,
    val recoveryDays: Int,
    val checkInDays: Int,
    val completionRate: Int,
    val bannerTitle: String,
    val bannerSubtitle: String,
    val hasActivePlan: Boolean
)
