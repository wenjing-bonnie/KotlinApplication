package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/11
 * 特殊的内置函数:
 * 1.apply
 *   返回值就是对象本身的类型;在匿名函数中无it,持有this,可以直接调用对象的方法和直接将对象传入到{}或具名函数.
 *   方便链式调用
 * 2.let
 *   返回值的类型根据匿名函数的最后一行代码;含有it,就是该对象.
 *   与空合并符进行null逻辑处理
 * 3.run
 *   返回值的类型根据匿名函数的最后一行代码；含有this,可直接调用对象的方法和直接将对象传入到{}或具名函数
 *   可以方便将上一个方法的返回值传递到{}或具名函数中
 * 4.with
 *   与run一样,但是需要将对象主动传入,例如with(info, ::isLong)
 * 5.also
 *   返回值就是对象本身的类型;含有it,就是该对象.
 *   可以链式调用
 * 6.takeIf:如果匿名函数返回值为true,则返回为对象本身;否则返回为null
 *    与空合并符进行null逻辑处理
 * 7.takeUnless:与takeIf恰好相反
 * 8.require(_name.isNotBlank()){}第一个输入参数为false则执行lambda表达式的内容,并抛出异常
 */


fun main() {
    val info = "adbdfsfsd"
    //1.永远返回的是对象本身,在apply{}中不持有it,而是持有this,可以直接调用对象的方法,所以可以链式调用
    info.apply {
        //大部分匿名函数都会持有it,但是这里不持有it,而是持有this,这个this就是info本身
        //可直接调用
        println(length)
    }.apply {
        println()
    }
    //2.在let持有it,即对象本身.返回是根据最后一行的值确定返回值
    info.let {
        //持有it
    }
    //3.在{}中持有this,返回类型根据最后一行来确定 {}为匿名函数
    info.run {
        length > 3
    }.run {
        if (this) "合格字符串" else "不合格字符串"
    }.run {
        println(this)
    }
    //具名函数调用,必须与上面匿名函数的输入参数保持一致
    info.run(::isLong)
        .run(::showText)
        .run(::mapText)
        .run(::println)
    //4.with
    var r1 = with(info, ::isLong)
    var r2 = with(r1, ::showText)
    with(r2) {
        println(this)
    }
    //5.also
    info.also {

    }
    //6.takeIf :如果匿名函数返回值为true,则返回本身;如果false返回为null
    info.takeIf {
        true
    }
    //7.takeUnless:如果匿名函数返回值为false返回为null
    info.takeUnless {
        true
    }
}

fun isLong(name: String): Boolean {
    return name.length > 3
}

fun showText(isLong: Boolean) = if (isLong) "合格字符串" else "不合格字符串"

fun mapText(name: String) = "$name"
