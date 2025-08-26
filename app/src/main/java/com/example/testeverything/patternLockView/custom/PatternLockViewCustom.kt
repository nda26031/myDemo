package com.example.testeverything.patternLockView.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.testeverything.R

class PatternLockViewCustom : ViewGroup {
    companion object {
        private const val TAG = "PatternLockViewCustom"
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var density: Float = context.resources.displayMetrics.density
    private val paint = Paint()

    private val pointPositions = mutableListOf<PatternPoint>()

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        paint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = measuredWidth
        var height = measuredHeight

        width = resolveSize(width, widthMeasureSpec)
        height = resolveSize(height, heightMeasureSpec)

        setMeasuredDimension(width, height)

        initPatternPoints(width * 0.5f, height * 0.5f, 8.0f * density)
    }

//    private fun initPatternPoints(centerX: Float, centerY: Float, radius: Float) {
//        pointPositions.clear()
//        //row 1
//        var point = PatternPoint(centerX * 0.5f, centerY * 0.5f, radius, "1") //11
//        pointPositions.add(point)
//
//        point = PatternPoint(centerX, centerY * 0.5f, radius, "2") //12
//        pointPositions.add(point)
//
//        point = PatternPoint(centerX * 0.5f + centerX, centerY * 0.5f, radius, "3") //13
//        pointPositions.add(point)
//
//        //row 2
//        point = PatternPoint(centerX * 0.5f, centerY, radius, "4") //21
//        pointPositions.add(point)
//
//        point = PatternPoint(centerX, centerY, radius, "5") //22
//        pointPositions.add(point)
//
//        point = PatternPoint(centerX * 0.5f + centerX, centerY, radius, "6") //23
//        pointPositions.add(point)
//
//        //row 3
//        point = PatternPoint(centerX * 0.5f, centerY * 0.5f + centerY, radius, "7") //31
//        pointPositions.add(point)
//
//        point = PatternPoint(centerX, centerY * 0.5f + centerY, radius, "8") //32
//        pointPositions.add(point)
//
//        point = PatternPoint(centerX * 0.5f + centerX, centerY * 0.5f + centerY, radius, "9") //33
//        pointPositions.add(point)
//    }

    private fun initPatternPoints(centerX: Float, centerY: Float, radius: Float) {
        removeAllViews()
        pointPositions.clear()

        val points = listOf(
            Pair(centerX * 0.5f, centerY * 0.5f),
            Pair(centerX, centerY * 0.5f),
            Pair(centerX * 0.5f + centerX, centerY * 0.5f),
            Pair(centerX * 0.5f, centerY),
            Pair(centerX, centerY),
            Pair(centerX * 0.5f + centerX, centerY),
            Pair(centerX * 0.5f, centerY * 0.5f + centerY),
            Pair(centerX, centerY * 0.5f + centerY),
            Pair(centerX * 0.5f + centerX, centerY * 0.5f + centerY)
        )

        for ((index, pos) in points.withIndex()) {
            val imageView = ImageView(context).apply {
                setImageResource(R.drawable.ic_photo_permission) // icon mặc định
                layoutParams = LayoutParams((radius * 4).toInt(), (radius * 4).toInt())
                x = pos.first - radius * 2
                y = pos.second - radius * 2
            }
            addView(imageView)
            pointPositions.add(PatternPoint(pos.first, pos.second, radius, (index + 1).toString()))
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
//        drawPoint(canvas)
    }

    private fun drawBackground(canvas: Canvas?) {
        canvas?.drawColor(0xFF000000.toInt())
    }

//    private fun drawPoint(canvas: Canvas?) {
//        paint.color = Color.parseColor("#FFFFFF")
//        paint.strokeMiter = 8.0f
//
//        for (point in pointPositions) {
//            paint.style = if (point.isSelected) Paint.Style.FILL else Paint.Style.STROKE
//            canvas?.drawCircle(point.x, point.y, point.r, paint)
//        }
//    }

    override fun onLayout(
        changed: Boolean, l: Int, t: Int, r: Int, b: Int
    ) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.layout(
                child.x.toInt(),
                child.y.toInt(),
                (child.x + child.measuredWidth).toInt(),
                (child.y + child.measuredHeight).toInt()
            )
        }
    }

    data class PatternPoint(val x: Float, val y: Float, val r: Float, val pattern: String) {
        var isSelected: Boolean = false
    }
}