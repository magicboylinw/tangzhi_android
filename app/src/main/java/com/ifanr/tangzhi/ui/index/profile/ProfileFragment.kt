package com.ifanr.tangzhi.ui.index.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.Const

import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.avatar
import com.ifanr.tangzhi.ext.default
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelFragment
import com.ifanr.tangzhi.ui.base.viewModelOf
import com.minapp.android.sdk.auth.Auth
import kotlinx.android.synthetic.main.profile_fragment.*

/**
 * 我的
 */
class ProfileFragment : BaseViewModelFragment() {

    companion object {
        private const val TAG = "ProfileFragment"

    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModelOf()

        viewModel.profile.observe(this, Observer {
            profileUserName.text = it?.displayName ?: getString(R.string.profile_name_default)
            profileUserMotto.text =
                if (!it?.motto.isNullOrEmpty()) it.motto
                else getString(R.string.profile_motto_default)
            Glide.with(this).avatar().load(it?.displayAvatar).into(profileAvatar)
            profilePointText.setPoint(it?.point?.toLong() ?: -1)
        })

        viewModel.banner.observe(this, Observer {
            Glide.with(this).default().load(it).into(profileBanner)
        })

        listOf(profileCard, profileAvatar).forEach {
            it.setOnClickListener {
                ARouter.getInstance().build(Routes.updateProfile).navigation(requireActivity())
            }
        }

        profilePointText.setOnClickListener {
            ARouter.getInstance().build(Routes.points).navigation(requireActivity())
        }

        // 关于
        profileAbout.setOnClickListener {
            ARouter.getInstance().build(Routes.browser)
                .withString(Routes.browserUrl, Const.aboutTangZhi)
                .withString(Routes.browserTitle, getString(R.string.profile_about))
                .navigation(requireActivity())
        }

        // 反馈
        profileFeedback.setOnClickListener {
            ARouter.getInstance().build(Routes.browser)
                .withString(Routes.browserUrl, Const.feedback)
                .withString(Routes.browserTitle, getString(R.string.profile_feedback))
                .navigation(requireActivity())
        }

        // 我的消息
        profileMessage.setOnClickListener {
            ARouter.getInstance().build(Routes.message).navigation(requireActivity())
        }

        // 我的动态
        profileActivities.setOnClickListener {
            ARouter.getInstance().build(Routes.timeline).navigation(requireActivity())
        }

        // 我的关注
        profileFollow.setOnClickListener {
            ARouter.getInstance().build(Routes.follows).navigation(requireActivity())
        }

        profileAvatar.setOnLongClickListener {
            viewModel.logUserId()
            false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.syncLoginState()
    }
}
