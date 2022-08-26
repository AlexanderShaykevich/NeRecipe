package ru.netology.nerecipe.entity


import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.dto.Step

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "categoryId")
    val categoryId: Int,
    @ColumnInfo(name = "inFavorites")
    val inFavorites: Boolean,
    @ColumnInfo(name = "imageUri")
    val imageUri: String?,
    @ColumnInfo(name = "steps")
    val steps: List<Step>,
)

internal fun RecipeEntity.toModel() = Recipe(
    id = id,
    name = name,
    author = author,
    category = category,
    categoryId = categoryId,
    inFavorites = inFavorites,
    imageUri = imageUri,
    steps = steps
)

internal fun Recipe.toEntity() = RecipeEntity(
    id = id,
    name = name,
    author = author,
    category = category,
    categoryId = categoryId,
    inFavorites = inFavorites,
    imageUri = imageUri,
    steps = steps
)

@ProvidedTypeConverter
class Converter {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Step::class.java).type

    @TypeConverter
    fun fromMutableListOfStepsToString(value: String): List<Step> {
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringToMutableListOfSteps(steps: List<Step>): String {
        return gson.toJson(steps)
    }
}