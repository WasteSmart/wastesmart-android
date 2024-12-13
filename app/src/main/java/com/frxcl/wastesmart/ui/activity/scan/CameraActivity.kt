package com.frxcl.wastesmart.ui.activity.scan

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.databinding.ActivityCameraBinding
import com.frxcl.wastesmart.util.createCustomTempFile

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageCapture: ImageCapture

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var isFlashOn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            backBtn.setOnClickListener { onBackPressed() }
            imageButtonCapture.setOnClickListener { takePhoto() }
            if (checkFlashAvailability()) {
                imageButtonFlash.setOnClickListener{ toggleFlash() }
            } else {
                imageButtonFlash.setImageResource(R.drawable.baseline_flash_disabled_32)
                imageButtonFlash.isEnabled = false
            }
            if (checkCamera()) {
                imageButtonSwitch.setOnClickListener {
                    switchCamera()
                }
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        startCamera()
    }


    private fun startCamera() {
        setLoading(true)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setFlashMode(FLASH_MODE_OFF)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
        setLoading(false)
    }

    private fun takePhoto() {
        setLoading(true)

        when (isFlashOn) {
            1 -> {
                imageCapture.flashMode = FLASH_MODE_AUTO
            }
            2 -> {
                imageCapture.flashMode = FLASH_MODE_ON
            }
            else -> {
                imageCapture.flashMode = FLASH_MODE_OFF
            }
        }

        val imageCapture = imageCapture

        val photoFile = createCustomTempFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent(this@CameraActivity, ScanResultActivity::class.java)
                    intent.putExtra("scanImageUri", output.savedUri)
                    setLoading(false)
                    startActivity(intent)
                    finish()
                }

                override fun onError(exc: ImageCaptureException) {
                    setLoading(false)
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun switchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    private fun toggleFlash () {
        val flashButton = binding.imageButtonFlash
        when (isFlashOn) {
            0 -> {
                imageCapture.flashMode = FLASH_MODE_AUTO
                flashButton.setImageResource(R.drawable.baseline_flash_auto_32)
                isFlashOn = 1
            }
            1 -> {
                imageCapture.flashMode = FLASH_MODE_ON
                flashButton.setImageResource(R.drawable.baseline_flash_on_32)
                isFlashOn = 2
            }
            else -> {
                imageCapture.flashMode = FLASH_MODE_OFF
                flashButton.setImageResource(R.drawable.baseline_flash_off_32)
                isFlashOn = 0
            }
        }
    }

    private fun checkCamera(): Boolean {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIdList = cameraManager.cameraIdList

        var hasFrontCamera = false
        var hasBackCamera = false

        for (cameraId in cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)

            if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                hasFrontCamera = true
            } else if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                hasBackCamera = true
            }

            if (hasFrontCamera && hasBackCamera) {
                return true
            }
        }
        return false
    }

    private fun checkFlashAvailability(): Boolean {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        return try {
            val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                facing == CameraCharacteristics.LENS_FACING_BACK
            }

            cameraId?.let {
                val characteristics = cameraManager.getCameraCharacteristics(it)
                characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            } ?: false
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Camera access exception: ${e.message}", e)
            false
        }
    }

    fun setLoading(p1: Boolean) {
        if (p1) {
            binding.progressBarCamera.visibility = View.VISIBLE
        } else {
            binding.progressBarCamera.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "CameraActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
    }
}