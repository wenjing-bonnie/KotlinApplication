package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/13
 * 接口相关
 * (1)同java一致,里面的成员和方法都是public open
 * (2)接口不能有主构造函数
 * (3)实现接口时,成员和方法都必须复写
 * (4)可以定义val,在声明的时候进行赋值,但是不能进行在修改
 *
 * 抽象类同java
 */
interface IUsb {
    var usbVersion: String
    var usbDevice: String
    val name: String
    fun insertUsb(): String
}

class Mouse(override var usbVersion: String = "USB 3.0", override var usbDevice: String) : IUsb {
    override val name: String
        get() = ""

    override fun insertUsb() = "version is $usbVersion , device is $usbDevice"
}

class Keybroad : IUsb {
    override var usbDevice: String = ""
        get() = "$field +"
        set(value) {
            field = value
        }
    override val name: String
        get() = "afd"

    override var usbVersion: String = ""
        get() {
            println("$field ")
            return "$field -"
        }
        set(value) {
            field = value
        }

    override fun insertUsb(): String {
        return "version is $usbVersion , device is $usbDevice"
    }
}


abstract class BaseClass {
    abstract fun absMethod(): String
    fun onCreate() {}
}

fun main() {
    val key: Keybroad = Keybroad()
    println(key.usbDevice)
    key.usbVersion = "1.0.0"
    key.name
    println(key.usbVersion)
    println(key.insertUsb())
}