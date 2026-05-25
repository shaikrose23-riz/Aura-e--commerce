package com.example.data.repository

import com.example.data.database.CartDao
import com.example.data.database.CartItem
import com.example.data.database.FavoriteDao
import com.example.data.database.FavoriteItem
import com.example.data.database.OrderDao
import com.example.data.database.OrderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class RoomRepository(
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao,
    private val orderDao: OrderDao
) {
    val cartItems: Flow<List<CartItem>> = cartDao.getCartItems()
    val favorites: Flow<List<FavoriteItem>> = favoriteDao.getFavorites()
    val orders: Flow<List<OrderItem>> = orderDao.getOrders()

    suspend fun addToCart(productId: Int, quantity: Int, size: String, color: String) = withContext(Dispatchers.IO) {
        val existingItems = cartDao.getCartItems().firstOrNull() ?: emptyList()
        val existing = existingItems.find { it.productId == productId && it.size == size && it.color == color }
        if (existing != null) {
            cartDao.updateCartItem(existing.copy(quantity = existing.quantity + quantity))
        } else {
            cartDao.insertCartItem(CartItem(productId = productId, quantity = quantity, size = size, color = color))
        }
    }

    suspend fun updateCartQuantity(cartId: Int, newQuantity: Int) = withContext(Dispatchers.IO) {
        if (newQuantity <= 0) {
            cartDao.deleteCartItemById(cartId)
        } else {
            // Retrieve item and update
            val existingItems = cartDao.getCartItems().firstOrNull() ?: emptyList()
            val item = existingItems.find { it.id == cartId }
            if (item != null) {
                cartDao.updateCartItem(item.copy(quantity = newQuantity))
            }
        }
    }

    suspend fun removeCartItem(cartId: Int) = withContext(Dispatchers.IO) {
        cartDao.deleteCartItemById(cartId)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        cartDao.clearCart()
    }

    suspend fun toggleFavorite(productId: Int) = withContext(Dispatchers.IO) {
        val currentFavs = favoriteDao.getFavorites().firstOrNull() ?: emptyList()
        val exists = currentFavs.any { it.productId == productId }
        if (exists) {
            favoriteDao.deleteFavorite(FavoriteItem(productId))
        } else {
            favoriteDao.insertFavorite(FavoriteItem(productId))
        }
    }

    fun isFavorite(productId: Int): Flow<Boolean> {
        return favoriteDao.isFavorite(productId)
    }

    suspend fun saveOrder(total: Double, itemsSummary: String, count: Int) = withContext(Dispatchers.IO) {
        val order = OrderItem(
            timestamp = System.currentTimeMillis(),
            totalAmount = total,
            itemCount = count,
            itemsSummary = itemsSummary,
            status = "Processing"
        )
        orderDao.insertOrder(order)
    }
}
