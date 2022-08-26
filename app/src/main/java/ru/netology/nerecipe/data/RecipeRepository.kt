package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.dto.Recipe

interface RecipeRepository {
    fun get(): LiveData<List<Recipe>>
    fun delete(id: Long)
    fun like(id: Long)
    fun deleteAllFromFavorites()
    fun save(recipe: Recipe)
    fun filterByCategoryMain(categoryId: Int): Boolean
    fun removeCategoryFromFilterChips(categoryId: Int)
    fun addCategoryToFilterChips(categoryId: Int)
    fun startFilterChips()
    fun search(query: String)

    companion object {
        const val NEW_REC_ID = 0L
        const val NEW_STEP_ID = 0L
    }

}