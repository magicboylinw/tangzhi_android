package com.ifanr.tangzhi.ui.product.comments.review

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter

import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.observeToastLiveData
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelFragment
import com.ifanr.tangzhi.ui.base.viewModelOf
import com.ifanr.tangzhi.ui.product.ProductViewModel
import com.ifanr.tangzhi.ui.product.comments.review.widget.ReviewList
import com.ifanr.tangzhi.ui.product.comments.review.widget.TagCard
import com.ifanr.tangzhi.ui.product.indexes.IndexesDialogFragment
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import com.ifanr.tangzhi.ui.widgets.ProductTag
import com.ifanr.tangzhi.ui.widgets.dismissLoading
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import com.ifanr.tangzhi.util.LoadingState
import kotlinx.android.synthetic.main.review_fragment.*

/**
 * 点评
 */
class ReviewFragment : BaseViewModelFragment() {

    companion object {
        private const val TAG = "ReviewFragment"
        const val SEND_REVIEW = 34
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.review_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val vm: ReviewViewModel = viewModelOf(requireActivity())

        // 产品说明书 activity 与 点评 fragment 之间通过在 activity 的 ViewModel 传递数据
        val productViewModel =
            ViewModelProviders.of(requireActivity())[ProductViewModel::class.java]
        productViewModel.product.observe(this, Observer { vm.product.value = it })

        vm.product.observe(this, Observer { it?.also { product ->
            // 模范指数
            reviewIndexesCard.set(
                product.rating,
                product.reviewCount.toInt(),
                product.orgRating.size
            )
            reviewIndexesCard.setOnClickListener {
                IndexesDialogFragment.show(product, childFragmentManager)
            }

            // 写点评
            sendReviewBtn.setOnClickListener { v ->
                ARouter.getInstance().build(Routes.sendReview)
                    .withString(Routes.sendReviewProductId, it.id)
                    .withString(Routes.sendReviewProductName, it.name)
                    .navigation(requireActivity(), SEND_REVIEW)
            }
        }})

        // 大家说标签
        vm.tags.observe(this, Observer { it?.map { ProductTag(it) }?.also {
            tagCard.setData(it)
        }})

        tagCard.listener = object: TagCard.Listener {

            // 打开大家说 dialog
            override fun openTagDialog() {
                ProductTagDialogFragment().show(childFragmentManager, null)
            }

            // 点击标签
            override fun onTagClick(position: Int) {
                vm.onTagClick(position)
            }
        }

        // 点评列表
        vm.reviews.observe(this, Observer {
            if (it != null)
                reviewList.setData(it.first, it.second)
        })

        vm.refreshToLatest.observe(this, Observer {
            reviewSwitch.forceValue(CommentSwitch.Type.LATEST)
        })

        observeToastLiveData(vm.toast)
        observeLoadingLiveData(vm.loading)

        reviewSwitch.onValueChanged = { vm.orderBy.value = it }
        reviewSwitch.setValue(CommentSwitch.Type.LATEST)
        reviewList.setLoadMoreListener { vm.tryLoadNextPage() }
        reviewList.setListener(object: ReviewList.Listener {

            // 点击回复按钮
            override fun onReplyClick(position: Int) {
                onClick(position)
            }

            // 点击点评卡片
            override fun onClick(position: Int) {
                val product = vm.product.value
                val review = vm.reviews.value?.first?.getOrNull(position)
                if (product != null && review != null) {
                    ARouter.getInstance().build(Routes.comment)
                        .withString(Routes.commentProductId, product.id)
                        .withString(Routes.commentProductName, product.name)
                        .withString(Routes.commentReviewId, review.id)
                        .withLong(Routes.commentReviewCreatedBy, review.createdById)
                        .navigation(context)
                }
            }

            // 点击「有用」
            override fun onVoteClick(position: Int) {
                vm.onVoteClick(position)
            }
        })
    }

}
