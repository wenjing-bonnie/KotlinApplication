package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/12
 * 类构造器
 * 1.主构造函数: 跟在类名后的为主构造函数
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
 *
 * 懒加载
 */
class KotlinClass(_name: String) //主构造函数,默认有个()
{
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
}

fun main() {
    KotlinClass("", 'M')
}