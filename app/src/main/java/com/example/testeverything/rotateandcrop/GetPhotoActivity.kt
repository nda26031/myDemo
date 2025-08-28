package com.example.testeverything.rotateandcrop

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testeverything.R
import com.example.testeverything.databinding.ActivityGetPhotoBinding
import java.io.File
import java.io.FileOutputStream

class GetPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetPhotoBinding
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        binding.civPhoto.setImageUriAsync(galleryUri)
    }

    private var croppedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGetPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGetPhoto.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.sbRotation.max = 360
        binding.sbRotation.progress = 0
        binding.sbRotation.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                sb: SeekBar?,
                process: Int,
                fromUser: Boolean
            ) {
                binding.civPhoto.rotatedDegrees = process
                    binding.tvRotation.text = "${binding.civPhoto.rotatedDegrees}"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.btnCrop.setOnClickListener {
            croppedBitmap = binding.civPhoto.getCroppedImage()
            croppedBitmap?.let {
                val file = File(cacheDir, "cropped_result.jpg")
                FileOutputStream(file).use { out ->
                    it.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                // Xử lý ảnh đã crop xong (ví dụ: trả về qua Intent)
                binding.ivPreview.setImageBitmap(croppedBitmap)
            }
        }
    }
}