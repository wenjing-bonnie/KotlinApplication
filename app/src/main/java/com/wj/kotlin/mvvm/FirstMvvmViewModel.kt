package com.wj.kotlin.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * create by wenjing.liu at 2022/3/23
 */
class FirstMvvmViewModel : ViewModel() {
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    override fun onCleared() {
        super.onCleared()
        println(" ==  onCleared  ==")
    }
}