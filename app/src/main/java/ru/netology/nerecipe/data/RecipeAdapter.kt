package ru.netology.nerecipe.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.RecipeBinding
import ru.netology.nerecipe.dto.Recipe

class RecipeAdapter(
    private val context: Context,
    private val listener: RecipeInteractionListener
    ) : ListAdapter<Recipe, RecipeViewHolder>(RecipeDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            RecipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener, context
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class RecipeViewHolder(
    private val binding: RecipeBinding,
    private val listener: RecipeInteractionListener,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        binding.apply {
            recipeAuthor.text = recipe.author
            recipeName.text = recipe.name
            recipeCategory.text = recipe.category
            recipe.imageUri?.let {
                    Glide
                        .with(context)
                        .load(it)
                        .into(recipeImage)
            } ?: run {
                recipeImage.setImageResource(R.drawable.eda)
            }

            root.setOnClickListener {
                listener.onRecipeClickListener(recipe)
            }
        }
    }

}

class RecipeDiffCallBack : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }

}
