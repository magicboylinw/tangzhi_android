package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R

/**
 * 有三层的堆叠图片，eg：糖纸清单
 */
class StackImageView: ConstraintLayout {

    val coverIv: ImageView
    val bgIv: ImageView

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.stack_image_view, this, true)
        coverIv = findViewById(R.id.frontIv)
        bgIv = findViewById(R.id.bgIv)
        bgIv.setImageResource(R.drawable.stack_image_view)
    }
}