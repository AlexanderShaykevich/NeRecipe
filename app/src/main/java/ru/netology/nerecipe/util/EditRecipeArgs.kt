package ru.netology.nerecipe.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object EditRecipeArgs: ReadWriteProperty<Bundle, EditRecipeResult> {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): EditRecipeResult {
        return thisRef.get(property.name) as EditRecipeResult
    }

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: EditRecipeResult) {
        thisRef.putSerializable(
            property.name,
            EditRecipeResult(value.name, value.categoryId, value.imageUri, value.steps))
    }
}
