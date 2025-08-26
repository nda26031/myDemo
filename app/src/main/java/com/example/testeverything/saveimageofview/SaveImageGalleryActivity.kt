package com.example.testeverything.saveimageofview

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testeverything.R
import com.example.testeverything.databinding.ActivitySaveImageGalleryBinding
import java.io.File
import java.io.OutputStream
import androidx.core.graphics.createBitmap

class SaveImageGalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaveImageGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySaveImageGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        binding.btnSave.setOnClickListener {
            val bitmap = getImageOfView(binding.ivPreview)
            if (bitmap != null) {
                saveImageToGallery(bitmap)
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageToGallery(
        bitmap: Bitmap,
    ) {
        val imageName = "Tét_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { contentResolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri? = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                fos = imageUri?.let {
                    contentResolver.openOutputStream(it)
                }
            }
        } else {
            val imageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imageDir, imageName)
            fos = image.outputStream()
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Saved to Photos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageOfView(view: ImageView): Bitmap? {
        var image: Bitmap? = null
        try {
            image = createBitmap(view.measuredWidth, view.measuredHeight)
            val canvas = Canvas(image)

            view.draw(canvas)

        } catch (e: Exception) {
            Log.d("SaveImageGalleryActivity", "Error capturing image: ${e.message}")
        }
        return image
    }
}


