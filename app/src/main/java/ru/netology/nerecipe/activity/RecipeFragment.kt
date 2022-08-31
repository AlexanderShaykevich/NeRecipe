package ru.netology.nerecipe.activity

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.MainFragment.Companion.KEY_ID
import ru.netology.nerecipe.data.RecipeViewModel
import ru.netology.nerecipe.data.StepAdapter
import ru.netology.nerecipe.databinding.FragmentRecipeBinding
import ru.netology.nerecipe.dto.Recipe

class RecipeFragment : Fragment() {
    private val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)
    lateinit var binding: FragmentRecipeBinding
    lateinit var recipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val viewHolder = RecipeViewHolder(binding, requireContext())
        val id = arguments?.getLong(KEY_ID)

        val adapter = StepAdapter(requireContext())
        binding.rvRecipe.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            recipes.firstOrNull { it.id == id }?.let {
                viewHolder.bind(it)
                recipe = it
                adapter.submitList(it.steps)
                return@observe
            }
            findNavController().navigateUp()
        }

        viewModel.editRecipeEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_recipeFragment_to_newRecipeFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_recipe, menu)
                if(recipe.inFavorites) {
                    menu.findItem(R.id.action_like).setIcon(R.drawable.ic_favorite_red_24)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        viewModel.onEditListener(recipe)
                        true
                    }
                    R.id.menu_delete -> {
                        viewModel.onDeleteListener(recipe)
                        true
                    }
                    R.id.action_like -> {
                        viewModel.onFavoritesAddListener(recipe)
                        if (recipe.inFavorites) {
                            menuItem.setIcon(R.drawable.ic_favorite_red_24)
                        } else {
                            menuItem.setIcon(R.drawable.ic_favorite_border_white_24)
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


}

class RecipeViewHolder(
    private val binding: FragmentRecipeBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        binding.apply {
            recipeName.text = recipe.name
            recipeCategory.text = recipe.category
            recipe.imageUri?.let {
                Glide
                    .with(context)
                    .load(it)
                    .into(recipeImage)
            }

        }
    }

}
