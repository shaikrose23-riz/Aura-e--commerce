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
                    colors = listOf(gradientStart.copy(alpha = 0.18f), gradientEnd.copy(alpha = 0.08f))
                )
            )
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2
            val cy = h / 2

            // Custom procedural tech drawings based on project types
            when (drawingType) {
                "ai_tool" -> {
                    // Draw Starry / Brain Neural network for AI Tool
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(gradientStart.copy(alpha = 0.4f), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = w * 0.42f
                        ),
                        radius = w * 0.42f,
                        center = Offset(cx, cy)
                    )

                    // Node Coordinates
                    val points = listOf(
                        Offset(cx, cy - h * 0.25f),      // Top Node
                        Offset(cx - w * 0.26f, cy - h * 0.05f), // Left Core
                        Offset(cx + w * 0.26f, cy - h * 0.05f), // Right Core
                        Offset(cx - w * 0.18f, cy + h * 0.22f), // Bottom Left
                        Offset(cx + w * 0.18f, cy + h * 0.22f), // Bottom Right
                        Offset(cx, cy + h * 0.05f),      // Central intelligence hub
                    )

                    // Draw connecting pathways (Dashed-like lines)
                    for (i in points.indices) {
                        for (j in i + 1 until points.size) {
                            drawLine(
                                color = primaryColor.copy(alpha = 0.35f),
                                start = points[i],
                                end = points[j],
                                strokeWidth = 5f
                            )
                        }
                    }

                    // Draw glowing hardware central processor nodes
                    points.forEachIndexed { index, pt ->
                        val glowRadius = if (index == 5) 24f else 15f
                        val coreColor = if (index == 5) gradientEnd else primaryColor
                        
                        drawCircle(
                            color = coreColor.copy(alpha = 0.3f),
                            radius = glowRadius * 1.8f,
                            center = pt
                        )
                        drawCircle(
                            color = Color.White,
                            radius = glowRadius * 0.5f,
                            center = pt
                        )
                        drawCircle(
                            color = coreColor,
                            radius = glowRadius,
                            style = Stroke(width = 4f),
                            center = pt
                        )
                    }
                }
                
                "enterprise" -> {
                    // Draw Professional Server / Database Storage Stack
                    val barWidth = w * 0.45f
                    val barHeight = h * 0.14f
                    val gap = h * 0.19f

                    // Draw Background Server Rack framing
                    drawRoundRect(
                        color = Color.Gray.copy(alpha = 0.15f),
                        topLeft = Offset(cx - w * 0.32f, cy - h * 0.38f),
                        size = Size(w * 0.64f, h * 0.76f),
                        cornerRadius = CornerRadius(20f, 20f)
                    )

                    // Draw 3 Stacked Server Disks with glowing LED indicators
                    val levels = listOf(cy - gap, cy, cy + gap)
                    levels.forEachIndexed { index, levelY ->
                        val levelColor = if (index == 0) primaryColor else gradientStart
                        // Standard database cylinder/rack segment
                        drawRoundRect(
                            color = levelColor,
                            topLeft = Offset(cx - barWidth / 2, levelY - barHeight / 2),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(14f, 14f)
                        )

                        // Server disk indentation details
                        drawLine(
                            color = Color.White.copy(alpha = 0.3f),
                            start = Offset(cx - barWidth / 2 + 30f, levelY),
                            end = Offset(cx + barWidth / 2 - 80f, levelY),
                            strokeWidth = 6f
                        )

                        // Glowing status lights (green status LEDs)
                        drawCircle(
                            color = Color(0xFF10B981),
                            radius = 10f,
                            center = Offset(cx + barWidth / 2 - 40f, levelY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 4f,
                            center = Offset(cx + barWidth / 2 - 40f, levelY)
                        )
                    }
                }

                "security" -> {
                    // Security Cryptographic Shield & Padlock core
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(primaryColor.copy(alpha = 0.25f), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = w * 0.45f
                        ),
                        radius = w * 0.45f,
                        center = Offset(cx, cy)
                    )

                    // Draw a protective biometric shield path
                    val shieldPath = Path().apply {
                        moveTo(cx, cy - h * 0.32f)
                        quadraticTo(cx + w * 0.26f, cy - h * 0.32f, cx + w * 0.26f, cy - h * 0.05f)
                        quadraticTo(cx + w * 0.26f, cy + h * 0.22f, cx, cy + h * 0.36f)
                        quadraticTo(cx - w * 0.26f, cy + h * 0.22f, cx - w * 0.26f, cy - h * 0.05f)
                        quadraticTo(cx - w * 0.26f, cy - h * 0.32f, cx, cy - h * 0.32f)
                        close()
                    }

                    // Shell contour
                    drawPath(
                        path = shieldPath,
                        color = primaryColor,
                        style = Stroke(width = 8f)
                    )

                    // Shield background filling
                    drawPath(
                        path = shieldPath,
                        color = primaryColor.copy(alpha = 0.15f)
                    )

                    // Central lock drawing
                    val lockW = w * 0.16f
                    val lockH = h * 0.15f
                    val lockX = cx - lockW / 2
                    val lockY = cy - lockH / 4

                    // Lock shackle arc
                    drawArc(
                        color = Color.White,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = 7f),
                        topLeft = Offset(cx - lockW * 0.4f, lockY - lockH * 0.5f),
                        size = Size(lockW * 0.8f, lockH * 0.82f)
                    )

                    // Lock Body card
                    drawRoundRect(
                        color = gradientEnd,
                        topLeft = Offset(lockX, lockY),
                        size = Size(lockW, lockH),
                        cornerRadius = CornerRadius(10f, 10f)
                    )

                    // Keyhole icon
                    drawCircle(
                        color = Color.White,
                        radius = 8f,
                        center = Offset(cx, lockY + lockH * 0.4f)
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(cx, lockY + lockH * 0.4f),
                        end = Offset(cx, lockY + lockH * 0.75f),
                        strokeWidth = 5f
                    )
                }

                "web_dev" -> {
                    // Browser portal representation with grid mock items
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(cx - w * 0.38f, cy - h * 0.32f),
                        size = Size(w * 0.76f, h * 0.64f),
                        cornerRadius = CornerRadius(16f, 16f),
                        style = Stroke(width = h * 0.02f)
                    )

                    // Filled browser header
                    drawRoundRect(
                        color = primaryColor.copy(alpha = 0.2f),
                        topLeft = Offset(cx - w * 0.37f, cy - h * 0.31f),
                        size = Size(w * 0.74f, h * 0.12f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // Browser Address Bar
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.6f),
                        topLeft = Offset(cx - w * 0.15f, cy - h * 0.28f),
                        size = Size(w * 0.46f, h * 0.06f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    // Window dot indicators (Red, Yellow, Green status dots)
                    val dotY = cy - h * 0.25f
                    drawCircle(color = Color(0xFFEF4444), radius = 6f, center = Offset(cx - w * 0.3f, dotY))
                    drawCircle(color = Color(0xFFF59E0B), radius = 6f, center = Offset(cx - w * 0.24f, dotY))
                    drawCircle(color = Color(0xFF10B981), radius = 6f, center = Offset(cx - w * 0.18f, dotY))

                    // Simulated shopping-cart grid items inside browser preview
                    val itemW = w * 0.26f
                    val itemH = h * 0.15f
                    
                    // Left Grid Block
                    drawRoundRect(
                        color = gradientStart.copy(alpha = 0.6f),
                        topLeft = Offset(cx - w * 0.3f, cy - h * 0.08f),
                        size = Size(itemW, itemH),
                        cornerRadius = CornerRadius(10f, 10f)
                    )

                    // Right Grid block
                    drawRoundRect(
                        color = gradientEnd.copy(alpha = 0.6f),
                        topLeft = Offset(cx + w * 0.04f, cy - h * 0.08f),
                        size = Size(itemW, itemH),
                        cornerRadius = CornerRadius(10f, 10f)
                    )

                    // Bottom navigation lines
                    drawLine(
                        color = primaryColor.copy(alpha = 0.4f),
                        start = Offset(cx - w * 0.28f, cy + h * 0.18f),
                        end = Offset(cx - w * 0.02f, cy + h * 0.18f),
                        strokeWidth = 6f
                    )
                    drawLine(
                        color = primaryColor.copy(alpha = 0.4f),
                        start = Offset(cx + w * 0.06f, cy + h * 0.18f),
                        end = Offset(cx + w * 0.28f, cy + h * 0.18f),
                        strokeWidth = 6f
                    )
                }
            }
        }
    }
}
