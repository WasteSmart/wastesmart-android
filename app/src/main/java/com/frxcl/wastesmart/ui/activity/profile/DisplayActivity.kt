package com.frxcl.wastesmart.ui.activity.profile

import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityDisplayBinding
import com.frxcl.wastesmart.ui.activity.start.StartActivity
import com.frxcl.wastesmart.util.SettingPreferences
import com.frxcl.wastesmart.util.dataStore
import com.frxcl.wastesmart.viewmodel.SettingViewModel
import com.frxcl.wastesmart.viewmodel.SettingViewModelFactory

class DisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayBinding
    private lateinit var viewModel: SettingViewModel
    private val startActivity = StartActivity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pref = SettingPreferences.getInstance(this.dataStore)
        viewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        getThemeSettingToDataStore()

        binding.apply {
            backBtn.setOnClickListener{onBackPressed()}
        }

        binding.radioGroup.setOnCheckedChangeListener{ group, checkedId ->
            val selectedRadioButton: RadioButton = findViewById(checkedId)
            val selectedIndex = group.indexOfChild(selectedRadioButton)
            viewModel.saveThemeSetting(selectedIndex)
        }
    }

    private fun getThemeSettingToDataStore() {
        viewModel.getThemeSetting().observe(this) { state : Int? ->
            when (state) {
                0 -> {
                    startActivity.setAppTheme(0)
                    binding.useDeviceMode.isChecked = true
                }
                1 -> {
                    startActivity.setAppTheme(1)
                    binding.lightMode.isChecked = true
                }
                2 -> {
                    startActivity.setAppTheme(2)
                    binding.darkMode.isChecked = true
                }
            }
        }
    }
}