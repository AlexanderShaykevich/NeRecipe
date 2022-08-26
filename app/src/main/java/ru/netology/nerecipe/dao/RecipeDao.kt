package ru.netology.nerecipe.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nerecipe.dto.Step
import ru.netology.nerecipe.entity.RecipeEntity

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun get(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getForFilter(): List<RecipeEntity>

    @Insert
    fun insert(recipe: RecipeEntity)

    @Query(
        """
        UPDATE recipes SET
        name = :name, category = :category, categoryId = :categoryId, 
        imageUri = :imageUri, steps = :steps
        WHERE id = :id
    """
    )
    fun updateRecipeById(
        id: Long,
        name: String,
        category: String,
        categoryId: Int,
        imageUri: String?,
        steps: List<Step>,
    )

    fun save(recipe: RecipeEntity) =
        if (recipe.id == 0L) insert(recipe) else
            updateRecipeById(
                recipe.id, recipe.name, recipe.category, recipe.categoryId, recipe.imageUri,
                recipe.steps
            )

    @Query(
        """
        UPDATE recipes SET
        inFavorites = CASE WHEN inFavorites THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun inFavorites(id: Long)

    @Query(
        """
        UPDATE recipes SET
        inFavorites = 0
        """
    )
    fun clearFavorites()

    @Query("DELETE FROM recipes WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :query || '%' ")
    fun searchByName(query: String): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE categoryId = :id ORDER BY id DESC")
    fun filterByCategory(id: Int): List<RecipeEntity>


}