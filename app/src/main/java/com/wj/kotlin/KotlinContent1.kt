package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/10
 * [函数类型总结]
 * 1.在Kotlin中可以把函数作为一种数据类型.修饰符表示形式为[(输入参数的数据类型)返回值的数据类型]
 *   (1)可以理解为可以定义一个函数类型的变量,该函数类型相当于Int/String...
 *   val method:(Int,String) -> String = { a:Int, b:String ->
 *      //方法体内的最后一行默认为方法的返回值.若该方法需要返回值,最后一行代码的类型必须符合
 *   }
 *   (2)在定义该函数类型的变量,也可以通过类型推断的方式来自行根据方法体的最后一行代码来确认返回值
 *   val method = { a:Int,b:Int -> dd
 *         a+b
 *   }
 *   (3)只有一个输入参数的时候,在{}默认的就会有一个it的输入参数
 * 2.函数类型的变量可[解决java通过接口回调的方式来进行处理结果]
 *   (1)通常方法的输入参数中有一个[函数类型的变量]来代替[java的接口类型的输入参数]
 *   inline fun login(user: String, password: String, response: (String, Int) -> String) {
 *
 *   }
 *   (2)使用函数类型的输入参数的时候,该方法需要用[inline 内联]来进行修饰:
 *   可以将回调函数直接拷贝到调用处,避免转换成字节码的时候,需要创建对象,引起性能损耗
 *   (3)在调用该内联函数,对函数类型的输入变量进行赋值的时候,有两种方式:
 *      1)lambda表达式：lambda表达式本质就是一个函数类型
 *       { user:String, password:String ->
 *        //具体实现的内容
 *        }
 *      2）在回调函数名前面添加::
 *      ::handleResponse
 * 3.
 * 4.
 *
 */
/**
 * 1.匿名函数
 */
private fun niMing() {
    "Derry".filter {
        it.isHighSurrogate()
    }
    val method = { a: Int, b: Int ->
        a + b
    }
}

/**
 * 2.函数类型&隐式返回
 *  函数可作为传入参数，分为两部分
 *  (1)可以定义一个变量为函数类型,一个函数类型格式:(输入参数类型)-> 返回值类型
 *  (2)直接调用变量进行传入值即可
 *  (3)具体实现过程中，不需要return，默认的最后一行就是返回值,若只有一个参数,则默认会有一个it的输入参数
 *  TODO 这里难道不会引起在一个方法里代码量变大吗？？

 *  用处在于：代替java的接口回调方式
 */
private fun methodAction() {
    //1.定义函数输入输出的声明
    //该method的变量的类型为"(a: Int) -> String" 输入参数->返回值
    val method: (Int) -> String = { a: Int ->
        //2.实现函数的具体内容.匿名函数不要写返回，最后一行就是返回值
        " ${a}dadafds"
    }
    //3.调用此函数
    println(method(30))
    /**
     *  3.函数类型自动类型推断,即上面的 (1)可以直接省略,自动根据最后一行推断函数的返回值
     *  4.匿名函数等价于lambda表达式
     */
    val voidmethod = { a: Int ->
        println()
        " dadafds"
    }
    println(voidmethod)
}

/**
 * 5.定义一个函数类型的功能:用来实现一个原java的接口回调
 * 若一个函数中有lambda表达式，最好用inline进行修饰
 * 若不使用内联,会生成多个对象调用，造成性能损耗
 * 若增加内联,相当于C++的宏定义，会把函数直接替换到调用处，无对象开辟的损耗
 *
 */
private inline fun login(user: String, password: String, response: (String, Int) -> String) {
    if (user.length > 3 && password.length > 3) {
        response("ok", 200)
    } else {
        response("failed", 300)
    }
}

fun main() {
    methodAction()
    /**
     * 6.在对lambda表达式进行赋值的时候
     * (1)一种是通过lambda表达式来进行赋值
     */
    login("liuwenjing", "123456") { msg: String, code: Int ->
        "${msg} , ${code}"
    }
    /**
     * (2)通过函数类型的对象:在函数名前面添加::
     */
    login("liuwenjing", "123456", ::handleResponse)
}

private fun handleResponse(msg: String, code: Int): String {
    return "msg:$msg , code:${code}"
}
