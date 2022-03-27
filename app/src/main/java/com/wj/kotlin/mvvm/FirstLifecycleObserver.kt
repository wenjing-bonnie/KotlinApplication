package com.wj.kotlin.mvvm

import androidx.lifecycle.*

/**
 * create by wenjing.liu at 2022/3/23
 * 生命周期的监听
 */
class FirstLifecycleObserver : DefaultLifecycleObserver, LifecycleEventObserver {

    //@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    // @OnLifecycleState(Lifecycle.State.CREATED)
    fun test() {

    }

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

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        println("state = ${source.lifecycle.currentState} , event = ${event.name}")
    }
}