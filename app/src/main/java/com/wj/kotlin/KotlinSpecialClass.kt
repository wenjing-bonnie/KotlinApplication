package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/13
 * 特殊的几种类
 */
/**
 * 1.使用数据类是为了可以直接使用里面的方法
 * (1)运算符重载
 * 通过operator fun AddClass.plus可以提示哪些运算符可以重载
 */
data class AddClass(var a: Int, var b: Int) {
    /**
     * 重载
     */
    operator fun plus(add: AddClass): Int {
        return (a + add.a) + (b + add.b)
    }
}
/**
 * (2)数据类的使用条件
 *   1.服务器请求返回的响应javabean loginresponsebean
 *   2.数据类至少有一个参数的主构造函数,并且输入参数至少要有一个var/val修饰
 *   3.数据类不能使用abstract/open/sealed/inner等进行修饰,只做数据存储
 *   4.需求比较,可以方法的利用里面的比较/copy/toString等功能
 */


/**
 * 2.枚举其实就是一个class.
 * 枚举值本身等价于枚举本身 Week.星期一 is Week
 * （1)枚举值可以是一个常量字符串
 */
enum class Week {
    星期一,
    星期二
}

/**
 * (2)可以通过主构造函数给定初始值
 */
enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00)
}

data class Job(val content: String, val title: String) {
}

/**
 * (3)枚举值也可以是一个对象
 *   1.所有的枚举值类型必须保持一致
 *   2.枚举类的主构造函数必须和枚举的参数保持一致
 */
enum class Jobs(val job: Job) {
    DOCTOR(Job("看病", "doctor")),
    TEACHER(Job("教书", "teacher")),
    POLICE(Job("抓坏人", "police"));

    fun job() {
        println("in The job is ${job.title} , the content is ${job.content}")
    }
}

/**
 * 3.密封类
 * 可以解决枚举类的枚举值必须一致问题,例如单独输出police的名字
 * (1)通过sealed进行修饰class
 * (2)通过object定义成员变量,并且继承本类
 * (3)需要扩展的枚举值通过class增加输入变量
 */
sealed class JobSealed {
    object DOCTOR : JobSealed()
    object TEACHER : JobSealed()
    class POLICE(val name: String) : JobSealed()
}

/**
 * 使用密封类
 */
class JobSummary(val job: JobSealed) {

    fun showJob() =
        when (job) {
            is JobSealed.DOCTOR -> "白衣天使"
            is JobSealed.TEACHER -> "教书育人"
            is JobSealed.POLICE -> "人民公仆 ${job.name}"
        }
}

class SingletonLazy {
    companion object {
        private var instance: SingletonLazy? = null
            get() {
                if (field == null) {
                    field = SingletonLazy()
                }
                return field
            }

        @Synchronized //添加同步锁之后 懒汉式 安全
        fun getInstanceAction() = instance!!


    }

    fun show() {
        println("show")
    }
}

class SingletonLazySync private constructor() {
    companion object {
        val instance: SingletonLazySync by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { SingletonLazySync() }
    }

    fun show(){
        println("show")
    }
}


fun main() {
    println(AddClass(1, 2) + AddClass(2, 3))
    println(Week.星期一 is Week)
    Jobs.DOCTOR.job()
    println(JobSummary(JobSealed.POLICE("张三")).showJob())
    SingletonLazy.getInstanceAction().show()
    SingletonLazySync.instance.show()
}