package com.ifanr.tangzhi.di

import android.content.ClipboardManager
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.EventBus
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppProviderModule {

    companion object {
        private const val TAG = "AppProviderModule"
    }

    @Provides
    @Singleton
    fun provideInputMethodManager(ctx: Context): InputMethodManager =
        ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    @Provides
    @Singleton
    fun provideWechatApi(ctx: Context): IWXAPI {
        Log.d(TAG, "provideWechatApi")
        return WXAPIFactory.createWXAPI(ctx, Const.wechatAppId, false).apply {
            registerApp(Const.wechatAppId)
        }
    }

    @Provides
    @Singleton
    fun provideTelephonyManager(ctx: Context): TelephonyManager =
        ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @Provides
    @Singleton
    fun provideAssetManager(ctx: Context) = ctx.assets

    @Provides
    @Singleton
    fun provideEventBus(): EventBus = EventBus()

    @Provides
    @Singleton
    fun provideClipboardManager(ctx: Context) =
        ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
}