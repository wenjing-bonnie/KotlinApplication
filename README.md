# Kotlin学习笔记

马上就要入职新的公司了，以前的Android开发的技术都用不到了，争取在入职之前把Kotlin、Compose及Rxjava的内容全部搞定。  
加油，拼搏少妇！

## 一、Kotlin几个特殊的地方

## 1.定义编译时常量

* 在kotlin中常量定义只能定义在方法体外，如果定义在方法体内，编译无法通过。
* 定义编译时常量只能是基本数据类型
* 为什么只能定义在方法体之外? 如果可以在方法体内，必须在运行时才对变量进行赋值，失去了常量的意义。

## 2.kotlin中只有引用类型，但在转换成字节码的时候，会转换成基本数据类型

## 3.kotlin的null

* 在kotlin中不能随意赋值null，若必须赋值null，如`var name:String? = null`
* 判断不为null的几种方式：     
  -(1)安全操作符?：例如`name?.length`。当name不为null的时候，才执行该代码；       
  -(2)安全调用let：例如   
    ```
  name?.let {
        println(it.length)
    }
    ```
  -(3)断言操作符!!：例如`name!!.length`，不管name是不是为null，都会执行该代码；若为null时，则抛出异常。所以只有100%确定不为null的时候才能使用。    
  -(4)if语句：例如 `if(name == null)`    
  -(5)`checkNotNull()`或`requireNotNull()`：在为null的时候抛出异常。    
