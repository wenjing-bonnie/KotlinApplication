package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/17
 *
 * Rxjava类似的一些函数
 */

val list = listOf(
    listOf("123", "456", "789", "1232132"),
    listOf("123abc", "456def", "789hij"),
    listOf("123qwe", "456asd", "789zxc")
)

/**
 * 变换函数
 * 含有的it(就是集合中的每个元素)，
 * 最后一行的代码返回值添加到新的集合中，返回值新的集合，!!!!
 * 该集合的类型为最后一行的代码返回值类型
 * 应用场景： 对集合里的元素进行变换，像包装或者类型转换
 */
fun map1() {
    val list = listOf("123", "456", "789")

    /**
     * 默认的就会返回最后的那个新集合
     */
    val listInt = list.map {
        it.toInt()
    }

    println(listInt)
}

/**
 * 将有嵌套关系的集合(List<List>)元素转换到一个集合
 * it：集合中的每个元素，该元素也是一个集合
 * 返回值：必须返回一个集合
 */
fun flatMap1() {

    val list1 = list.flatMap {
        println("集合中的it为${it}")
        it
    }
    list1.forEach {
        println(" new list is $it")
    }

    println(list1.size)
    println(list1)

}

/**
 * 过滤器：如果最后一行代码为tru就会将it添加到新的集合中，如果false，过滤掉，不加入
 */
fun filter1() {
    val newList = list.filter {
        it.size == 4
    }.map {
        println("filter ${it}")
        it
    }

    println("new ${newList.size}")
}

/**
 * 就是将第一个和第二个集合合并都一起，返回一个新集合.
 * 返回的新集合中的每个元素包括两个集合中的元素Pair(第一个元素,第二个元素)
 */
fun zip1() {

    val list = listOf("123", "456", "789")
    val list1 = listOf("adc", "dafs", "dsad")
    val newList = list.zip(list1)
    newList.map {
        println(it::class.javaObjectType)
    }
    println(newList)
    newList.forEach {
        println("${it.first} , ${it.second}")
    }
    newList.toMap().forEach {
        // it == MapEntry(String,String)
        println("${it.key} , ${it.value}")
        //it == Pair<String,String>
//            (key, ualue) ->
//        println("${key} , ${ualue}")
    }
}

fun zip2() {
    val list = listOf("zhangsan", "lisi", "hanmeimei")
    val list1 = listOf(12, 34, 23)
    val newList = list.zip(list1)
    val result = newList.map {
        "name is ${it.first} , age is ${it.second} ; "
    }
    println(result)
}

fun main() {
    map1()
    flatMap1()
    filter1()
    zip1()
    zip2()
}