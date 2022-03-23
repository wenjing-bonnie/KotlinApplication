package com.wj.kotlin.mvvm

import androidx.lifecycle.ViewModel

/**
 * create by wenjing.liu at 2022/3/23
 */
class FirstMvvmViewModel : ViewModel() {
    var count = 0

    override fun onCleared() {
        super.onCleared()
        println(" ==  onCleared  ==")
    }
}