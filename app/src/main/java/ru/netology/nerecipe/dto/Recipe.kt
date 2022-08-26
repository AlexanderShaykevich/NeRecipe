package ru.netology.nerecipe.dto

data class Recipe(
    val id: Long,
    val name: String,
    val author: String,
    val category: String,
    val categoryId: Int,
    val inFavorites: Boolean = false,
    val imageUri: String? = null,
    val steps: List<Step>,
    )
