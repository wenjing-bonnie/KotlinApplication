package com.wj.kotlin

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File

/**
 * create by wenjing.liu at 2022/3/11
 * 集合、数组
 */

@RequiresApi(Build.VERSION_CODES.N)
fun main() {
    list()
    println()
    println()

    set()
    println()
    println()

    set2List()
    println()
    println()

    array()
    println()
    println()

    map()
    println()
    println()

    mutableMap()
}

private fun mutableMap() {
    val map1 = mutableMapOf<String, Int>("key1" to 1, "key2" to 2)
    map1.put("key3", 3)
    map1["key4"] = 8
    map1 += "key6" to 10
    //如果有key3,则取出来;如果没有,则加入进去
    val a = map1.getOrPut("key3") {
        666
    }
    println(a)
    println(map1)
}

@RequiresApi(Build.VERSION_CODES.N)
private fun map() {
    //1.定义
    val map1 = mapOf<String, Int>("key1" to 1, "key2" to 2)
    val map2 = mapOf(Pair("key11", 123), Pair("key22", 234))
    //2.取值
    println(map1["key1"])
    println(map1.getOrDefault("key2", 233))
    println(map2.getOrDefault("key11", 11233))
    println(map2.getOrDefault("key11") {
        1333
    })
    println(map2.getOrElse("key1111") {
        1333
    })
    //3.遍历
    //it就是每个元素
    map2.forEach {
        println("${it.key}, ${it.value}")
    }
    map1.forEach { key, value ->
        println("${key}, ${value}")
    }
    for (it in map1) {
        println("${it.key}, ${it.value}")
    }
}

private fun array() {
    //1.数组定义
    val intArray = intArrayOf(1, 3, 4, 5)
    println(intArray)
    println(intArray.elementAtOrNull(10))
    //2.集合转数组
    intArray.toList()
    //3.对象数组
    arrayOf(File("12"))

}

private fun set2List() {
    val list = mutableListOf("123", "324324", "123", "324324", "324324")
    println(list)
    //list->set,并去重
    val set = list.toSet()
    println(set)
    //list->set->list,并去重
    println(set.toList())
    //直接去重
    println(list.distinct())
}

private fun set() {
    //1.不可变
    val set = setOf<String>("1233", "3434", "211312")
    val se1 = set.elementAt(0)
    val se2 = set.elementAtOrElse(4) {
        println("越界")
        "匿名越界"
    }
    val se3 = set.elementAtOrNull(10) ?: "空合并符越界"
    println("${se1}")
    println("${se2}")
    println("${se3}")
    //2.可变
    val set1 = mutableSetOf<String>()
    set1.add("12333")
    set1.add("2324324")
    println("${set1}")
}

@RequiresApi(Build.VERSION_CODES.N)
private fun list() {
    //1.不可变集合定义，无法完成可变操作
    val list = listOf("111111", "222222")
    list[0]
    //2.取出元素,防止越界
    list.getOrElse(4) { println("越界") }
    //越界之后返回为null+空合并操作符来判空
    val item = list.getOrNull(4) ?: " out of index "
    println("The item is $item")
    //3.定义可变集合
    val mulList = mutableListOf("1", "2")
    mulList.add("3")
    mulList.add("4")
    //4.将不可变集合->可变集合
    val mulList1 = list.toMutableList()
    //5.将可变->不可变
    val list1 = mulList.toList()
    //6.mutator([mju(ː)ˈteɪtə])函数 += -=
    mulList1 += "122"
    mulList1 -= "1"
    println(mulList1)
    //7.返回值为true时,将集合中的元素一个一个的删除
    mulList1.removeIf { true }
    println("The remove is ${mulList1}")
    //8.遍历元素
    for (li in mulList) {
        println(li)
    }
    mulList.forEach {
        println(it)
    }
    mulList.forEachIndexed { index, s ->
        println("${index} , " + s)
    }
    //9.过滤元素:不接受赋值,可以节省一点性能
    val (_, n2) = list
    println(n2)
}
