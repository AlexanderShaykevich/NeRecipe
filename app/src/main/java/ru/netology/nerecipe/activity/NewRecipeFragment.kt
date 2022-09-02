package ru.netology.nerecipe.activity

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.netology.nerecipe.R
import ru.netology.nerecipe.data.RecipeViewModel
import ru.netology.nerecipe.data.StepEditAdapter
import ru.netology.nerecipe.databinding.FragmentNewRecipeBinding

class NewRecipeFragment : Fragment() {
    lateinit var binding: FragmentNewRecipeBinding
    val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewRecipeBinding.inflate(inflater, container, false)

        val adapter = StepEditAdapter(requireContext(), viewModel)
        binding.rvRecipe.adapter = adapter

        viewModel.currentRecipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                with(binding) {
                    recipeName.setText(recipe.name)
                    category.setSelection(recipe.categoryId)
                    viewModel.editStepsMode(recipe.steps)
                    viewModel.imageUriRecipe.value = recipe.imageUri
                }
            }
        }

        viewModel.stepsView.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.imageUriRecipe.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide
                    .with(requireActivity())
                    .load(it)
                    .into(binding.chooseImage)
            }
        }

        val image = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let {
                requireActivity().contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModel.imageUriRecipe.value = it.toString()
            }
        }
        binding.buttonGallery.setOnClickListener {
            image.launch(arrayOf("image/*"))
        }

        binding.buttonLink.setOnClickListener {
            val dialogView =
                LayoutInflater.from(requireContext()).inflate(
                    R.layout.dialog, binding.root, false
                )

            MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.OK) { dialog, _ ->
                    val link = dialogView.findViewById<EditText>(R.id.link_dialog).text.toString()
                    if (link.isNotEmpty()) {
                        viewModel.imageUriRecipe.value = link
                        dialog.dismiss()
                        (dialogView.parent as? ViewGroup)?.removeView(dialogView)
                    }
                }
                .show()
        }

        binding.buttonStepAdd.setOnClickListener {
            saveData()
            findNavController().navigate(R.id.action_newRecipeFragment_to_newStepFragment)
        }

        viewModel.editStepEvent.observe(viewLifecycleOwner) {
            saveData()
            findNavController().navigate(R.id.action_newRecipeFragment_to_newStepFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.close_newRecipeFragment)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.OK) { dialog, _ ->
                    clearData()
                    findNavController().navigateUp()
                    dialog.dismiss()
                }
                .show()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_recipe_edit, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    android.R.id.home -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(R.string.close_newRecipeFragment)
                            .setNegativeButton(R.string.cancel) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(R.string.OK) { dialog, _ ->
                                clearData()
                                findNavController().navigateUp()
                                dialog.dismiss()
                            }
                            .show()
                        return true
                    }

                    R.id.menu_cancel -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(R.string.close_newRecipeFragment)
                            .setNegativeButton(R.string.cancel) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(R.string.OK) { dialog, _ ->
                                clearData()
                                findNavController().navigateUp()
                                dialog.dismiss()
                            }
                            .show()
                        return true
                    }

                    R.id.menu_save -> {
                        val category = binding.category.selectedItem.toString()
                        val categoryId = binding.category.selectedItemPosition
                        val name = binding.recipeName.text.toString()
                        val steps = viewModel.steps

                        if (name.isBlank()) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.toast_recipe_name_empty),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            return false
                        }

                        if (steps.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.toast_recipe_steps_empty),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            return false
                        }

                        viewModel.onSaveRecipeListener(name, category, categoryId)
                        viewModel.clearStepsList()
                        findNavController().navigateUp()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun saveData() {
        viewModel.currentRecipe.value = viewModel.currentRecipe.value?.copy(
            category = binding.category.selectedItem.toString(),
            categoryId = binding.category.selectedItemPosition,
            name = binding.recipeName.text.toString(),
            imageUri = viewModel.imageUriRecipe.value
        )
    }

    private fun clearData() {
        viewModel.clearStepsList()
        viewModel.currentRecipe.value = null
        viewModel.imageUriRecipe.value = null
    }


}

