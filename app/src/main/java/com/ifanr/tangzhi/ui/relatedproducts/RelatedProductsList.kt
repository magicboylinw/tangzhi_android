package com.ifanr.tangzhi.ui.relatedproducts

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV

class RelatedProductsList: AppEpoxyRV {

    private val controller = Controller()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setController(controller)
        addItemDecoration(Decoration(context))
    }

    fun setData(data: List<Product>) {
        controller.setData(data)
    }
}

class Decoration(
    private val ctx: Context
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(0, 0, 0, 0)
        val vh = parent.getChildViewHolder(view)
        val vt = vh.itemViewType
        val position = vh.adapterPosition
        val count = parent.adapter?.itemCount ?: 0

        if (vt == R.layout.related_products_item && position < count - 2) {
            outRect.bottom = ctx.dp2px(20)
        }
    }
}