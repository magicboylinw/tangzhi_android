package com.ifanr.tangzhi.ui.comment.widget.model

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.comment_all_title)
abstract class CommentAllTitleModel: EpoxyModelWithHolder<CommentAllTitleModel.Holder>() {

    class Holder: KotlinEpoxyHolder()
}