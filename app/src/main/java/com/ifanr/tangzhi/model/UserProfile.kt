package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.ext.getSafeBoolean
import com.ifanr.tangzhi.ext.getSafeInt
import com.ifanr.tangzhi.ext.getSafeLong
import com.ifanr.tangzhi.ext.getSafeString
import com.minapp.android.sdk.user.User

data class UserProfile(

    // 是否是黑名单用户
    var blocked: Boolean = false,

    // 是否阻止发送模版消息
    var tmsgBlocked: Boolean = false,

    // 职业
    var profession: String = "",

    // 用户等级，如：1 代表第一级用户，是默认最低等级，目前最高级别为 3
    var level: Int = 1,

    // 用户展示的名字，用户设置了此字段，则优先展示此字段，不展示 nickname
    var displayName: String = "",

    // 用户展示的头像，用户设置了此字段，则优先展示此字段，不展示 avatar
    var displayAvatar: String = "",

    // 知晓云默认的用户联系电话属性，来源于两处： 1. 预约时收集 2. 填写调查问卷时收集
    var phone: String = "",

    // 个人签名
    var motto: String = "",

    // 个人主页背景图
    var banner: String = "",

    // 用户积分
    var point: Int = 0,

    // 今日获得的积分
    var todayPoint: Int = 0,

    // 收藏数量
    var favoriteCount: Int = 0,

    // 关注数量
    var followCount: Int = 0,

    // 足迹数
    var traceCount: Int = 0
) {


    constructor(user: User): this (
        blocked = user.getSafeBoolean(COL_BLOCKED),
        tmsgBlocked = user.getSafeBoolean(COL_TMSG_BLOCKED),
        profession = user.getSafeString(COL_PROFESSION),
        level = user.getSafeInt(COL_LEVEL),
        displayName = if (user.getSafeString(COL_DISPLAY_NAME).isEmpty())
            (user.nickname ?: "") else user.getSafeString(COL_DISPLAY_NAME),
        displayAvatar = if (user.getSafeString(COL_DISPLAY_AVATAR).isEmpty())
            (user.avatar ?: "") else user.getSafeString(COL_DISPLAY_AVATAR),
        phone = user.getSafeString(COL_PHONE),
        motto = user.getSafeString(COL_MOTTO),
        banner = user.getSafeString(COL_BANNER),
        point = user.getSafeInt(COL_POINT),
        todayPoint = user.getSafeInt(COL_TODAY_POINT),
        favoriteCount = user.getSafeInt(COL_FAVORITE_COUNT),
        followCount = user.getSafeInt(COL_FOLLOW_COUNT),
        traceCount = user.getSafeInt(COL_TRACE_COUNT)
    )

    companion object {
        const val COL_BLOCKED        = "blocked"
        const val COL_TMSG_BLOCKED   = "tmsg_blocked"
        const val COL_PROFESSION     = "profession"
        const val COL_LEVEL          = "level"
        const val COL_DISPLAY_NAME   = "display_name"
        const val COL_DISPLAY_AVATAR = "display_avatar"
        const val COL_PHONE          = "_phone"
        const val COL_MOTTO          = "motto"
        const val COL_BANNER         = "banner"
        const val COL_POINT          = "point"
        const val COL_TODAY_POINT    = "today_point"
        const val COL_FAVORITE_COUNT = "favorite_count"
        const val COL_FOLLOW_COUNT   = "follow_count"
        const val COL_TRACE_COUNT    = "trace_count"
    }
}