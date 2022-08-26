package ru.netology.nerecipe.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.EditStepArgs
import ru.netology.nerecipe.data.RecipeViewModel
import ru.netology.nerecipe.databinding.FragmentNewStepBinding

class NewStepFragment : Fragment() {
    var imageUri: String? = null
//    private val idPref = requireActivity().getSharedPreferences("stepId", Context.MODE_PRIVATE)

//    init {
//        nextId = idPref.getLong(KEY_STEP_ID, 0)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewStepBinding.inflate(inflater, container, false)
        val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

        viewModel.imageUriStep.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.imageStep.isVisible = true
                Glide
                    .with(requireActivity())
                    .load(it)
                    .into(binding.imageStep)
            }
        }

        arguments?.EditStepArgs?.let {
            requireActivity().title = "Редактирование"

            with(binding) {
                textStep.setText(it.content)
                viewModel.imageUriStep.value = it.imageUri
            }
        }

        val image = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let {
                requireActivity().contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModel.imageUriStep.value = it.toString()

            }
        }

        binding.buttonImage.setOnClickListener {
            image.launch(arrayOf("image/*"))
        }

        binding.buttonSave.setOnClickListener {
            if (binding.textStep.text.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    R.string.toast_empty_text,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.onSaveStepListener(binding.textStep.text.toString())
//            saveId()
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.close_newRecipeFragment)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.OK) { dialog, _ ->
                    viewModel.imageUriStep.value = null
                    findNavController().navigateUp()
                    dialog.dismiss()
                }
                .show()
        }


        return binding.root
    }



}