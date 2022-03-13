package com.wj.kotlin

import android.view.View

/**
 * create by wenjing.liu at 2022/3/12
 * 类构造器
 * 1.主构造函数: 跟在类名后的为主构造函数.里面含有最多的输入参数
 *  (1)规范来说,其中输入变量采用"_xxxx"的方式,是临时的,不能直接使用,需要定义name = _name接收
 *     但可通过在 var name:String来解决直接使用该变量
 *  (2)在init{}进行初始化主构造,统一管理次构造器.在init{}中可直接使用临时输入变量
 * 2.次构造函数:通过constructor()声明
 *  (1)必须通过:this()来调用主构造函数.通过主构造函数进行统一管理
 * 3.先调用主构造函数(初始化类成员变量、init{}代码块),然后再去调用次构造函数
 * 4.require(_name.isNotBlank()){}第一个输入参数为false则执行lambda表达式的内容,并抛出异常
 * 5.类里面的代码按顺序执行,所以在变量使用之前一定要保证赋值,一般将赋值操作放到最前面
 * 6.类默认的为final进行修饰,不能被继承.=> 可通过open class 来移除final修饰
 * 7. 对象 is class: 判断对象是不是class的对象
 *    对象 as class: 将对象转换成class的对象
 * 8.所有的类都继承Any
 * 9.object声明单例实例,只有一个创建该类的实例,其中init{}为静态代码块,
 *   相当于在java的静态代码块中实例化该类的实例,该实例会通过public static final进行修饰
 * 10.通过[object:对象表达式]来创建一个java的接口对象,可以有两种实现方式
 *    (1)匿名方式
 *       val onClickListener = object : View.OnClickListener {
 *                override fun onClick(v: View?) {
 *                   TODO("Not yet implemented")
 *               }
 *           }
 *    (2)lambda表达式
 *       val onClickListener =  View.OnClickListener {}
 *    (3)具名实现方式:
 *    直接定义一个子类继承View.OnClickListener,然后实例化子类
 *    但对于kotlin定义的接口类,不能使用第二种表示方式
 * 11.伴生对象companion object{}里面既可以添加val/var/方法
 *    在kotlin中没有static,相当于java的static
 *    不管类对象创建多少次,但是伴生对象companion的对象只有一次加载
 *    原理在于:转化成字节码的时候,会将companion转化成public static final的类
 * 12.内部类
 *    内部类不能直接访问外部类,必须通过在内部类前添加inner成为内部类,才可以两者互相访问
 *    嵌套类:外部类的类可以访问内部的嵌套类,内部类不可以访问外部类
 *    默认情况为嵌套类,嵌套类前添加inner就变成了内部类
 * 13.数据类
 *    class前通过data来进行修饰.
 *    相对于普通类(只会继承Any,只提供标准,里面接口方法没有处理)set/get/构造函数,
 *    增加了更多的方法:如copy/toString/hashCode/equals,这些方法都有复写实现具体的功能.
 *    使用数据类在于可以直接利用上面的那些方法.普通类这些方法没有实现
 *    但是这些方法在处理的时候,只会关注主构造里面的内容,假设有次构造函数,在使用这些方法的时候,要注意这里的问题
 *     fun component1()就是对应主构造函数的输入参数,并且顺序必须为1,2,3...
 *
 *
 *
 * 懒加载
 */
class KotlinClass( _name: String) //主构造函数,默认有个()
{


    val onClickListener = View.OnClickListener { TODO("Not yet implemented") }
    companion object{
        var info = "1232"
    }


    /**
     * 懒加载,在使用的时候必须进行手动初始化
     */
    lateinit var lazy1: String

    /**
     * 惰性加载,会自动加载,然后在使用
     */
    val lazy2 by lazy {
        lazyVar()
    }

    /**
     * 1.次构造函数，必须调用主构造函数.通过主构造函数进行统一管理，更好的初始化设计
     * 先调用主构造函数(类成员和init代码块同时生成),再去调用次构造函数
     * 若在项目中调用默认的构造函数,即KotlinClass()那么此时会调用主构造函数
     */
    constructor(name: String, sex: Char) : this(name) {
        println("次构造函数进行调用$name")
    }

    /**
     * 2.初始块,主构造函数进行调用,并不是static类型
     */
    init {
        /**可以直接使用主构造函数的临时输入变量*/
        println("主构造函数进行调用$_name")
        //如果第一个参数是false的话,就会执行后面的lambda表达式,抛出异常
        //判断name是不是空值
//        require(_name.isNotBlank()){
//            println("空的名字")
//        }
    }

    private fun lazyVar(): String {
        return "1222"
    }

    open fun add() {

    }
}

fun main() {
    KotlinClass("", 'M')
}