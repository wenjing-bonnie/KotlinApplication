package com.wj.kotlin

import android.view.View
import java.io.BufferedReader

/**
 * create by wenjing.liu at 2022/3/11
 * 特殊的内置函数:TODO 具体的应用场景是什么呢？

 * 1.let
 *   返回值的类型根据匿名函数的最后一行代码;含有it,就是该对象.
 *   [应用场景] (1)与空合并符进行null逻辑处理,代替if判断。(2)明确该变量的作用域范围内可以使用
 *
 * 2.with
 *   与run一样,但是需要将对象主动传入,例如with(info, ::isLong)
 *   [应用场景]消除对同一个变量多次引用。调用同一个类的多个方法，可以省去类名重复，直接调用类的方法即可。
 *
 * 3.run
 *   返回值的类型根据匿名函数的最后一行代码；含有this,可直接调用对象的方法和直接将对象传入到{}或具名函数
 *   可以方便将上一个方法的返回值传递到{}或具名函数中
 *   是let/with结合体。既可以直接访问对象的方法，又可以弥补with在传入对象需要判空问题
 *   [应用场景]避免临时变量名作用域过长，代码赋值风险。适合于let/with的任何场景
 *
 * 4.apply
 *   返回值就是对象本身的类型;在匿名函数中无it,持有this,可以直接调用对象里的方法和直接将对象传入到{}或具名函数.
 *   方便链式调用。
 *   与run的作用差不多，唯一不同的是返回值不同。返回的是对象本身，而run返回的是最后一行代码的值
 *   [应用场景]用于对象实例化时对属性进行赋值&返回该对象。或者动态inflate出一个view时，需要给view绑定数据。特别是model向viewmodel转化实例化过程时需要
 *   如
 *
 *    * 5.also
 *   返回值就是对象本身的类型;含有it,就是该对象.
 *   可以链式调用
 *
 *
 * 6.takeIf:如果匿名函数返回值为true,则返回为对象本身;否则返回为null
 *    与空合并符进行null逻辑处理
 * 7.takeUnless:与takeIf恰好相反
 * 8.require(_name.isNotBlank()){}第一个输入参数为false则执行lambda表达式的内容,并抛出异常
 */

/**
 * 与空合并符配合使用,代替if判断
 */
private fun let() {
    var name: String? = null
    var result = name?.let {
        "这个名字为$it"
    } ?: "还没有起名字"
    println(result)
}

/**
 * 持有的this,返回的是对象本身,对象初始化
 */
private fun apply() {
    val map = HashMap<String, Int>()
    map.apply {
        put("key1", 12)
        put("key2", 123)
    }
    println(map)
    //
//    val rootView = View.inflate(activity,R.layout.activity_main,null).apply{
//        setBackgroundColor(Color.RED)
//        setPadding(0,0,0,0)
//    }
    多层

}

/**
 * 全局函数和扩展函数
 */
private fun run() {
//1.全局函数代替java{}作用域,避免重复创建变量
    run {
        val first = "123"
    }
    run {
        val first = 345
    }
    //2.最后一行为返回值
}

/**
 * (line=reader.readLine())!=null这种写法，是由于赋值语句可以作为表达式才可以这么写。
 * 但在kotlin中赋值语句不能作为表达式
 * also通过it来访问对象,并且返回的是对象本身
 */
private fun also(reader: BufferedReader) {
    var line: String?;
    while (reader.readLine().also { line = it } != null) {
        println(line)
    }
}

/**
 * 消除对同一个变量多次引用
 *
 */
private fun with(){

//    val service = Service()
//    service.setName("xxx")
//    service.doSome()
//    service.ddd()
//    service.sddd()
//
//    with(service){
//        setName("xxx")
//        doSome()
//        ddd()
//        sddd()
//    }

}


fun main() {
    println("let")
    let()
    apply()


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
