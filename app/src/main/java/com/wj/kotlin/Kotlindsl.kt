package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/16
 *
 * DSL DSL领域专用语言(Domain Specified Language/DSL)
 * 编程规范：定义输入输出等规则
 * 1.定义了lamdba规则标准，输入必须为Context类，才有资格调用apply6
 * 2.定义了lamdba规则标准，输出始终为Context
 * 代码结构通常是链式调用、lambda 嵌套，并且接近于日常使用的英语句子。体现代码的整洁之道
 */


class Context {
    val info = "1234"
    val name = "abc"
    fun toast(str: String) = println("the toast :$str")

    /**
     * invoke约定：让对象调用函数的语法结构更加简洁。
     */
    operator fun invoke(count: Int) {
        println("invoke $count")
    }
}

/**
 * lamdba: Context.() -> Unit) 这个定义的就是一个函数的规则
 * 方法名为lamdba，输入参数的类型为Context的扩展函数：Context.()，无返回值
 * 对于扩展函数Context.()：这是没有任何输入参数的扩展函数：
 * 在传值的时候，{}就是一个匿名函数，
 *
 * Context.(String)这个是有一个String的输入参数的扩展函数：
 * 在传值的时候，传入的{}为一个匿名函数，只有一个传入参数，那么就会含有一个本身的it代指该扩展函数的一个输入参数
 *
 * Context.(String, String)：那么在调用lamdba()的时候，要传入两个参数，同样在匿名函数{}中会有两个传入参数
 *
 */
inline fun Context.apply5(lamdba: Context.() -> Unit): Context {
    lamdba()
    return this
}

/**
 * 这种方式下的
 */
inline fun Context.apply6(lamdba: Context.(String, String) -> Unit): Context {
    //这两个参数传入到匿名函数{}中，里面有两个输入参数。默认的可以直接调用Context里的方法
    lamdba(info, name)
    return this
}

/**
 * 这种无法直接调用Context里面的方法
 */
inline fun Context.apply7(lamdba: (String, String) -> Unit): Context {
    lamdba(info, name)
    return this
}

fun main() {

    val context = Context()
    context.apply6 { a1: String, a2: String ->
        toast("$a1, $a2")
    }
    context.apply7 { a1: String, a2: String ->
        // it.toast("$a1, $a2")
    }
    //invoke约定
    context(3)

}