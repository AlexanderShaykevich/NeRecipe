package ru.netology.nerecipe.util

import android.net.Uri
import ru.netology.nerecipe.dto.Step
import java.io.Serializable

class EditStepResult(
    val content: String,
    val imageUri: String?,
) : Serializable
