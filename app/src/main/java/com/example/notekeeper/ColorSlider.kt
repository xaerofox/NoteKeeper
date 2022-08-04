package com.example.notekeeper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.core.content.ContextCompat

@SuppressLint("AppCompatCustomView")
class ColorSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.seekBarStyle,
    defStyleRes: Int = 0
) : SeekBar(context, attrs, defStyleAttr, defStyleRes) {
    private var colors: ArrayList<Int> = arrayListOf(Color.RED, Color.YELLOW, Color.BLUE)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSlider)
        try {
            colors = typedArray.getTextArray(R.styleable.ColorSlider_colors)
                .map {
                    Color.parseColor(it.toString())
                } as ArrayList<Int>
        }
        finally {
            typedArray.recycle()
        }

        colors.add(0, android.R.color.transparent)
        max = colors.size - 1
        progressBackgroundTintList =
            ContextCompat.getColorStateList(context, android.R.color.transparent)
        progressTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
        splitTrack = false
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + 50)
        thumb = context.getDrawable(R.drawable.ic_color_slider_thumb)

        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listeners.forEach {
                    it(colors[progress])
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    var selectedColorValue: Int = android.R.color.transparent
    set(value) {
        var index = colors.indexOf(value)
        if(index == -1) {
            progress = 0
        }
        else
            progress = index
    }

    private var listeners: ArrayList<(Int) -> Unit> = arrayListOf()

    fun addListeners(function: (Int) -> Unit) {
        listeners.add(function)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTickMarks(canvas)
    }

    private fun drawTickMarks(canvas: Canvas?) {
        canvas?.let {
            val count = colors.size
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), (height / 2).toFloat() + 50f)
            if (count > 1) {
                val w = 48f
                val h = 48f
                val halfW = if (w >= 0) w / 2f else 1f
                val halfH = if (h >= 0) h / 2f else 1f
                val spacing = (width - paddingLeft - paddingRight) / (count - 1).toFloat()
                for (i in 0 until count) {
                    if (i == 0) {
                        val drawable = context.getDrawable(R.drawable.ic_no_color)
                        val w2 = drawable?.intrinsicWidth ?: 0
                        val h2 = drawable?.intrinsicHeight ?: 0
                        val halfW2 = if (w2 >= 0) w2 / 2 else 1
                        val halfH2 = if (h2 >= 0) h2 / 2 else 1
                        drawable?.setBounds(-halfW2, -halfH2, halfW2, halfH2)
                        drawable?.draw(canvas)
                    }
                    else {
                        val paint = Paint()
                        paint.color = colors[i]
                        canvas.drawRect(-halfW, -halfH, halfW, halfH, paint)
                    }
                    canvas.translate(spacing, 0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }
}
