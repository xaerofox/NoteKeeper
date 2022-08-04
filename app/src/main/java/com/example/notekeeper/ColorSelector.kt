package com.example.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout

class ColorSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defRes) {

    private var listOfColors = listOf(Color.BLUE, Color.RED, Color.GREEN)
    private var selectedColorIndex = 0

    private var colorSelectListeners: ArrayList<(Int) -> Unit> = arrayListOf()

    fun addListeners(function: (Int) -> Unit) {
        this.colorSelectListeners.add(function)
    }

    var selectedColorValue: Int = android.R.color.transparent
        set(value) {

            var index = listOfColors.indexOf(value)
            if (index == -1) {
                findViewById<CheckBox>(R.id.colorEnabled).isChecked = false
                index = 0
            } else findViewById<CheckBox>(R.id.colorEnabled).isChecked = true

            selectedColorIndex = index
            findViewById<View>(R.id.selectedColor).setBackgroundColor(listOfColors[selectedColorIndex])
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSelector)
        listOfColors = typedArray.getTextArray(R.styleable.ColorSelector_colors)
            .map {
                Color.parseColor(it.toString())
            }
        typedArray.recycle()

        orientation = LinearLayout.HORIZONTAL

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.color_selector, this)

        val selectedColor = findViewById<View>(R.id.selectedColor)
        val colorSelectorLeftArrow = findViewById<ImageView>(R.id.colorSelectorLeftArrow)
        val colorSelectorRightArrow = findViewById<ImageView>(R.id.colorSelectorRightArrow)
        val colorEnabled = findViewById<CheckBox>(R.id.colorEnabled)

        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        colorSelectorLeftArrow.setOnClickListener {
            selectPrevColor()
        }
        colorSelectorRightArrow.setOnClickListener {
            selectNextColor()
        }
        colorEnabled.setOnCheckedChangeListener { buttonView, isChecked ->
            broadcastColor()
        }
    }

    private fun selectNextColor() {
        if (selectedColorIndex == listOfColors.lastIndex) {
            selectedColorIndex = 0
        } else {
            selectedColorIndex++
        }
        findViewById<View>(R.id.selectedColor).setBackgroundColor(listOfColors[selectedColorIndex])
        val colorEnabled = findViewById<CheckBox>(R.id.colorEnabled)
        broadcastColor()
    }

    private fun selectPrevColor() {
        if (selectedColorIndex == 0) {
            selectedColorIndex = listOfColors.lastIndex
        } else {
            selectedColorIndex--
        }
        findViewById<View>(R.id.selectedColor).setBackgroundColor(listOfColors[selectedColorIndex])
        val colorEnabled = findViewById<CheckBox>(R.id.colorEnabled)
        broadcastColor()
    }

    private fun broadcastColor() {
        val colorEnabled = findViewById<CheckBox>(R.id.colorEnabled)
        val color = if (colorEnabled.isChecked)
            listOfColors[selectedColorIndex]
        else
            Color.TRANSPARENT
        this.colorSelectListeners.forEach { function -> function(color) }
    }
}
