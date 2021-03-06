package com.ifanr.tangzhi.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.SoundEffectConstants
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.Dimension
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ui.icons.IconCompoundButton

/**
 * 赞同👍，反对👇
 * 设置 textSize 即可
 */
class JudgementView: IconCompoundButton {

    companion object {
        private const val STYLE_APPROVE = 0
        private const val STYLE_OPPOSE = 1

        @Dimension(unit = Dimension.DP)
        private const val PADDING = 8
    }

    private var onClick: OnClickListener? = null

    constructor(context: Context?) : super(context) { init(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs) }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) { init(attrs) }

    private fun init(attrs: AttributeSet?) {
        var style = STYLE_APPROVE

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.JudgementView)
            style = ta.getInt(R.styleable.JudgementView_judgementViewStyle, STYLE_APPROVE)
            ta.recycle()
        }

        setText(when (style) {
            STYLE_APPROVE -> R.string.ic_like_btn
            else -> R.string.ic_unlike_btn
        })
        setBackgroundResource(R.drawable.bg_judgement_view)
        setTextColor(Color.WHITE)
        context.dp2px(PADDING).also {
            setPadding(it, it, it, it)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        onClick = l
    }

    /**
     * parent 在这里会 toggle state，而我不需要 toggle
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun performClick(): Boolean {
        onClick?.onClick(this)
        playSoundEffect(SoundEffectConstants.CLICK)
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED)
        return true
    }
}