package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.database.CartItem
import com.example.data.models.Product
import com.example.ui.components.ProductDrawer
import com.example.viewmodel.Screen
import com.example.viewmodel.ShopViewModel

@Composable
fun CartScreen(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val cartState by viewModel.cartItems.collectAsState()
    val allProducts = viewModel.allProducts

    // Map Room entities to product matches
    val itemsWithProducts = cartState.mapNotNull { cartItem ->
        val matchedProduct = allProducts.find { it.id == cartItem.productId }
        if (matchedProduct != null) {
            Pair(cartItem, matchedProduct)
        } else null
    }

    if (itemsWithProducts.isEmpty()) {
        EmptyCartPlaceholder(viewModel = viewModel)
    } else {
        // Calculate costs
        val subtotal = itemsWithProducts.sumOf { it.first.quantity * it.second.price }
        val shipping = if (subtotal > 150.0) 0.0 else 5.99
        val grandTotal = subtotal + shipping

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Main items scroll lists
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.weight(1.0f)
            ) {
                items(itemsWithProducts, key = { it.first.id }) { (cartItem, product) ->
                    CartItemRow(
                        cartItem = cartItem,
                        product = product,
                        onIncrease = { viewModel.updateCartQuantity(cartItem.id, cartItem.quantity + 1) },
                        onDecrease = { viewModel.updateCartQuantity(cartItem.id, cartItem.quantity - 1) },
                        onRemove = { viewModel.removeCartItem(cartItem.id) }
                    )
                }
            }

            // Summary Checkout panel
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ORDER SUMMARY",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(String.format("$%.2f", subtotal), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Shipping & Delivery", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = if (shipping == 0.0) "FREE" else String.format("$%.2f", shipping),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (shipping == 0.0) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (shipping > 0.0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("Spend $%.2f more for Free Shipping!", 150.0 - subtotal),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Grand Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                        Text(
                            text = String.format("$%.2f", grandTotal),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Final Action Trigger
                    Button(
                        onClick = { viewModel.checkout(grandTotal) },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("checkout_action_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Confirm Checkout Securely", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    product: Product,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var itemColor = Color.DarkGray
    try {
        if (cartItem.color.startsWith("#")) {
            itemColor = Color(android.graphics.Color.parseColor(cartItem.color))
        }
    } catch (_: Exception) {}

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = modifier
            .fillMaxWidth()
            .testTag("cart_item_card_${cartItem.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Visual Miniature
            ProductDrawer(
                drawingType = product.drawingType,
                primaryColor = itemColor,
                gradientStart = Color(product.visualGradientStart),
                gradientEnd = Color(product.visualGradientEnd),
                modifier = Modifier
                    .size(80.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Details section
            Column(modifier = Modifier.weight(1.0f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Size: ${cartItem.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(itemColor)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = String.format("$%.2f", product.price),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Incrementers
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        IconButton(onClick = onDecrease, modifier = Modifier.size(28.dp).testTag("cart_dec_btn_${cartItem.id}")) {
                            Icon(Icons.Default.Remove, contentDescription = "Sub", modifier = Modifier.size(14.dp))
                        }

                        Text(
                            text = cartItem.quantity.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )

                        IconButton(onClick = onIncrease, modifier = Modifier.size(28.dp).testTag("cart_inc_btn_${cartItem.id}")) {
                            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Remove Button
            IconButton(onClick = onRemove, modifier = Modifier.testTag("cart_remove_btn_${cartItem.id}")) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete from cart",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun EmptyCartPlaceholder(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = "Empty bag sign",
            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your digital cart is empty",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Explore the shop to search custom wireless audio,\ntrajectory backpacks, and ambient luminous lighting assets.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { viewModel.navigateTo(Screen.Home) },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Launch Catalog Browser", fontWeight = FontWeight.Bold)
        }
    }
}
