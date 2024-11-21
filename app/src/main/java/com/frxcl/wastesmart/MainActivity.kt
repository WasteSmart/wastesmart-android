package com.frxcl.wastesmart

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.frxcl.wastesmart.databinding.ActivityMainBinding
import com.frxcl.wastesmart.ui.encyclopedia.EncyclopediaActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, EncyclopediaActivity::class.java)
        val intentD = Intent(this, EncyclopediaDetailActivity::class.java)

        binding.apply {
            constraintLayout3.setOnClickListener{
                startActivity(intent)
            }
            buttonIn.setOnClickListener {
                startActivity(intentD)
            }
        }
    }
}