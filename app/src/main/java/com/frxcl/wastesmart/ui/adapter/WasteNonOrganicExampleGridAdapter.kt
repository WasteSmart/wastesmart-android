package com.frxcl.wastesmart.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.data.remote.response.ExamplesItemNonOrganic
import com.frxcl.wastesmart.databinding.WasteExampleListBinding
import com.frxcl.wastesmart.ui.activity.encyclopedia.EncyclopediaWasteExampleActivity

class WasteNonOrganicExampleGridAdapter (private val items: List<ExamplesItemNonOrganic>) :
    RecyclerView.Adapter<WasteNonOrganicExampleGridAdapter.GridViewHolder>() {

    class GridViewHolder(private val binding: WasteExampleListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExamplesItemNonOrganic) {
            when (item.title) {
                "Kardus" -> binding.ivImage.setImageResource(R.drawable.sample_nonorganic_cardboard_icon)
                "Sepatu" -> binding.ivImage.setImageResource(R.drawable.sample_nonorganic_shoes_icon)
                "Kaca" -> binding.ivImage.setImageResource(R.drawable.sample_nonorganic_glass_icon)
                "Logam" -> binding.ivImage.setImageResource(R.drawable.sample_nonorganic_metal_icon)
                "Baju" -> binding.ivImage.setImageResource(R.drawable.sample_nonorganic_clothes_icon)
                "Kertas" -> binding.ivImage.setImageResource(R.drawable.sample_nonorganic_paper_icon)
                "Plastik" -> binding.ivImage.setImageResource(R.drawable.sample_nonorganic_plastic_icon)
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