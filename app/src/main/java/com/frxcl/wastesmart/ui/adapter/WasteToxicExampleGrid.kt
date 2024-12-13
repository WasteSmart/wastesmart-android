package com.frxcl.wastesmart.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.data.remote.response.ExamplesItemToxic
import com.frxcl.wastesmart.databinding.WasteExampleListBinding
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaWasteExampleActivity

class WasteToxicExampleGrid (private val items: List<ExamplesItemToxic>) :
    RecyclerView.Adapter<WasteToxicExampleGrid.GridViewHolder>() {

    class GridViewHolder(private val binding: WasteExampleListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExamplesItemToxic) {
            when (item.title) {
                "Baterai" -> binding.ivImage.setImageResource(R.drawable.sample_toxic_battery_icon)
                else -> {
                    Glide.with(itemView.context)
                        .load(item.imageUrl)
                        .into(binding.ivImage)
                }
            }
            binding.tvTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = WasteExampleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val items = items[position]
        holder.bind(items)
        holder.itemView.setOnClickListener{
            val moveToEncyclopediaWasteExample = Intent(holder.itemView.context, EncyclopediaWasteExampleActivity::class.java)
            moveToEncyclopediaWasteExample.putExtra("idExa", items.id)
            moveToEncyclopediaWasteExample.putExtra("titleExa", items.title)
            moveToEncyclopediaWasteExample.putExtra("descExa", items.description)
            moveToEncyclopediaWasteExample.putExtra("imageUrlExa", items.imageUrl)
            holder.itemView.context.startActivity(moveToEncyclopediaWasteExample)
        }
    }

    override fun getItemCount(): Int = items.size
}