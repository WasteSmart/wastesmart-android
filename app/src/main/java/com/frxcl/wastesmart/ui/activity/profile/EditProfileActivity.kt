package com.frxcl.wastesmart.ui.activity.profile

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityEditProfileBinding
import com.frxcl.wastesmart.ui.activity.home.MainActivity
import com.frxcl.wastesmart.util.SettingPreferences
import com.frxcl.wastesmart.util.dataStore
import com.frxcl.wastesmart.viewmodel.SettingViewModel
import com.frxcl.wastesmart.viewmodel.SettingViewModelFactory
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var fixUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pref = SettingPreferences.getInstance(this.dataStore)
        val viewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        val moveToHome = Intent(this, MainActivity::class.java)

        loadUserPfp()

        binding.apply {
            backBtn.setOnClickListener{
                onBackPressed()
            }
            usernameEditText.setText(intent.getStringExtra("name").toString())
            usernameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            textViewChange.setOnClickListener{startGallery()}
            doneBtn.setOnClickListener {
                fixUri?.let { saveImageToDatastore(it) }
                viewModel.saveUserName(usernameEditText.text.toString())
                startActivity(moveToHome)
                finish()
            }
        }
    }

    private fun loadUserPfp() {
        val fileName = "user_pfp.png"
        val directory = getDir("images", Context.MODE_PRIVATE)
        val file = File(directory, fileName)

        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            Glide.with(this)
                .load(bitmap)
                .circleCrop()
                .into(binding.imageViewPfp)
        }
    }

    private fun startCrop(uri: Uri) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        val destinationUri = Uri.fromFile(File(cacheDir, "image${dateFormat.format(date)}"))
        uri.let {
            UCrop.of(it, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(800, 800)
                .withOptions(UCrop.Options().apply {
                    setToolbarColor(ContextCompat.getColor(this@EditProfileActivity, R.color.colorPrimary))
                    setStatusBarColor(
                        ContextCompat.getColor(this@EditProfileActivity,
                            R.color.colorPrimary
                        ))
                    setActiveControlsWidgetColor(
                        ContextCompat.getColor(this@EditProfileActivity,
                            R.color.colorSecondary
                        ))
                })
                .start( this)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val currentImageUri = result.data?.data as Uri
            startCrop(currentImageUri)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            binding.imageViewPfp.setImageResource(android.R.color.transparent)
            resultUri?.let { uri ->
                fixUri = uri
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.imageViewPfp)
                setMyButtonEnable()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Toast.makeText(this,
                "Gagal $cropError",
                Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun saveImageToDatastore(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val fileName = "user_pfp.png"
        val fileOutputStream: FileOutputStream

        try {
            val directory = getDir("images", Context.MODE_PRIVATE)
            val file = File(directory, fileName)

            if (!file.exists()) {
                file.createNewFile()
            }

            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
            println("simpan")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal menyimpan gambar: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            inputStream?.close()
        }
    }

    private fun setMyButtonEnable() {
        val result = binding.usernameEditText.text
        binding.doneBtn.isEnabled = result != null && result.toString().isNotEmpty()
    }
}