package com.example.data.repository

import com.example.data.models.Product

class ProductRepository {
    private val products = listOf(
        Product(
            id = 1,
            name = "Aura SoundMax Wireless",
            category = "Audio",
            price = 289.99,
            description = "Experience rich, spatial acoustics and unparalleled hybrid Active Noise Cancellation. Built with luxurious leatherette memory foam cups and custom 40mm beryllium-reinforced dynamic drivers.",
            rating = 4.8,
            reviewsCount = 124,
            colors = listOf(0xFF1E2022, 0xFF4A4E69, 0xFF223843),
            sizes = listOf("Standard", "Pro XL"),
            visualGradientStart = 0xFF4F46E5,
            visualGradientEnd = 0xFFEC4899,
            drawingType = "headphones"
        ),
        Product(
            id = 2,
            name = "Nomad Tech Backpack",
            category = "Style",
            price = 145.00,
            description = "Sleek, splash-resistant origami-constructed multi-compartment commuter bag. Designed for the modern hybrid workspace, featuring safe suspended laptop padding, magnetic quick-access pockets, and premium modular interior loops.",
            rating = 4.6,
            reviewsCount = 98,
            colors = listOf(0xFF2B2D42, 0xFF8D99AE, 0xFF588157),
            sizes = listOf("15-inch", "17-inch"),
            visualGradientStart = 0xFF10B981,
            visualGradientEnd = 0xFF3B82F6,
            drawingType = "backpack"
        ),
        Product(
            id = 3,
            name = "Aura Pulse Chrono",
            category = "Wellness",
            price = 219.00,
            description = "A sophisticated smartwatch pairing physical mechanical ticking elegance with an ultra-vibrant Always-On AMOLED screen. Monitored with biometric blood oxygen sensors, circadian rhythm sleepers, and metabolic fitness trackers.",
            rating = 4.7,
            reviewsCount = 186,
            colors = listOf(0xFF2D3142, 0xFFF4A261, 0xFF9B5DE5),
            sizes = listOf("40mm", "44mm"),
            visualGradientStart = 0xFF8B5CF6,
            visualGradientEnd = 0xFFEF4444,
            drawingType = "watch"
        ),
        Product(
            id = 4,
            name = "Sphere Ambient Glow",
            category = "Spaces",
            price = 79.99,
            description = "A gorgeous sculptural lighting accent piece that dynamically mimics natural morning sunlight, oceanic waves, and evening firesides. Seamlessly controlled via smart-hub gesture controls and features a built-in calming white noise generator.",
            rating = 4.9,
            reviewsCount = 64,
            colors = listOf(0xFFE07A5F, 0xFFF4F1DE, 0xFF3D405B),
            sizes = listOf("Compact", "Floor Large"),
            visualGradientStart = 0xFFF59E0B,
            visualGradientEnd = 0xFF10B981,
            drawingType = "lamp"
        ),
        Product(
            id = 5,
            name = "Nebula Spatial Pod",
            category = "Audio",
            price = 129.99,
            description = "Boasting raw 360-degree sonic coverage, the Nebula portable speaker features custom resonant bass radiators, extreme IPX7 waterproof integrity, and a gorgeous reactive breathing light ring synced to your active vibes.",
            rating = 4.5,
            reviewsCount = 52,
            colors = listOf(0xFF140D4F, 0xFF0D1B2A, 0xFF9E0059),
            sizes = listOf("Standard"),
            visualGradientStart = 0xFFEC4899,
            visualGradientEnd = 0xFF8B5CF6,
            drawingType = "speaker"
        ),
        Product(
            id = 6,
            name = "Stratum Knit Runner",
            category = "Style",
            price = 110.00,
            description = "Featherlight, breathable knitted footwear structured with high-recovery responsive carbon plates and honeycomb mid-sole cushioning. Perfect for daily cardio metrics or clean structural street urban outfits.",
            rating = 4.4,
            reviewsCount = 115,
            colors = listOf(0xFFD90429, 0xFF2B2D42, 0xFFEDF2F4),
            sizes = listOf("US 8", "US 9", "US 10", "US 11"),
            visualGradientStart = 0xFF3B82F6,
            visualGradientEnd = 0xFFF59E0B,
            drawingType = "sneakers"
        )
    )

    fun getAllProducts(): List<Product> = products

    fun getProductById(id: Int): Product? = products.find { it.id == id }

    fun getCategories(): List<String> = listOf("All") + products.map { it.category }.distinct()
}
