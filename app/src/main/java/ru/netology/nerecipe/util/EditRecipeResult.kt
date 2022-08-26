package ru.netology.nerecipe.util

import android.net.Uri
import ru.netology.nerecipe.dto.Step
import java.io.Serializable

class EditRecipeResult(
    val name: String,
    val categoryId: Int,
    val imageUri: String?,
    val steps: List<Step>,
) : Serializable
