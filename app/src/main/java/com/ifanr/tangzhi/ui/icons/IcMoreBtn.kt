package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.util.AttributeSet
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat

/**
 * 选择，三点
 */
class IcMoreBtn: IconView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.ic_more_btn)
        setTextColor(context.getColorCompat(R.color.base_ab))
    }
}