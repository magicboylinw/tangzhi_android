package com.ifanr.tangzhi.ui.base.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView

/**
 * 把 [EpoxyRecyclerView] 当作垂直的 LinearLayout 使用
 * 这里做一些优化
 */
abstract class FlatVerticalEpoxyRV: AppEpoxyRV {

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        isNestedScrollingEnabled = false
    }

    override fun createLayoutManager(): LayoutManager {
        return VerticalLayoutManager(
            context
        )
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return false
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return false
    }

    class VerticalLayoutManager: LinearLayoutManager {

        constructor(context: Context?) : super(context)
        constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
        )

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes)

        override fun canScrollVertically(): Boolean {
            return false
        }

        override fun canScrollHorizontally(): Boolean {
            return false
        }
    }
}