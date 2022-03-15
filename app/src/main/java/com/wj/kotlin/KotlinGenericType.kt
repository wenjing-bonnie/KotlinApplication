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
     * 单表达式方法，当方法仅有单个表达式时，可以省略花括号，并在 = 后指定方法体即可
     */
    fun <R> map(mapAction: (T) -> R) = mapAction(input)

//    fun <R> map(mapAction: (T) -> R): R {
//        return mapAction(input)
//    }

    fun <O, R> map(isMap: Boolean, input2: R, mapAction1: (T, R) -> O) {
        if (isMap) mapAction1(input, input2) else null
    }
}

fun main() {
    println(Generic("123", true).getGenericObj())
    val result = RxJava(true, 123).map {
        println("${it}")
        it.toString()
    }

    val result1 = RxJava(true, "123").map(true, 123) { first, second ->
        println("$first , $second")
    }
}
