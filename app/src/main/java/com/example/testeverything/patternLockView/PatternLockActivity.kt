package com.example.testeverything.patternLockView

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.overrideOf
import com.example.testeverything.R
import com.example.testeverything.databinding.ActivityPatternLockBinding
import jp.wasabeef.glide.transformations.MaskTransformation


class PatternLockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatternLockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPatternLockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Glide.with(this)
            .load(R.drawable.astronaut)
            .apply(overrideOf(binding.ivPreview.width, binding.ivPreview.height))
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation<Bitmap>(
                        CenterCrop(),
                        MaskTransformation(R.drawable.shape_5)
                    )
                )
            )
            .into(binding.ivPreview)
    }

}