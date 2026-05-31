package com.example.data.repository

import com.example.data.models.Product

class ProductRepository {
    private val products = listOf(
        Product(
            id = 1,
            name = "RizDude AI Text Toolkit",
            category = "AI & Tools",
            price = 98.0, // Represents completion scope or complexity rating
            description = "A powerful AI text processing assistant powered by language model prompts. It automates advanced tasks like multilingual translations, smart sentiment analysis, grammar refining, and summarized text blocks. Designed with a robust Spring Boot backend, interactive React client surfaces, and integration, guaranteeing high quality results.",
            rating = 4.9,
            reviewsCount = 42, // GitHub stars/reviews simulation
            colors = listOf(0xFF8B5CF6, 0xFF3B82F6, 0xFF10B981),
            sizes = listOf("Java", "Spring Boot", "React", "AWS", "Gemini API", "Git"),
            visualGradientStart = 0xFF8B5CF6,
            visualGradientEnd = 0xFFEC4899,
            drawingType = "ai_tool"
        ),
        Product(
            id = 2,
            name = "Employee Management System",
            category = "Enterprise Systems",
            price = 95.0,
            description = "A comprehensive enterprise solution engineered for high-volume staffing operations. Includes robust payroll processing engines, automated timesheet verification modules, secure role-based authorization protocols, and interactive department data charts. Designed with Spring Boot backend, React components, and MySQL databases.",
            rating = 4.7,
            reviewsCount = 28,
            colors = listOf(0xFF10B981, 0xFF3B82F6, 0xFF6B7280),
            sizes = listOf("Java", "Spring Boot", "MySQL", "Hibernate", "REST APIs", "Git"),
            visualGradientStart = 0xFF10B981,
            visualGradientEnd = 0xFF3B82F6,
            drawingType = "enterprise"
        ),
        Product(
            id = 3,
            name = "Fingerprint Authentication System",
            category = "Security & Android",
            price = 96.0,
            description = "A high-security, hardware-integrated mobile biometric authentication service. Leverages physical biometric sensor API interfaces, cryptographic SQLite secure containers, and high-performance Kotlin interfaces to validate identity credentials, preventing unauthorized hardware intrusions.",
            rating = 4.8,
            reviewsCount = 35,
            colors = listOf(0xFFEF4444, 0xFFF59E0B, 0xFF1F2937),
            sizes = listOf("Kotlin", "Android SDK", "Biometrics", "SQLite", "Software Testing", "Git"),
            visualGradientStart = 0xFFEF4444,
            visualGradientEnd = 0xFFF59E0B,
            drawingType = "security"
        ),
        Product(
            id = 4,
            name = "E-Commerce Website",
            category = "Web Applications",
            price = 99.0,
            description = "A modern high-fidelity web sales portal featuring a responsive front-end customer client, high-speed Redis-backed product search fields, persistent shopping states, and interactive order processing flows. Configured with secure Spring Boot servers and a beautiful React design.",
            rating = 5.0,
            reviewsCount = 50,
            colors = listOf(0xFF2563EB, 0xFF4F46E5, 0xFF06B6D4),
            sizes = listOf("React", "Spring Boot", "SQL", "HTML & CSS", "Git & GitHub", "REST APIs"),
            visualGradientStart = 0xFF00C6FF,
            visualGradientEnd = 0xFF0072FF,
            drawingType = "web_dev"
        )
    )

    fun getAllProducts(): List<Product> = products

    fun getProductById(id: Int): Product? = products.find { it.id == id }

    fun getCategories(): List<String> = listOf("All") + products.map { it.category }.distinct()
}
