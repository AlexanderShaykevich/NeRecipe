package ru.netology.nerecipe.data

import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.Step

interface RecipeInteractionListener {
    fun onFavoritesAddListener(recipe: Recipe)
    fun onDeleteAllFromFavoritesListener()
    fun onDeleteListener(recipe: Recipe)
    fun onEditListener(recipe: Recipe)
    fun onRecipeClickListener(recipe: Recipe)
    fun onCategoryListener(categoryId: Int): Boolean
    fun onRemoveFromFilterCategoryListener(categoryId: Int)
    fun onAddToFilterCategoryListener(categoryId: Int)
    fun onFilterClickedListener()
    fun onSearchListener(query: String)
    fun onDeleteStepListener(step: Step)
    fun onEditStepListener(step: Step)
}