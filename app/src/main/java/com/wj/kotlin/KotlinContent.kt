package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/10
 * 用来学习Kotlin
 *
 * 1.定义编译时常量:只能定义在函数外,如果在函数体内,编译不通过.
 * 编译时常量只能是基本的数据类型
 * 编译时常量只能定义在函数之外定义：如果可以在函数内，必须在运行时才能对函数进行赋值.
 * 2.Kotlin只有一种引用数据类型,无基本数据类型,但在编译成字节码的时候,会转换成基本的数据类型
 *
 */
const val TAG = "123"

/**
 * 3.range 表达式：连续的可遍历范围
 */
fun range(number: Int) {
    if (number in 0..10) {
        println("range 0~10")
    } else if (number in 20..30 step 2) {
        println("range 20~30")
    } else if (number in 40 downTo 30 step 2) {
        println("range 40~30")
    }
}

/**
 * 4.when 表达式.类似java的switch
 * 表达式有返回值.若所有的类型一致,则返回的为对应类型;若类型不一致,则返回Any
 *
 */
fun whenExpression(week: Int) {
    val info = when (week) {
        1 -> "111111"
        2 -> "222222"
        else -> {
            //此种情况下返回值为Unit,相当于java的void
            println()
        }
    }

    /**
     * 5.String模板.可以添加表达式
     */
    val stringFormat = "${if (week == 1) "123" else "345"}"
}

/**
 * 6.默认的函数都是public类型
 * 7.可以为函数的输入参数设置默认值
 * 8.在调用的时候,可以不用关心调用顺序,直接通过赋值的方式进行调用,如sum(second = 5,first = 4)
 */
private fun sum(first: Int, second: Int = 4): Int {
    return first + second
}

/**
 * 9.Nothing,并不是注释,而是在运行的时候会中止调用
 * 10.反引号函数名
 * (1)定义一些特殊字符串的函数名:如`1344`
 * (2)若java定义的函数名恰好是kotlin的关键字,则函数名用反引号,如:`in`
 */
private fun todoMethod() {
    TODO("还未实现")
}


fun main() {
    range(34)
    sum(second = 5, first = 4)
}