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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.data.WasteCategoryData
import com.frxcl.wastesmart.data.remote.response.Waste
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

        if (isConnectionOk(this)) {
            getData()
        } else {
            Toast.makeText(
                this,
                "Periksa koneksi internet anda.", Toast.LENGTH_LONG
            ).show()
        }
    }

    fun getData() {
        val factory: EncyclopediaViewModelFactory = EncyclopediaViewModelFactory.getInstance(this)
        val viewModel: EncyclopediaViewModel by viewModels {
            factory
        }

        setLoading(true)

        viewModel.getEncyclopedia()
        viewModel.encData.observe(this, Observer { result ->
            setLoading(false)
            binding.apply {
                if (result != null) {
                    Glide.with(this@EncyclopediaActivity)
                        .load(result.waste?.imageUrl!!)
                        .into(imageViewWaste)
                    textViewDesc.text = result.waste.generalDescription
                }
            }
        })

        binding.backBtn.setOnClickListener{onBackPressed()}

        val gridItems = listOf(
            WasteCategoryData(1, R.drawable.sample_organic_icon, "Organik"),
            WasteCategoryData(2, R.drawable.sample_nonorganic_icon, "Anorganik"),
            WasteCategoryData(3, R.drawable.sample_b3_icon, "B3"),
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

    fun setLoading(p1: Boolean) {
        val animation = AnimationUtils.loadAnimation(this@EncyclopediaActivity, R.anim.fade_in_fast)
        binding.apply {
            if (p1) {
                progressBar.visibility = View.VISIBLE
                imageViewWaste.visibility = View.GONE
                textViewDesc.visibility = View.GONE
                textViewCat.visibility = View.GONE
                rvWasteCategory.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                imageViewWaste.visibility = View.VISIBLE
                textViewDesc.visibility = View.VISIBLE
                textViewCat.visibility = View.VISIBLE
                rvWasteCategory.visibility = View.VISIBLE

                imageViewWaste.startAnimation(animation)
                textViewDesc.startAnimation(animation)
                textViewCat.startAnimation(animation)
                rvWasteCategory.startAnimation(animation)
            }
        }
    }
}