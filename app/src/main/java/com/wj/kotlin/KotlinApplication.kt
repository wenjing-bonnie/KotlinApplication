package com.wj.kotlin

import android.app.Application

/**
 * create by wenjing.liu at 2022/3/24
 */
class KotlinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        println("KotlinApplication")
    }
}