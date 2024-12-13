package com.frxcl.wastesmart.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frxcl.wastesmart.data.WasteCategoryData
import com.frxcl.wastesmart.databinding.WasteCatListBinding
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaWasteTypeActivity

class WasteCatGridAdapter (private val items: List<WasteCategoryData>) :
    RecyclerView.Adapter<WasteCatGridAdapter.GridViewHolder>() {

    class GridViewHolder(private val binding: WasteCatListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WasteCategoryData) {
            binding.ivImage.setImageResource(item.image)
            binding.tvTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = WasteCatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val items = items[position]
        holder.bind(items)
        holder.itemView.setOnClickListener{
            val moveToEncyclopediaWasteType = Intent(holder.itemView.context, EncyclopediaWasteTypeActivity::class.java)
            moveToEncyclopediaWasteType.putExtra("idCat", items.id)
            holder.itemView.context.startActivity(moveToEncyclopediaWasteType)
        }
    }

    override fun getItemCount(): Int = items.size
}