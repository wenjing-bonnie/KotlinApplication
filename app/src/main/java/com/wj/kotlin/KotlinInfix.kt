package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/16
 *
 * 中缀表达式
 * infix必须有几个条件
 * 前提条件：
 * 必须是成员函数或者扩展函数
 * 必须只有一个参数
 * 参数不能是可变参数或者默认参数
 * 使用方式： 对象 方法 内容信息
 *
 * 1.必须对第一个参数进行函数扩展
 * 2.必须传递一个参数
 *
 */

data class FakePair<A, B>(var a: A, var b: B) {

}

infix fun <A, B> A.toPair(b: B): FakePair<A, B> =
    FakePair(this, b)

fun main() {
    mapOf("key1" to "value1")

    val pair: FakePair<String, String> = "aaa" toPair "bbb"
    println(pair.toString())
}

