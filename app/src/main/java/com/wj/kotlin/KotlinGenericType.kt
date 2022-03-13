package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/13
 * 泛型
 */
class Generic<T>(val obj: T, val isObj: Boolean) {
    fun show(obj: T) {
        println("outputs: $obj")
    }

    fun getGenericObj() = obj.takeIf {
        isObj
    }
}

fun <B> show(item: B) {
    item?.also {

    }
}

/**
 * 模仿Rxjava
 */
class RxJava<T>(val isMap: Boolean, var input: T) {
    /**
     * T是要变换的类型,R是变换后的类型
     */
    fun <R> map(mapAction: (T) -> R) {

    }
}

fun main() {
    println(Generic("123", true).getGenericObj())
}
