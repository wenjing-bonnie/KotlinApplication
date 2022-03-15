package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/13
 * 1.泛型
 * 在声明类的时候，声明泛型
 * class Generic<T>(val obj:T)
 * 在声明方法的时候，声明参数
 * fun <B> show(item:B)
 * 若是在参数中有多个泛型，那么在定义类型的时候就是
 * fun <O, R> map(isMap: Boolean, input2: R, mapAction1: (T, R) -> O)
 * 2.泛型约束
 * 指定泛型的上界，默认为Any？可通过fun <B : String> show(item: B)限制上界范围
 * TODO 若有多个泛型,没有验证通过 fun <R> show1(item: R) where R : Teacher, R : Student
 */
class Generic<T>(val obj: T, val isObj: Boolean) {
    fun show(obj: T) {
        println("outputs: $obj")
    }

    fun getGenericObj() = obj.takeIf {
        isObj
    }
}

fun <B : String> show(item: B) {
    item?.also {

    }
}

class Student {

}

class Teacher {}

//fun <R> show1(item: R) where R : Teacher, R : Student {
//
//}


/**
 * 模仿Rxjava
 */
class RxJava<T>(val isMap: Boolean, var input: T) {
    /**
     * T是要变换的类型,R是变换后的类型
     * 单表达式方法，当方法仅有单个表达式时，可以省略花括号，并在 = 后指定方法体即可
     */
    fun <R> map(mapAction: (T) -> R) = mapAction(input)

//    fun <R> map(mapAction: (T) -> R): R {
//        return mapAction(input)
//    }

    fun <O, R> map(isMap: Boolean, input2: R, mapAction1: (T, R) -> O) {
        if (isMap) mapAction1(input, input2) else null
    }
}

fun main() {
    println(Generic("123", true).getGenericObj())
    val result = RxJava(true, 123).map {
        println("${it}")
        it.toString()
    }

    val result1 = RxJava(true, "123").map(true, 123) { first, second ->
        println("$first , $second")
    }
}
