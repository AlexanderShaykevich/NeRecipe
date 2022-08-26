package ru.netology.nerecipe.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.StepBinding
import ru.netology.nerecipe.dto.Step

class StepEditAdapter(
    private val context: Context,
    private val listener: RecipeInteractionListener
) : ListAdapter<Step, StepEditViewHolder>(StepDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepEditViewHolder {
        return StepEditViewHolder(
            StepBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), context, listener
        )
    }

    override fun onBindViewHolder(holder: StepEditViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}

class StepEditViewHolder(
    private val binding: StepBinding,
    private val context: Context,
    private val listener: RecipeInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(step: Step) {
        binding.apply {
            val position = (layoutPosition + 1).toString()
            stepNumber.text = position
            textStep.text = step.content
            step.image?.let {
                imageStep.isVisible = true
                Glide
                    .with(context)
                    .load(it)
                    .into(imageStep)
            }

            menuStep.apply {
                isVisible = true

                setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.menu_step)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.menu_delete -> {
                                    listener.onDeleteStepListener(step)
                                    true
                                }
                                R.id.menu_edit -> {
                                    listener.onEditStepListener(step)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
            }


        }
    }
}

