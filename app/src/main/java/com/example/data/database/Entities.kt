package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val quantity: Int,
    val size: String,
    val color: String
)

@Entity(tableName = "favorite_items")
data class FavoriteItem(
    @PrimaryKey val productId: Int
)

@Entity(tableName = "orders")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val totalAmount: Double,
    val itemCount: Int,
    val itemsSummary: String,
    val status: String = "Processing"
)
