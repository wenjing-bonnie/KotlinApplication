package com.wj.kotlin

/**
 * created by wenjing.liu at 2022/3/16
 * 内置函数的源码解读
 *
 * (T)->Unit:
 * 在函数体内调用时默认it指代参数
 * T.()->Unit：给T定义一个扩展函数，该函数没有形参,没有返回值，当然也可以增加参数和返回值。
 * 在函数体可以直接使用this来访问T对象
 * T.()为T的扩展函数，所以可以在函数体里直接访问T对象的属性或者成员函数。
 * (在理解这个：相当于为T添加了一个()扩展函数，因为扩展函数可以直接访问T的属性和成员变量)
 * ()->Unit：定义一个普通函数，没有参数，没有返回值。
 * 和T没有任何关系就是一个普通的没有参数的函数,在函数体内只能通过外部的T的变量来访问对象。
 */
fun main() {

    var name: String? = "1213"
    //name = null
    name?.mLet {
        println("$it")
    } ?: oo()//println("is null")
    name.apply { }
    name?.mApply {
        println(substring(0))
    } ?: println("the name is null")
    name.also {

    }
    name.run {

    }
}

fun oo() {
    println("具名函数")
}

/**
 * let 代替 if来进行判空
 * 持有it
 * 返回最后一行代码
 */
private inline fun <I, O> I.mLet(lambda: (I) -> O) = lambda(this)

/**
 * 持有this，返回的对象本身
 */
private inline fun <I> I.mApply(lambda: I.() -> Unit): I {
    //lambda(this) //默认就有this，所以可以直接不添加this
    lambda()
    return this
}

/**
 * 实现also:持有it，返回对象本身
 */
private inline fun <I> I.mAlso(lambda: (I) -> Unit): I {
    lambda(this)
    return this
}

/**
 * 持有this,返回的是最后一行代码
 */
private inline fun <I, O> I.mRun(lambda: I.() -> O) = lambda()