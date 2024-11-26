package com.frxcl.wastesmart.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frxcl.wastesmart.data.WasteCatData
import com.frxcl.wastesmart.databinding.WasteCatListBinding

class WasteCatGridAdapter (private val items: List<WasteCatData>) :
    RecyclerView.Adapter<WasteCatGridAdapter.GridViewHolder>() {

    class GridViewHolder(private val binding: WasteCatListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WasteCatData) {
            binding.ivImage.setImageResource(item.image)
            binding.tvTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = WasteCatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}