package com.example.mobile_app_lab1.repository.model

data class Character(
    val name: String,
    val alternateNames: List<String>,
    val species: String,
    val house: String,
    val image: String,
)
