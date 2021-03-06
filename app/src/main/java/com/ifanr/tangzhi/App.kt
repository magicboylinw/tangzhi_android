package com.ifanr.tangzhi

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import com.airbnb.epoxy.EpoxyController
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.di.DaggerAppComponent
import com.ifanr.tangzhi.glide.RoundedBitmapDrawableTranscoder
import com.jakewharton.threetenabp.AndroidThreeTen
import com.minapp.android.sdk.BaaS
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins

class App: DaggerApplication() {

    companion object {
        private const val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()
        initBaas()
        initARouter()
        initGlide()
        initRxJava()
        initEpoxy()
        AndroidThreeTen.init(this)
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    private fun initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
            ARouter.printStackTrace()
        }
        ARouter.init(this)
    }

    private fun initGlide() {
        val glide = Glide.get(this)
        glide.registry.register(Bitmap::class.java, RoundedBitmapDrawable::class.java,
            RoundedBitmapDrawableTranscoder(this, glide.bitmapPool))
    }

    private fun initRxJava() {
        RxJavaPlugins.initIoScheduler(workerSchedulerCallable)
        RxJavaPlugins.initComputationScheduler(workerSchedulerCallable)
        RxJavaPlugins.setErrorHandler(SimpleErrorHandler())
    }

    private fun initEpoxy() {
        EpoxyController.setGlobalDebugLoggingEnabled(BuildConfig.DEBUG)
        EpoxyController.setGlobalDuplicateFilteringDefault(true)
    }

    private fun initBaas() {
        BaaS.init(Const.baasId, this)
        BaaS.initWechatComponent(Const.wechatAppId, this)
    }
}

private class SimpleErrorHandler: Consumer<Throwable> {
    override fun accept(t: Throwable?) {
        t?.also { Log.e("SimpleErrorHandler", it.message, it) }
    }
}