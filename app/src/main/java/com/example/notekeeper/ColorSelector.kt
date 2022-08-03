package com.example.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

class ColorSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defRes: Int = 0) : LinearLayout(context, attrs, defStyle, defRes) {

    private var listOfColors = listOf(Color.BLUE, Color.RED, Color.GREEN)
    private var selectedColorIndex = 0

    init {
        orientation = LinearLayout.HORIZONTAL

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.color_selector, this)

        val selectedColor = findViewById<View>(R.id.selectedColor)
        val colorSelectorLeftArrow = findViewById<ImageView>(R.id.colorSelectorLeftArrow)
        val colorSelectorRightArrow = findViewById<ImageView>(R.id.colorSelectorRightArrow)

        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        colorSelectorLeftArrow.setOnClickListener {
            selectPrevColor()
        }
        colorSelectorRightArrow.setOnClickListener {
            selectNextColor()
        }
    }

    private fun selectNextColor() {
        if(selectedColorIndex == listOfColors.lastIndex) {
            selectedColorIndex = 0
        }
        else {
            selectedColorIndex++
        }
        findViewById<View>(R.id.selectedColor).setBackgroundColor(listOfColors[selectedColorIndex])
    }

    private fun selectPrevColor() {
        if(selectedColorIndex == 0) {
            selectedColorIndex = listOfColors.lastIndex
        }
        else {
            selectedColorIndex--
        }
        findViewById<View>(R.id.selectedColor).setBackgroundColor(listOfColors[selectedColorIndex])
    }
}
