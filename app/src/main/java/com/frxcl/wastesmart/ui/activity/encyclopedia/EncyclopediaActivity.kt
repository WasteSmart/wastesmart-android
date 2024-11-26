package com.frxcl.wastesmart.ui.activity.encyclopedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.data.WasteCatData
import com.frxcl.wastesmart.databinding.ActivityEncyclopediaBinding
import com.frxcl.wastesmart.ui.adapter.WasteCatGridAdapter

class EncyclopediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEncyclopediaBinding
    private lateinit var adapter: WasteCatGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEncyclopediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backBtn.setOnClickListener{onBackPressed()}

        val gridItems = listOf(
            WasteCatData(R.drawable.sample_organic_icon, "Organic"),
            WasteCatData(R.drawable.sample_nonorganic_icon, "Non Organic"),
            WasteCatData(R.drawable.sample_b3_icon, "B3"),
        )

        var gridLayoutManager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val adapter = WasteCatGridAdapter(gridItems)
                    val itemCount = adapter?.itemCount ?: 0

                    return if (position == itemCount - 1 && itemCount % spanCount != 0) {
                        spanCount
                    } else {
                        1
                    }
                }
            }
        }

        binding.rvWasteCategory.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
        }

        adapter = WasteCatGridAdapter(gridItems)
        binding.rvWasteCategory.adapter = adapter

    }
}