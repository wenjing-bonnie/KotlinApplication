package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/17
 */
@JvmField
var nameField: String = "zhangsan"

@JvmOverloads
fun show(name: String, age: Int = 20, sex: Char = 'M') {
    println("name is $name , age is ${age} , sex is ${sex}")
}

/**
 * 单独的class类
 */
class InnerClass {
    companion object {
        @JvmField
        val name: String = "xiaomi"

        @JvmStatic
        fun show() {
            println("name is $name")
        }
    }
}

fun main() {
    InnerClass.name
    InnerClass.show()
}