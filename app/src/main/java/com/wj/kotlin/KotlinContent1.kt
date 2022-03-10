package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/10
 */
/**
 * 1.匿名函数
 */
private fun niMing() {
    "Derry".filter {
        it.isHighSurrogate()
    }
}

/**
 * 2.函数类型&隐式返回
 *
 */
private fun methodAction() {
    //1.定义函数输入输出的声明
    //该method的变量的类型为"(a: Int) -> String" 输入参数->返回值
    val method: (a: Int) -> String = { a: Int ->
        //2.实现函数的具体内容.匿名函数不要写返回，最后一行就是返回值
        " ${a}dadafds"
    }
    //3.调用此函数
    println(method(30))
}


fun main() {
    methodAction()
}
