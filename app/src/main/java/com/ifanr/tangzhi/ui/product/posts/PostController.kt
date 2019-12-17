package com.ifanr.tangzhi.ui.product.posts

import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyViewHolder
import com.airbnb.epoxy.OnModelClickListener
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.BaseTyped2Controller
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderView
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderViewModel_
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

class PostController (
    val onHeaderClickListener: OnModelClickListener<SectionHeaderViewModel_, SectionHeaderView>
): BaseTyped2Controller<List<Product.CachedPost>, Int>() {

    @AutoModel
    lateinit var header: SectionHeaderViewModel_

    override fun buildModels(posts: List<Product.CachedPost>?, total: Int?) {
        if (posts?.isNotEmpty() == true) {
            with(header) {
                listener(onHeaderClickListener)
                title(R.string.product_posts)
                count(R.string.product_posts_total, total ?: 0)
                addTo(this@PostController)
            }
        }

        posts?.forEach {
            post {
                data(it)
                id(it.id)
            }
        }
    }

    override fun setData(posts: List<Product.CachedPost>, total: Int) {
        super.setData(posts, total)
    }

}