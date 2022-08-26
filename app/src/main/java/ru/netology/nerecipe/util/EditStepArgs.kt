package ru.netology.nerecipe.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object EditStepArgs: ReadWriteProperty<Bundle, EditStepResult> {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): EditStepResult {
        return thisRef.get(property.name) as EditStepResult
    }

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: EditStepResult) {
        thisRef.putSerializable(
            property.name,
            EditStepResult(value.content, value.imageUri))
    }
}