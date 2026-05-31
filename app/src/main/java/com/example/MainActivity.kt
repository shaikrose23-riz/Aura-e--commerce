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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Email
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
    val totalCartUnits = cartItems.size // Number of sent contact messages in our database!

    // Navigation setup for Professional Portfolio
    val navigationItems = listOf(
        NavigationItemData("Home", Screen.Home, Icons.Filled.Home, Icons.Outlined.Home, "tab_home"),
        NavigationItemData("About", Screen.Profile, Icons.Filled.Person, Icons.Outlined.Person, "tab_profile"),
        NavigationItemData("Projects", Screen.Favorites, Icons.Filled.Code, Icons.Outlined.Code, "tab_favorites"),
        NavigationItemData("Contact", Screen.Cart, Icons.Filled.Email, Icons.Outlined.Email, "tab_cart", badgeCount = totalCartUnits)
    )

    Scaffold(
        topBar = {
            // Hide header on ProjectDetail to let the detail view shine full bleed
            if (currentScreen !is Screen.ProductDetail) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "SHAROON SHAIK",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.testTag("app_bar_title")
                            )
                            Text(
                                text = "MSc CS & MBA | Aspiring Software Professional",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
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
            // Hide bottom bar on ProjectDetail for spaciousness
            if (currentScreen !is Screen.ProductDetail) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("app_navigation_bar")
                ) {
                    navigationItems.forEach { navItem ->
                        val isSelected = when (currentScreen) {
                            Screen.Home -> navItem.screen is Screen.Home
                            Screen.Profile -> navItem.screen is Screen.Profile
                            Screen.Favorites -> navItem.screen is Screen.Favorites
                            Screen.Cart -> navItem.screen is Screen.Cart
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
                                                containerColor = MaterialTheme.colorScheme.tertiary,
                                                contentColor = MaterialTheme.colorScheme.onTertiary
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
                                    style = MaterialTheme.typography.labelSmall,
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
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn() with fadeOut()
                },
                modifier = Modifier.fillMaxSize()
            ) { screen ->
                when (screen) {
                    is Screen.Home -> HomeScreen(viewModel = viewModel)
                    is Screen.Profile -> ProfileScreen(viewModel = viewModel)
                    is Screen.Favorites -> FavoritesScreen(viewModel = viewModel)
                    is Screen.Cart -> CartScreen(viewModel = viewModel)
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
