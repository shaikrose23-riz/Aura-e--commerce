package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.ProductDrawer
import com.example.viewmodel.Screen
import com.example.viewmodel.ShopViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val product = viewModel.allProducts.find { it.id == productId } ?: return
    val favorites by viewModel.favorites.collectAsState()
    val isFavorite = favorites.any { it.productId == product.id }

    var selectedSize by remember { mutableStateOf(product.sizes.first()) }
    var selectedColor by remember { mutableStateOf(product.colors.first()) }
    var purchaseQuantity by remember { mutableStateOf(1) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp) // Leave spacing for sticky footer
        ) {
            // Upper Header Actions Bar
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Home) },
                    modifier = Modifier.testTag("detail_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back back")
                }

                Text(
                    text = product.category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )

                IconButton(
                    onClick = { viewModel.toggleFavorite(product.id) },
                    modifier = Modifier.testTag("detail_fav_button")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite toggle",
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // High Fidelity Giant Product Artwork Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                ProductDrawer(
                    drawingType = product.drawingType,
                    primaryColor = Color(selectedColor),
                    gradientStart = Color(product.visualGradientStart),
                    gradientEnd = Color(product.visualGradientEnd),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Body specifications
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Name & Review stars row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "stars",
                            tint = Color(0xFFF1C40F),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${product.rating}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "${product.reviewsCount} customer reviews",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Long dynamic description
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.35f
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Custom parameters chooser
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Size parameter
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SELECT SIZE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            product.sizes.forEach { size ->
                                val active = size == selectedSize
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (active) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.surfaceContainerHigh
                                        )
                                        .clickable { selectedSize = size }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                        .testTag("size_chip_$size"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = size,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Color variants parameter
                    Column {
                        Text(
                            text = "COLOR FLAVOR",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            product.colors.forEach { hex ->
                                val active = hex == selectedColor
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(hex))
                                        .border(
                                            width = if (active) 3.dp else 0.dp,
                                            color = if (active) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { selectedColor = hex }
                                        .testTag("color_bubble_${String.format("%08X", hex)}")
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Quantity adjustments row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(14.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = { if (purchaseQuantity > 1) purchaseQuantity-- },
                        modifier = Modifier.size(36.dp).testTag("decrease_quantity_btn")
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease count", modifier = Modifier.size(18.dp))
                    }

                    Text(
                        text = purchaseQuantity.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .testTag("quantity_display")
                    )

                    IconButton(
                        onClick = { purchaseQuantity++ },
                        modifier = Modifier.size(36.dp).testTag("increase_quantity_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase count", modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Sticky Bottom CTA frame
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "TOTAL VALUE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        text = String.format("$%.2f", product.price * purchaseQuantity),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = {
                        viewModel.addToCart(product, purchaseQuantity, selectedSize, selectedColor)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Added $purchaseQuantity x ${product.name} to Cart!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = ButtonDefaults.ContentPadding,
                    modifier = Modifier
                        .height(56.dp)
                        .padding(start = 24.dp)
                        .weight(1f)
                        .testTag("add_to_cart_btn")
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Add symbol")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add to Cart", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Snackbar Host for status update banners
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        )
    }
}
