package com.example.testeverything.patternLockView.custom

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.overrideOf
import com.example.testeverything.R
import jp.wasabeef.glide.transformations.MaskTransformation

private const val ANIMATION_DURATION = 200L
private const val ANIMATION_SCALE_VALUE = 1.5f

private var animator: ObjectAnimator? = null

class DotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var key: String? = null
        private set

    init {
//        setImageDrawable(GradientDrawable().apply {
//            layoutParams = LinearLayout.LayoutParams(
//                resources.getDimensionPixelSize(R.dimen.dot_view_size),
//                resources.getDimensionPixelSize(R.dimen.dot_view_size)
//            )
//            setPadding(4)
//            shape = OVAL
//            setColor(Color.DKGRAY)
//        })
//
//        setWillNotDraw(false)

        setDotImageWithMash(R.drawable.astronaut, R.drawable.shape_5)
        layoutParams = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.dot_view_size),
            resources.getDimensionPixelSize(R.dimen.dot_view_size)
        )
        setPadding(4)
        scaleType = ScaleType.CENTER_CROP
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        animator?.cancel()
        animator?.removeAllListeners()
        animator = null
    }

    fun setKey(key: String) {
        this.key = key
    }

    fun setDotViewColor(@ColorInt color: Int) {
        (drawable as GradientDrawable).setColor(color)
    }

    fun setDotImage(url: Any) {
        Glide.with(context)
            .load(url)
            .into(this)
    }

    fun setDotImageWithMash(url: Any, mask: Int) {
        Glide.with(context)
            .load(url)
            .apply(overrideOf(this.width, this.height))
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation(
                        CenterCrop(),
                        MaskTransformation(mask)
                    )
                )
            )
            .into(this)
    }

    fun animateDotView() {
        animator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat("scaleX", ANIMATION_SCALE_VALUE),
            PropertyValuesHolder.ofFloat("scaleY", ANIMATION_SCALE_VALUE)
        ).apply {
            duration = ANIMATION_DURATION
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.RESTART
            start()
        }

        animator?.doOnEnd {
            animator?.removeAllListeners()
            animator = null
        }
    }
}