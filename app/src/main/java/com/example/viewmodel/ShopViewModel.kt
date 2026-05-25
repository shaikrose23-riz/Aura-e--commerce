package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.CartItem
import com.example.data.database.FavoriteItem
import com.example.data.database.OrderItem
import com.example.data.models.Product
import com.example.data.repository.ProductRepository
import com.example.data.repository.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    data class ProductDetail(val productId: Int) : Screen()
    object Cart : Screen()
    object Favorites : Screen()
    object Profile : Screen()
}

class ShopViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val productRepository = ProductRepository()
    private val roomRepository = RoomRepository(
        database.cartDao(),
        database.favoriteDao(),
        database.orderDao()
    )

    // Products catalog reference
    val allProducts: List<Product> = productRepository.getAllProducts()
    val categories: List<String> = productRepository.getCategories()

    // Navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen

    // Search and filters
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Filtered products state combining query & category
    val filteredProducts: StateFlow<List<Product>> = combine(
        _selectedCategory,
        _searchQuery
    ) { category, query ->
        allProducts.filter { product ->
            val matchesCategory = (category == "All" || product.category == category)
            val matchesSearch = product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = allProducts
    )

    // Database flows
    val cartItems: StateFlow<List<CartItem>> = roomRepository.cartItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favorites: StateFlow<List<FavoriteItem>> = roomRepository.favorites.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val orders: StateFlow<List<OrderItem>> = roomRepository.orders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Navigation helpers
    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Room DB actions
    fun addToCart(product: Product, quantity: Int, size: String, colorHex: Long) {
        viewModelScope.launch {
            val colorString = String.format("#%08X", colorHex)
            roomRepository.addToCart(
                productId = product.id,
                quantity = quantity,
                size = size,
                color = colorString
            )
        }
    }

    fun updateCartQuantity(cartId: Int, newQuantity: Int) {
        viewModelScope.launch {
            roomRepository.updateCartQuantity(cartId, newQuantity)
        }
    }

    fun removeCartItem(cartId: Int) {
        viewModelScope.launch {
            roomRepository.removeCartItem(cartId)
        }
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            roomRepository.toggleFavorite(productId)
        }
    }

    fun checkout(totalAmount: Double) {
        viewModelScope.launch {
            val currentCart = cartItems.value
            if (currentCart.isEmpty()) return@launch

            // Create a brief list summarizer
            val summaryLines = currentCart.mapNotNull { cartItem ->
                val prod = allProducts.find { it.id == cartItem.productId }
                if (prod != null) {
                    "${cartItem.quantity}x ${prod.name} (${cartItem.size})"
                } else null
            }
            val summary = summaryLines.joinToString(", ")
            val count = currentCart.sumOf { it.quantity }

            roomRepository.saveOrder(totalAmount, summary, count)
            roomRepository.clearCart()
            navigateTo(Screen.Profile)
        }
    }

    // Checking helper
    fun isProductInWishlist(productId: Int): Boolean {
        return favorites.value.any { it.productId == productId }
    }

    // Factory Provider
    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ShopViewModel(application) as T
                }
            }
    }
}
