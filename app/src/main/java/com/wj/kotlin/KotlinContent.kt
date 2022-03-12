package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/10
 * 用来学习Kotlin的特殊的几个地方
 *
 * 1.定义编译时常量:只能定义在函数外,如果在函数体内,编译不通过.
 *   (1)编译时常量只能是基本的数据类型
 *   (2)编译时常量只能定义在函数之外定义：如果可以在函数内，必须在运行时才能对函数进行赋值.
 * 2.Kotlin只有一种引用数据类型,无基本数据类型,但在编译成字节码的时候,会转换成基本的数据类型
 * 3.kotlin的null的处理
 *   (1)不能随意赋值null,若需要赋值为null,在数据类型在后面添加 ? ：例如 var name: String? = "123"
 *   (2)判断不为null的几种安全处理方式:
 *     1)安全操作符?:  例如 name?.length. 只有name不为null的时候才会执行该语句
 *     2)安全调用let:  例如 name?.let{}. 在{}中含有it就是name,当name不为null的时候,才会执行let里面的内容
 *     3)断言操作符!!: 例如 name!!.length. 不管name是不是null,都执行调用.若为null的时候,会抛出异常,必须为100%不为null才可以使用
 *     4)if判断null:  例如 if(name == null)
 *     5)checkNotNull()或者requireNotNull()来判断null时,抛出异常
 *   (3)执行为null的时候逻辑: 空合并操作符 name ?: "xxx" . 若name为null,则执行后面的内容
 *   (4)通常 ?: 与let来配合执行null和非null的情况: 例如 name?.let { "$it" } ?: "原来是null"
 * 4.==  值 内容的比较, 相当于equals
 *   === 引用的比较
 * 5.在类中定义field,在最后生成字节码的时候,相当于:
 *   var name = "wenjing" 等价于
 *   @NotNull
 *   private String name = "wenjing"
 *   public void setName(String name){
 *       this.name = name;
 *   }
 *   public String getName(){
 *
 *   }
 * 6.防范竞态条件(TODO 应该就是利用内置函数来实现这个功能)
 *
 */
const val TAG = "123"

//覆盖set()
var nameTesr = "124"
    get() = field //不允许私有化
    set(value1) { //可以私有化
        field = "**$value1"
    }

//覆盖get(),field就会失效了
val name: Int
    get() = 23
val name1: String? = "12 3454"

//防范竞态条件
fun getShowName(): String {
    return name1?.also {
        "1232"
    } ?: "原来是个null"
    name1?.let {
        println(it)
    } ?: "原来是个null"
}


/**
 * 3.range 表达式：连续的可遍历范围
 */
fun range(number: Int) {
    if (number in 0..10) {
        println("range 0~10")
    } else if (number in 20..30 step 2) {
        println("range 20~30")
    } else if (number in 40 downTo 30 step 2) {
        println("range 40~30")
    }
}

/**
 *  3.kotlin的null的处理
 */
fun checkNull() {
    var name: String? = "1234566"
    name ?: "123"
    name!!.length
    name?.let { "$it" } ?: "原来是null"
    val index = name.indexOf("4")
    name?.substring(0 until index)
}


/**
 * 4.when 表达式.类似java的switch
 * 表达式有返回值.若所有的类型一致,则返回的为对应类型;若类型不一致,则返回Any
 *
 */
fun whenExpression(week: Int) {
    val info = when (week) {
        1 -> "111111"
        2 -> "222222"
        else -> {
            //此种情况下返回值为Unit,相当于java的void
            println()
        }
    }

    /**
     * 5.String模板.可以添加表达式
     */
    val stringFormat = "${if (week == 1) "123" else "345"}"
}

/**
 * 6.默认的函数都是public类型
 * 7.可以为函数的输入参数设置默认值
 * 8.在调用的时候,可以不用关心调用顺序,直接通过赋值的方式进行调用,如sum(second = 5,first = 4)
 */
private fun sum(first: Int, second: Int = 4): Int {
    return first + second
}

/**
 * 9.Nothing,并不是注释,而是在运行的时候会中止调用
 * 10.反引号函数名
 * (1)定义一些特殊字符串的函数名:如`1344`
 * (2)若java定义的函数名恰好是kotlin的关键字,则函数名用反引号,如:`in`
 */
private fun todoMethod() {
    TODO("还未实现")
}


fun main() {
    range(34)
    sum(second = 5, first = 4)
    nameTesr = "123"
    println(nameTesr)
    println(name)
    println(getShowName())
}