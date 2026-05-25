package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.screens.CartScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.Screen
import com.example.viewmodel.ShopViewModel

class MainActivity : ComponentActivity() {
    private val shopViewModel: ShopViewModel by viewModels {
        ShopViewModel.provideFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppLayout(viewModel = shopViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainAppLayout(viewModel: ShopViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val totalCartUnits = cartItems.sumOf { it.quantity }

    // Navigation setup
    val navigationItems = listOf(
        NavigationItemData("Shop", Screen.Home, Icons.Filled.ShoppingBag, Icons.Outlined.ShoppingBag, "tab_home"),
        NavigationItemData("Wishlist", Screen.Favorites, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, "tab_favorites"),
        NavigationItemData("Cart", Screen.Cart, Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart, "tab_cart", badgeCount = totalCartUnits),
        NavigationItemData("Profile", Screen.Profile, Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
    )

    Scaffold(
        topBar = {
            // Only show main header when not on detailed description screen
            if (currentScreen !is Screen.ProductDetail) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "AURA SHOP",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Explore limits of futuristic craft",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },
        bottomBar = {
            // Fluidly hide bottom buttons when reading specific product specifications
            if (currentScreen !is Screen.ProductDetail) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("app_navigation_bar")
                ) {
                    navigationItems.forEach { navItem ->
                        val isSelected = when (currentScreen) {
                            Screen.Home -> navItem.screen is Screen.Home
                            Screen.Favorites -> navItem.screen is Screen.Favorites
                            Screen.Cart -> navItem.screen is Screen.Cart
                            Screen.Profile -> navItem.screen is Screen.Profile
                            else -> false
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(navItem.screen) },
                            icon = {
                                if (navItem.badgeCount > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.error,
                                                contentColor = MaterialTheme.colorScheme.onError
                                            ) {
                                                Text(
                                                    text = navItem.badgeCount.toString(),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.testTag("nav_badge_${navItem.label.lowercase()}")
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) navItem.activeIcon else navItem.inactiveIcon,
                                            contentDescription = navItem.label,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (isSelected) navItem.activeIcon else navItem.inactiveIcon,
                                        contentDescription = navItem.label,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = navItem.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag(navItem.tag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Render view-switcher with smooth fade effects
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn() with fadeOut()
                },
                modifier = Modifier.fillMaxSize()
            ) { screen ->
                when (screen) {
                    is Screen.Home -> HomeScreen(viewModel = viewModel)
                    is Screen.Favorites -> FavoritesScreen(viewModel = viewModel)
                    is Screen.Cart -> CartScreen(viewModel = viewModel)
                    is Screen.Profile -> ProfileScreen(viewModel = viewModel)
                    is Screen.ProductDetail -> ProductDetailScreen(
                        productId = screen.productId,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

data class NavigationItemData(
    val label: String,
    val screen: Screen,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val tag: String,
    val badgeCount: Int = 0
)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
