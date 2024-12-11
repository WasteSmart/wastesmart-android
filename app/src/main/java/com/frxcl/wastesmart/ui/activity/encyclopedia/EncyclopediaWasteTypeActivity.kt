package com.frxcl.wastesmart.ui.activity.encyclopedia

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
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
import kotlin.properties.Delegates

class EncyclopediaWasteTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEncyclopediaWasteTypeBinding
    private var id by Delegates.notNull<Int>()

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

        binding.backBtn.setOnClickListener { onBackPressed() }

        id = intent.getIntExtra("idCat", 0)

        if (isConnectionOk(this)) {
            getData()
        } else {
            Toast.makeText(
                this,
                "Periksa koneksi internet anda.", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getData() {
        val factory: EncyclopediaViewModelFactory = EncyclopediaViewModelFactory.getInstance(this)
        val viewModel: EncyclopediaViewModel by viewModels {
            factory
        }

        setLoading(true)

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
                    if (result != null) {
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
                        setLoading(false)
                    }
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
                    if (result != null) {
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
                        setLoading(false)
                    }
                })
            }
            3 -> { viewModel.getEncyclopediaToxic()
                viewModel.getEncyclopediaToxicExample()
                viewModel.encToxicData.observe(this, Observer { result ->
                    if (result != null) {
                        val title = result.typeOfWaste?.title
                        val desc = result.typeOfWaste?.description
                        val imageUrl = result.typeOfWaste?.imageUrl
                        val howToManage = result.typeOfWaste?.howToManage
                        setContent(title, desc, imageUrl, howToManage)
                    }
                })
                viewModel.encToxicExampleData.observe(this, Observer { result ->
                    if (result != null) {
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
                        setLoading(false)
                    }
                })
            }
        }
    }

    private fun setContent(title: String?, desc: String?, imageUrl: String?, howToManage: String?) {
        val totalWords = title?.split("\\s+".toRegex())?.size
        if (totalWords != null) {
            if (totalWords >= 2 ) {
                binding.textViewTitle.text = title.toString().split(" ").take(2).joinToString(" ")
            } else {
                binding.textViewTitle.text = title
            }
        }
        Glide.with(this)
            .load(imageUrl)
            .into(binding.imageViewWaste)
        binding.textViewDesc.text = desc
        binding.textViewRVTitle.text = "Contoh ${title}"
        binding.textViewCaraMengelola.text = howToManage
    }

    @SuppressLint("ServiceCast", "ObsoleteSdkInt")
    fun isConnectionOk(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    private fun setLoading(p1: Boolean) {
        val animation = AnimationUtils.loadAnimation(this@EncyclopediaWasteTypeActivity, R.anim.fade_in_fast)
        binding.apply {
            if (p1) {
                progressBar.visibility = View.VISIBLE
                imageViewWaste.visibility = View.GONE
                textViewDesc.visibility = View.GONE
                textViewCM.visibility = View.GONE
                textViewCaraMengelola.visibility = View.GONE
                textViewRVTitle.visibility = View.GONE
                rvWasteExample.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                imageViewWaste.visibility = View.VISIBLE
                textViewDesc.visibility = View.VISIBLE
                textViewCM.visibility = View.VISIBLE
                textViewCaraMengelola.visibility = View.VISIBLE
                textViewRVTitle.visibility = View.VISIBLE
                rvWasteExample.visibility = View.VISIBLE

                imageViewWaste.startAnimation(animation)
                textViewDesc.startAnimation(animation)
                textViewCM.startAnimation(animation)
                textViewCaraMengelola.startAnimation(animation)
                textViewRVTitle.startAnimation(animation)
                rvWasteExample.startAnimation(animation)
            }
        }
    }
}