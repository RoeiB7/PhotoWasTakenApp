package com.example.photowastakenapp

import android.app.Application

import com.example.photowastakenapp.Utils.ImageUtils
import com.example.photowastakenapp.Utils.SharedPrefUtils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ImageUtils.init(this)
        SharedPrefUtils.init(this)
    }

}

