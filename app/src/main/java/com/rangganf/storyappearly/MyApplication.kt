package com.rangganf.storyappearly

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory

class MyApplication : Application(), ImageLoaderFactory {

    // Metode untuk membuat instance baru dari ImageLoader
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)  // Mengaktifkan efek crossfade pada gambar
            .build()
    }
}
