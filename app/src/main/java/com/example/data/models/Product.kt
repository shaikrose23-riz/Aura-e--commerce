package com.example.data.models

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val description: String,
    val rating: Double,
    val reviewsCount: Int,
    val colors: List<Long>,
    val sizes: List<String>,
    val visualGradientStart: Long,
    val visualGradientEnd: Long,
    val drawingType: String // e.g. "headphones", "backpack", "speaker", "watch", "lamp", "sneakers"
)
