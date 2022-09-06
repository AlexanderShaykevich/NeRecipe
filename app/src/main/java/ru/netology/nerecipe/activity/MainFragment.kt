package ru.netology.nerecipe.activity

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.data.RecipeAdapter
import ru.netology.nerecipe.data.RecipeViewModel
import ru.netology.nerecipe.databinding.FragmentMainBinding
import ru.netology.nerecipe.dto.Recipe
import java.util.*

class MainFragment : Fragment() {
    val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)
    lateinit var binding: FragmentMainBinding
    var list = mutableListOf<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val adapter = RecipeAdapter(requireContext(), viewModel)
        binding.recyclerView.adapter = adapter

        with(binding) {

            fab.setOnClickListener {
                findNavController().navigate(R.id.action_recipes_to_newRecipeFragment)
            }

            searchClear.setOnClickListener {
                search.text?.clear()
                viewModel.clearSearch()
            }

            searchButton.setOnClickListener {
                if (search.text.isNullOrBlank()) return@setOnClickListener
                viewModel.onSearchListener(search.text.toString())
            }

        }

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            list = it.toMutableList()
            binding.emptyTextGroup.isVisible = it.isEmpty()
        }

        viewModel.openRecipeEvent.observe(viewLifecycleOwner) { id ->
            findNavController().navigate(
                R.id.action_recipes_to_recipeFragment,
                Bundle().apply {
                    putLong(KEY_ID, id)
                })
        }

        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = viewHolder.absoluteAdapterPosition
                val targetPosition = target.absoluteAdapterPosition
                Collections.swap(list, sourcePosition, targetPosition)
                adapter.notifyItemMoved(sourcePosition, targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

        })

        touchHelper.attachToRecyclerView(binding.recyclerView)

        binding.filterChipsGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            viewModel.onFilterClickedListener()

            for (id in checkedIds) {
                checkedChipsFilter(id)
            }

        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_filter -> {
                        with(binding) {
                            filterChipsGroup.isVisible = !filterChipsGroup.isVisible
                            searchGroup.isVisible = false
                            if (!filterChipsGroup.isVisible) {
                                makeAllChipsChecked()
                            } else {
                                viewModel.onFilterClickedListener()
                            }
                        }
                        true
                    }
                    R.id.action_search -> {
                        makeChipsGroupInvisible()
                        with(binding) {
                            searchGroup.isVisible = !searchGroup.isVisible
                            search.requestFocus()
                            true
                        }
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun checkedChipsFilter(id: Int) {
        when (id) {
            binding.chipEuropean.id -> viewModel.onAddToFilterCategoryListener(EUROPEAN)
            binding.chipAmerican.id -> viewModel.onAddToFilterCategoryListener(AMERICAN)
            binding.chipAsian.id -> viewModel.onAddToFilterCategoryListener(ASIAN)
            binding.chipEastern.id -> viewModel.onAddToFilterCategoryListener(EASTERN)
            binding.chipMediterranean.id -> viewModel.onAddToFilterCategoryListener(MEDITERRANEAN)
            binding.chipRussian.id -> viewModel.onAddToFilterCategoryListener(RUSSIAN)
            binding.chipPanAsiatic.id -> viewModel.onAddToFilterCategoryListener(PAN_ASIATIC)
        }
    }

    private fun makeAllChipsChecked() {
        with(binding) {
            chipAmerican.isChecked = true
            chipMediterranean.isChecked = true
            chipRussian.isChecked = true
            chipEastern.isChecked = true
            chipAsian.isChecked = true
            chipPanAsiatic.isChecked = true
            chipEuropean.isChecked = true
        }
    }

    private fun makeChipsGroupInvisible() {
        binding.filterChipsGroup.isVisible = false
    }

    companion object {
        const val KEY_ID = "id"
        const val EUROPEAN = 0
        const val ASIAN = 1
        const val PAN_ASIATIC = 2
        const val EASTERN = 3
        const val AMERICAN = 4
        const val RUSSIAN = 5
        const val MEDITERRANEAN = 6
    }


}