package com.wj.kotlin.mvvm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * create by wenjing.liu at 2022/3/23
 * 生命周期的监听
 */
class FirstLifecycleObserver : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        println(owner.lifecycle.currentState)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        println(owner.lifecycle.currentState)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        println(owner.lifecycle.currentState)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        println(owner.lifecycle.currentState)

    }
}