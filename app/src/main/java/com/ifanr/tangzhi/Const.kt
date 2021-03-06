package com.ifanr.tangzhi

import androidx.paging.PagedList
import com.ifanr.tangzhi.ext.toColorInt

object Const {

    const val baasId = "89edf6b8cac513a6d140"
    val DEFAULT_PRODUCT_THEME = "#383943".toColorInt()
    const val PRE_FETCH_DISTANCE = 5

    // 糖纸的微信 app id
    const val wechatAppId = "wx0479d25aff361645"

    // 糖纸小程序 user name
    const val miniProgramTangzhiUserName = "gh_ce46f0d4793b"

    // 糖纸小程序，产品页路径
    const val miniProgramProductPath =
        "packages/product/pages/product-hardware-detail/product-hardware-detail?id="

    // 糖纸小程序，点评页面的路径
    const val miniProgramReviewPath =
        "packages/product/pages/product-review-detail/product-review-detail?id="

    const val PAGE_SIZE = 15
    const val WEIBO_APP_KEY = "677122627"
    val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setPageSize(PAGE_SIZE)
        .setMaxSize(Int.MAX_VALUE)
        .setPrefetchDistance(PRE_FETCH_DISTANCE)
        .build()

    const val privacyPolicyUri = "https://www.ifanr.com/privacypolicy"
    const val userAgreementUri = "https://www.ifanr.com/privacypolicy"

    const val aboutTangZhi =
        "https://cloud-minapp-16269.myxiaoapp.com/h5/tangzhi-introduction-v2/index.0ab8abfd.html"
    const val feedback = "https://support.qq.com/product/123451"

    const val tag = "tangzhi"
}