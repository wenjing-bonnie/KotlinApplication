package com.wj.kotlin

/**
 * created by wenjing.liu at 2022/3/16
 * 内置函数的源码解读
 */
fun main() {

    var name: String? = "1213"
    name = null
    name?.mLet {
        println("$it")
    }?: oo()//println("is null")
}

fun oo(){
    println("具名函数")
}

/**
 * let 代替 if来进行判空
 * 持有it
 * 返回最后一行代码
 */
private inline fun <I, O> I.mLet(lambda: (I) -> O) = lambda(this)