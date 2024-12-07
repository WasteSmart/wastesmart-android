package com.frxcl.wastesmart.ui.activity.encyclopedia

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityEncyclopediaWasteTypeBinding
import com.frxcl.wastesmart.ui.adapter.WasteNonOrganicExampleGridAdapter
import com.frxcl.wastesmart.ui.adapter.WasteOrganicExampleGridAdapter
import com.frxcl.wastesmart.ui.adapter.WasteToxicExampleGrid

class EncyclopediaWasteTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEncyclopediaWasteTypeBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEncyclopediaWasteTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory: EncyclopediaViewModelFactory = EncyclopediaViewModelFactory.getInstance(this)
        val viewModel: EncyclopediaViewModel by viewModels {
            factory
        }

        val id = intent.getIntExtra("idCat", 0)

        when (id) {
            1 -> { viewModel.getEncyclopediaOrganic()
                viewModel.getEncyclopediaOrganicExample()
                viewModel.encOrganicData.observe(this, Observer { result ->
                    val title = result?.typeOfWaste?.title
                    val desc = result?.typeOfWaste?.description
                    val imageUrl = result?.typeOfWaste?.imageUrl
                    val howToManage = result?.typeOfWaste?.howToManage
                    setContent(title, desc, imageUrl, howToManage)
                })
                viewModel.encOrganicExampleData.observe(this, Observer { result ->
                    val gridLayoutManager = GridLayoutManager(this, 2).apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                val adapter = result?.let { WasteOrganicExampleGridAdapter(it) }
                                val itemCount = adapter?.itemCount ?: 0

                                return if (position == itemCount - 1 && itemCount % spanCount != 0) {
                                    spanCount
                                } else {
                                    1
                                }
                            }
                        }
                    }

                    binding.rvWasteExample.apply {
                        layoutManager = gridLayoutManager
                        setHasFixedSize(true)
                    }

                    val adapter = result?.let { WasteOrganicExampleGridAdapter(it) }
                    binding.rvWasteExample.adapter = adapter
                })
            }
            2 -> { viewModel.getEncyclopediaNonOrganic()
                viewModel.getEncyclopediaNonOrganicExample()
                viewModel.encNonOrganicData.observe(this, Observer { result ->
                    val title = result?.typeOfWaste?.title
                    val desc = result?.typeOfWaste?.description
                    val imageUrl = result?.typeOfWaste?.imageUrl
                    val howToManage = result?.typeOfWaste?.howToManage
                    setContent(title, desc, imageUrl, howToManage)
                })
                viewModel.encNonOrganicExampleData.observe(this, Observer { result ->
                    val gridLayoutManager = GridLayoutManager(this, 2).apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                val adapter = result?.let { WasteNonOrganicExampleGridAdapter(it) }
                                val itemCount = adapter?.itemCount ?: 0

                                return if (position == itemCount - 1 && itemCount % spanCount != 0) {
                                    spanCount
                                } else {
                                    1
                                }
                            }
                        }
                    }

                    binding.rvWasteExample.apply {
                        layoutManager = gridLayoutManager
                        setHasFixedSize(true)
                    }

                    val adapter = result?.let { WasteNonOrganicExampleGridAdapter(it) }
                    binding.rvWasteExample.adapter = adapter
                })
            }
            3 -> { viewModel.getEncyclopediaToxic()
                viewModel.getEncyclopediaToxicExample()
                viewModel.encToxicData.observe(this, Observer { result ->
                    val title = result?.typeOfWaste?.title
                    val desc = result?.typeOfWaste?.description
                    val imageUrl = result?.typeOfWaste?.imageUrl
                    val howToManage = result?.typeOfWaste?.howToManage
                    setContent(title, desc, imageUrl, howToManage)
                })
                viewModel.encToxicExampleData.observe(this, Observer { result ->
                    val gridLayoutManager = GridLayoutManager(this, 2).apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                val adapter = result?.let { WasteToxicExampleGrid(it) }
                                val itemCount = adapter?.itemCount ?: 0

                                return if (position == itemCount - 1 && itemCount % spanCount != 0) {
                                    spanCount
                                } else {
                                    1
                                }
                            }
                        }
                    }

                    binding.rvWasteExample.apply {
                        layoutManager = gridLayoutManager
                        setHasFixedSize(true)
                    }

                    val adapter = result?.let { WasteToxicExampleGrid(it) }
                    binding.rvWasteExample.adapter = adapter
                })
            }
        }

        viewModel.getEncyclopediaOrganicExample()
        viewModel.encOrganicExampleData.observe(this, Observer { result ->

        })
    }

    fun setContent(title: String?, desc: String?, imageUrl: String?, howToManage: String?) {
        binding.textViewTitle.text = title
        Glide.with(this)
            .load(imageUrl)
            .into(binding.imageViewWaste)
        binding.textViewDesc.text = desc
        binding.textViewRVTitle.text = "Contoh ${title}"
        binding.textViewCaraMengelola.text = howToManage
    }

    fun setRVContent(items: List<String>) {

    }
}