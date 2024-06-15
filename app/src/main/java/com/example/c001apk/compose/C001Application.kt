package com.example.c001apk.compose

import android.app.Application
import coil.Coil
import coil.ImageLoader
import dagger.hilt.android.HiltAndroidApp
import me.zhanghai.android.appiconloader.coil.AppIconFetcher
import me.zhanghai.android.appiconloader.coil.AppIconKeyer
import net.mikaelzero.coilimageloader.CoilImageLoader
import net.mikaelzero.mojito.Mojito
import net.mikaelzero.mojito.view.sketch.SketchImageLoadFactory

/**
 * Created by bggRGjQaUbCoE on 2024/5/29
 */
lateinit var c001Application: C001Application

@HiltAndroidApp
class C001Application : Application() {

    override fun onCreate() {
        super.onCreate()

        c001Application = this

        val iconSize = resources.getDimensionPixelSize(android.R.dimen.app_icon_size)
        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .components {
                    add(AppIconKeyer())
                    add(AppIconFetcher.Factory(iconSize, false, this@C001Application))
                }
                .build()
        )

        Mojito.initialize(
            CoilImageLoader.with(this),
            SketchImageLoadFactory()
        )

    }

}