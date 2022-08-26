package ru.netology.nerecipe.data

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.NewRecipeFragment
import ru.netology.nerecipe.databinding.StepBinding
import ru.netology.nerecipe.dto.Step

class StepAdapter(
    private val context: Context,
) : ListAdapter<Step, StepViewHolder>(StepDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        return StepViewHolder(
            StepBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), context
        )
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class StepViewHolder(
    private val binding: StepBinding,
    private val context: Context,
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
        }
    }

}

class StepDiffCallBack : DiffUtil.ItemCallback<Step>() {
    override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
        return oldItem == newItem
    }

}
