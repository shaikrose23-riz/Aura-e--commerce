package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ProductDrawer(
    drawingType: String,
    primaryColor: Color,
    gradientStart: Color,
    gradientEnd: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(gradientStart.copy(alpha = 0.15f), gradientEnd.copy(alpha = 0.05f))
                )
            )
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2
            val cy = h / 2

            // Custom procedural drawings based on type
            when (drawingType) {
                "headphones" -> {
                    // Draw Headband
                    drawArc(
                        color = primaryColor.copy(alpha = 0.6f),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = h * 0.08f),
                        topLeft = Offset(cx - w * 0.35f, cy - h * 0.35f),
                        size = Size(w * 0.7f, h * 0.7f)
                    )
                    
                    // Connected sliders
                    drawLine(
                        color = Color.Gray,
                        start = Offset(cx - w * 0.35f, cy),
                        end = Offset(cx - w * 0.35f, cy + h * 0.1f),
                        strokeWidth = h * 0.03f
                    )
                    drawLine(
                        color = Color.Gray,
                        start = Offset(cx + w * 0.35f, cy),
                        end = Offset(cx + w * 0.35f, cy + h * 0.1f),
                        strokeWidth = h * 0.03f
                    )

                    // Ear cups (Pill shape cards)
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(cx - w * 0.42f, cy - h * 0.05f),
                        size = Size(w * 0.16f, h * 0.32f),
                        cornerRadius = CornerRadius(w * 0.08f, h * 0.08f)
                    )
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(cx + w * 0.26f, cy - h * 0.05f),
                        size = Size(w * 0.16f, h * 0.32f),
                        cornerRadius = CornerRadius(w * 0.08f, h * 0.08f)
                    )

                    // Glow Inner circle
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.2f),
                        radius = cx * 0.4f,
                        center = Offset(cx, cy)
                    )
                }
                
                "backpack" -> {
                    // Draw Main Backpack Body
                    val path = Path().apply {
                        moveTo(cx - w * 0.3f, cy + h * 0.4f)
                        lineTo(cx - w * 0.3f, cy - h * 0.22f)
                        quadraticTo(cx - w * 0.25f, cy - h * 0.42f, cx, cy - h * 0.42f)
                        quadraticTo(cx + w * 0.25f, cy - h * 0.42f, cx + w * 0.3f, cy - h * 0.22f)
                        lineTo(cx + w * 0.3f, cy + h * 0.4f)
                        close()
                    }
                    drawPath(
                        path = path,
                        color = primaryColor
                    )

                    // Front Pocket
                    drawRoundRect(
                        color = primaryColor.copy(alpha = 0.82f),
                        topLeft = Offset(cx - w * 0.24f, cy),
                        size = Size(w * 0.48f, h * 0.36f),
                        cornerRadius = CornerRadius(w * 0.05f, h * 0.05f)
                    )

                    // Contrast Stripe/Pocket Lock
                    drawLine(
                        color = Color.White.copy(alpha = 0.6f),
                        start = Offset(cx - w * 0.15f, cy + h * 0.12f),
                        end = Offset(cx + w * 0.15f, cy + h * 0.12f),
                        strokeWidth = 6f
                    )
                    
                    // Handle
                    drawArc(
                        color = Color.DarkGray,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = 8f),
                        topLeft = Offset(cx - w * 0.1f, cy - h * 0.48f),
                        size = Size(w * 0.2f, h * 0.12f)
                    )
                }

                "watch" -> {
                    // Watch strap Top/Bottom
                    drawRoundRect(
                        color = Color.DarkGray,
                        topLeft = Offset(cx - w * 0.12f, cy - h * 0.45f),
                        size = Size(w * 0.24f, h * 0.9f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )

                    // Outer dial glowing gradient ring
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(primaryColor, primaryColor.copy(alpha = 0.1f))
                        ),
                        radius = cx * 0.52f,
                        center = Offset(cx, cy)
                    )

                    // Physical Bezel Case
                    drawCircle(
                        color = Color(0xFF1E2022),
                        radius = cx * 0.42f,
                        center = Offset(cx, cy)
                    )

                    // Inner Screen Accent
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.4f),
                        radius = cx * 0.35f,
                        center = Offset(cx, cy)
                    )

                    // Physical Dial Ticks (Hour/Minute Hands)
                    drawLine(
                        color = Color.White,
                        start = Offset(cx, cy),
                        end = Offset(cx + w * 0.2f, cy - h * 0.1f),
                        strokeWidth = 8f
                    )
                    drawLine(
                        color = primaryColor,
                        start = Offset(cx, cy),
                        end = Offset(cx, cy + h * 0.22f),
                        strokeWidth = 5f
                    )
                    
                    // Center pin
                    drawCircle(
                        color = Color.White,
                        radius = 10f,
                        center = Offset(cx, cy)
                    )
                }

                "lamp" -> {
                    // Glowing circular dynamic halo
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(primaryColor.copy(alpha = 0.7f), Color.Transparent)
                        ),
                        radius = cx * 0.62f,
                        center = Offset(cx, cy - h * 0.1f)
                    )

                    // Bulb sphere
                    drawCircle(
                        color = Color.White,
                        radius = cx * 0.25f,
                        center = Offset(cx, cy - h * 0.1f)
                    )

                    // Neck rod
                    drawLine(
                        color = Color.DarkGray,
                        start = Offset(cx, cy - h * 0.1f),
                        end = Offset(cx, cy + h * 0.35f),
                        strokeWidth = h * 0.04f
                    )

                    // Base stand card
                    drawRoundRect(
                        color = Color(0xFF2B2D42),
                        topLeft = Offset(cx - w * 0.22f, cy + h * 0.3f),
                        size = Size(w * 0.44f, h * 0.1f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                }

                "speaker" -> {
                    // Cylindrical rounded speaker base
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(cx - w * 0.26f, cy - h * 0.32f),
                        size = Size(w * 0.52f, h * 0.64f),
                        cornerRadius = CornerRadius(w * 0.16f, w * 0.16f)
                    )

                    // Top buttons row
                    drawCircle(
                        color = Color.White.copy(alpha = 0.5f),
                        radius = 12f,
                        center = Offset(cx - w * 0.12f, cy - h * 0.24f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.5f),
                        radius = 12f,
                        center = Offset(cx, cy - h * 0.24f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.5f),
                        radius = 12f,
                        center = Offset(cx + w * 0.12f, cy - h * 0.24f)
                    )

                    // Speaker Driver circles (Symmetrical subwoofers)
                    drawCircle(
                        color = Color.Black.copy(alpha = 0.25f),
                        radius = cx * 0.24f,
                        center = Offset(cx, cy + h * 0.05f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.2f),
                        radius = cx * 0.15f,
                        center = Offset(cx, cy + h * 0.05f)
                    )
                }

                "sneakers" -> {
                    // Stylish Athletic shoe path
                    val shoePath = Path().apply {
                        // Heel counter
                        moveTo(cx - w * 0.35f, cy + h * 0.22f)
                        // Top ankle rim
                        lineTo(cx - w * 0.32f, cy - h * 0.16f)
                        // Tongue opening curve
                        quadraticTo(cx - w * 0.12f, cy - h * 0.18f, cx, cy - h * 0.05f)
                        // Vamp curve down to toe bumper
                        quadraticTo(cx + w * 0.25f, cy + h * 0.02f, cx + w * 0.4f, cy + h * 0.18f)
                        // Front toe profile
                        lineTo(cx + w * 0.38f, cy + h * 0.24f)
                        // Standard outsole line
                        lineTo(cx - w * 0.35f, cy + h * 0.24f)
                        close()
                    }
                    drawPath(
                        path = shoePath,
                        color = primaryColor
                    )

                    // Contrasting Thick foam outsole plate
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(cx - w * 0.37f, cy + h * 0.22f),
                        size = Size(w * 0.78f, h * 0.08f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // Carbon fiber tread detail
                    drawLine(
                        color = primaryColor.copy(alpha = 0.6f),
                        start = Offset(cx, cy + h * 0.22f),
                        end = Offset(cx + w * 0.1f, cy + h * 0.28f),
                        strokeWidth = 6f
                    )

                    // Lacing cross marks
                    drawLine(color = Color.White, start = Offset(cx - w * 0.08f, cy - h * 0.1f), end = Offset(cx + w * 0.02f, cy - h * 0.06f), strokeWidth = 5f)
                    drawLine(color = Color.White, start = Offset(cx - w * 0.03f, cy - h * 0.14f), end = Offset(cx + w * 0.07f, cy - h * 0.1f), strokeWidth = 5f)
                }
            }
        }
    }
}
