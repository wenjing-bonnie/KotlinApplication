package com.wj.kotlin.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * create by wenjing.liu at 2022/3/23
 */
class FirstMvvmViewModel : ViewModel() {
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun loadUserFromServer() {
        //默认的为主线程
        viewModelScope.launch {
            println("当前线程：${Thread.currentThread().name}")
            delay(2000)
            println("当前线程正在延时2s")
            currentName.value = "更改名字了"
        }

    }

    override fun onCleared() {
        super.onCleared()
        println(" ==  onCleared  ==")
    }
}