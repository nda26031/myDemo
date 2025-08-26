package com.example.testeverything.camerax

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageProxy
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testeverything.R
import com.example.testeverything.databinding.ActivityEditPhotoBinding

class EditPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bitmap = BitmapHolder.imageBitmap
        if (bitmap != null) {
            displayCapturedImage(bitmap)
        } else {
            Toast.makeText(this, "Error to retrieve image", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private fun displayCapturedImage(bitmap: Bitmap) {
        val isFrontCamera = intent.getBooleanExtra("IS_FRONT_CAMERA", false)
        Log.d("PreviewImageActivity", "isFrontCamera: $isFrontCamera")
        val displayBitmap = if (isFrontCamera) {
            bitmap.flipHorizontally()
        } else {
            bitmap
        }
        binding.ivPreview.setImageBitmap(displayBitmap)
    }

    fun Bitmap.flipHorizontally(): Bitmap {
        val matrix = Matrix().apply {
            preScale(-1.0f, 1.0f)
        }
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }
}

