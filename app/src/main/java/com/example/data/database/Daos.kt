package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)

    @Delete
    suspend fun deleteCartItem(item: CartItem)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItemById(id: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_items")
    fun getFavorites(): Flow<List<FavoriteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(item: FavoriteItem)

    @Delete
    suspend fun deleteFavorite(item: FavoriteItem)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_items WHERE productId = :productId)")
    fun isFavorite(productId: Int): Flow<Boolean>
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getOrders(): Flow<List<OrderItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderItem)
}
