package com.ifanr.tangzhi.ext

import android.graphics.Color
import androidx.annotation.ColorInt
import com.ifanr.tangzhi.Const

/**
 * 对于 "#456744"（"#rrggbb"）
 * 返回 color int
 */
@ColorInt fun String.toColorInt(): Int {
    return Color.rgb(substring(1, 3).toInt(16),
        substring(3, 5).toInt(16),
        substring(5, 7).toInt(16))
}

@ColorInt fun String.toSafeColorInt(): Int {
    return runCatching { Color.parseColor(this.trim()) }.getOrDefault(Const.DEFAULT_PRODUCT_THEME)
}

@Throws(IllegalArgumentException::class)
fun String.assertNotEmpty(name: String) {
    if (isEmpty())
        throw IllegalArgumentException("$name can't be empty")
}