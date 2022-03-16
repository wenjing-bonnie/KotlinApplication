package com.wj.kotlin

/**
 * created by wenjing.liu at 2022/3/16
 *
 * 扩展函数：在一些开源框架中动态增加函数
 * fun 类名.方法名的方式为类动态增加功能。
 * 若重复定义里面已有的方法，则覆盖，并且优先使用。
 * 如果自己添加两个相同的方法，则编译不通过。
 * 对超类Any进行扩展，那所有的类都会增加相应功能，要慎用
 */
class KotlinExtension(val name: String, val age: Int) {
}

//增加扩展函数
fun KotlinExtension.show() {
    println("name $name, age ${age}")
}

//对可空类型的扩展函数
fun KotlinExtension?.show1() {
    this ?: println("1222")
    println("name $name, age ${this?.age}")
}


fun StringBuffer.length(): Int {
    println(this)
    return 3
}

val String.name: String
    get() = "123"

/**
 * 任何一种类型都属于泛型，包括方法。
 * TODO 如果对String中也增加show()，发现并没有被覆盖。这个是为什么？
 */
fun <T> T.show() = println("${if (this is String) "是String" else " 不是String"}")


/**
 * 谁.这个this就是谁。不知道为什么这里的输出并不是T的show
 */
fun String.show() = println(this)

fun main() {
    KotlinExtension("zhangsan", 78).show()
    "liu".show()
    val buffer = StringBuffer()
    buffer.append("12321");
    buffer.length()

    "liu".show()
    buffer.show()

    println("liu".name)
}