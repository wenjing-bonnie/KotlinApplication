package com.wj.kotlin

/**
 * created by wenjing.liu at 2022/3/15
 *
 * vararg表示多个参数
 */
class KotlinVararg<T>(vararg _objs: T, val isMap: Boolean) {
    /**
     * 创建objs来接收_objs。
     * 因为_objs是一个动态参数，需要通过out设置接收的这个objs只能可读，不能修改
     */
    var objs: Array<out T> = _objs

    /**
     * takeIf：里面的返回为true则返回对象本身,若为false则返回null
     */
    fun showObj(index: Int): T? = objs[index].takeIf { isMap }

    /**
     * 这个lambda表达式可以考虑成回调的一种思想
     */
    fun <O> mapObj(index: Int, mapAction: (T?) -> O) = mapAction(objs[index].takeIf { isMap })

}

fun main() {
    val k1 = KotlinVararg<Any?>("we", 111, "1233", isMap = true)

    val re = k1.mapObj(0) {
        println("1 , " + it.toString())
        24
    }
    println("$re")

}