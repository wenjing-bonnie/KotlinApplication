package com.wj.kotlin.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
        GlobalScope.launch(Dispatchers.IO) {
            delay(2000)
            currentName.value = "更改名字了"
        }

    }

    override fun onCleared() {
        super.onCleared()
        println(" ==  onCleared  ==")
    }
}