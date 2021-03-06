package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ext.setPadding

class ShareButton: ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    init {
        setPadding(intArrayOf(18, 6, 18, 6))
        inflateInto(R.layout.share_button)
        setBackgroundResource(R.drawable.share_button_bg)
    }
}