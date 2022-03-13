# Kotlin学习笔记

马上就要入职新的公司了，以前的Android开发的技术都用不到了，争取在入职之前把Kotlin、Compose及Rxjava的内容全部搞定。  
加油，拼搏少妇！

## 一、Kotlin几个特殊的地方

### 1.定义编译时常量

* 在kotlin中常量定义只能定义在方法体外，如果定义在方法体内，编译无法通过。
* 定义编译时常量只能是基本数据类型
* 为什么只能定义在方法体之外? 如果可以在方法体内，必须在运行时才对变量进行赋值，失去了常量的意义。

### 2.kotlin中只有引用类型，但在转换成字节码的时候，会转换成基本数据类型

### 3.kotlin的null的注意点

* 在kotlin中不能随意赋值null，若必须赋值null，如`var name:String? = null`
* 判断不为null的几种方式：
    - (1)安全操作符?：例如`name?.length`。当name不为null的时候，才执行该代码；
    - (2)安全调用let：例如

    ```
    name?.let {
        println(it.length)
    }
    ```
    - (3)断言操作符!!：例如`name!!.length`，不管name是不是为null，都会执行该代码；若为null时，则抛出异常。所以只有100%确定不为null的时候才能使用。
    - (4)if语句：例如 `if(name == null)`
    - (5)`checkNotNull()`或`requireNotNull()`：在为null的时候抛出异常。
* 与空合并操作符?:配合来执行当出现null的情况。例如`name ?: "原来是一个null字符串"`或者

  ```
   name?.let {
        println(it)
    } ?: "原来是个null"
  ```

### 4.== 和 ===

== 值或内容的比较，相当于java的equals；   
=== 引用的比较

### 5.在类中定义成员变量时，在最后生成字节码的时候，会自动添加get/set方法

* 默认的`get()`/`set()`就将该成员变量的`field`进行赋值。
* 当然也可以对`get()`/`set()`替换该逻辑，例如：

```
   var name = "124"
    get() = field //不允许私有化
    set(value1) { //可以私有化
        field = "欢迎$value1"
    }
```

* `get()`不可私有化；`set()`可私有化。

### 6.range表达式

```
fun range(number: Int) {
    if (number in 0..10) {
        println("range 0~10")
    } else if (number in 20..30 step 2) {
        println("range 20~30")
    } else if (number in 40 downTo 30 step 2) {
        println("range 40~30")
    }
}
```

### 7.when表达式相当于java的switch

```
fun whenExpression(week: Int) {
    val info = when (week) {
        1 -> "111111"
        2 -> "222222"
        else -> {
            //此种情况下返回值为Unit,相当于java的void
            println()
        }
    }
}    
```

### 8.Nothing表达式：抛出异常

### 9.反引号函数

* 标记一些特殊含义的方法名
* 某些java方法恰好是kotlin的关键字，如`is`

[具体对应的类是KotlinContent.kt]

## 二、函数类型

在kotlin中可以将函数作为一种数据类型。数据类型的表示形式为`(输入参数的数据类型)->返回值的数据类型`，例如`(String,Int)->String`

### 1.将函数类型赋值给变量

像Int/String一样，可以直接将函数赋值给一个变量。有两种方式：

* (1)lambda表达式（本质就是一个匿名函数），其中最后一行代码为方法的返回值。

```
    val method: (Int) -> String = { a: Int ->
        //实现函数的具体内容.匿名函数不要写返回，最后一行就是返回值
        " ${a}dadafds"
    }
```    

这里的method后面的函数修饰符`(Int) -> String`可以省略，直接通过类型推断的方式得到method的类型

* (2)具名函数：在已有的方法名添加::

```
    val method = handleResponse(3, 4)
    private fun handleResponse(msg: String, code: Int): String {
        println("msg:$msg , code:${code}")
        return "msg:$msg , code:${code}"
    }
```

### 2.将函数类型作为输入参数

主要用来解决java通过接口回调方式来处理回调结果的问题。

* 实现方式：在输入参数中有一个函数类型的输入参数。相对于java的接口类型的输入参数，在kotlin中改为函数类型的输入参数。例如：

```
    private inline fun login(user: String, password: String, response: (String, Int) -> String) {
      if (user.length > 3 && password.length > 3) {
        response("ok", 200)
      } else {
        response("failed", 300)
      }
    }
```

在调用该login的方式的时候，通过下面的方式对函数类型的输入参数进行赋值，例如：

```
    login("liuwenjing", "123456") { msg: String, code: Int ->
        "${msg} , ${code}"
    }
```

或者

```
    login("liuwenjing", "123456", ::handleResponse)
```

* 当函数中含有函数类型的输入参数时，该方法需要用`inline`内联来修饰。  
  这样可以在kotlin转换成字节码的时候，不在需要通过创建类对象来处理该函数类型的输入参数，而是直接将方法替换到调用处，无对象开辟的损耗。

### 3.方法的返回值也可以是函数类型。

同样也是可以直接通过lambda表达式或者具名函数的方式，例如：

```
    private fun returnMethod(a: Int, b: Int): (String, Int) -> String {
      val firstSum = a + b
      return { aa: String, bb: Int ->
        println("$a+$b=?")
        val sum = aa + bb + firstSum
        sum
    }
    // return ::handleResponse
}
```

或者

```
    private fun returnMethod(a: Int, b: Int): (String, Int) -> String {
      val firstSum = a + b
      return ::handleResponse
}
```

【TODO 遗留问题：函数类型的返回值的应用场景是什么呢？？？？？】

[具体对应的类是KotlinMethodType.kt]

## 三、数组和集合

[具体对应的类是KotlinListArrayMap.kt]

## 四、类

### 1.类构造器

* 主构造器：跟在类名后面的()为主构造器.默认的为()，可省略。含有最多的输入参数。
    - (1)若输入变量为临时变量，可直接采用`_name:String`。该值只可在`init{}`中使用，在其他地方无法使用，需要定义成员变量`var name = _name`
      接收该临时变量才可以在类的其他地方使用。
    - (2)通过`var/val name:String`，可直接使用输入变量
    - (3)在`init{}`增加主构造函数的逻辑，转换成字节码只是`{}`代码块，非`static{}`静态代码块。
* 次构造器：通过`constructor():this()`声明次构造函数。最后通过主构造函数统一管理。
* 先调用主构造函数 -> init{} -> 次构造函数

### 2.类的一些基本概念

* 类里面的代码按顺序执行，一般要将对变量赋值放在最前面。在使用变量之前，一定先要将变量进行赋值。
* 类都会继承Any，默认的final进行修饰，不能被继承。->通过`open class`来移除final。
* `对象 is class`：判断对象是不是class的对象。
* `对象 as class`：将对象转换成class对象。
* 懒加载有两种方式：
    - 使用`lateinit var lazy1: String`，在使用的时候必须进行手动初始化。
    - 惰性加载，在使用该变量的时候，会主动加载，例如：

```
    val lazy2 by lazy {
        lazyVar()
    }
    private fun lazyVar(): String {
        return "1222"
    }
```

### 3.接口类的实例化

* 匿名方式：通过`object:接口类`，例如：

```
   val onClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
   }
```

* lambda表达式，但不适用于kotlin中的接口类。例如：

```
   val onClickListener1 = View.OnClickListener { TODO("Not yet implemented") }
```        

* 具名：直接定义一个子类实现接口，然后实例化子类。

### 4.单例类 -> 类似于java的static类

* `object class`声明单例类，只有一个该类的实例，此时`init{}`为静态代码块。在转换成字节码时，该类会通过`public static final`进行修饰。
* `companion object{}`声明伴生对象，`{}`里面可定义val/var/fun。该伴生对象只会加载一次。在转换成字节码时，该类会通过`public static final`
  进行修饰。

### 5.内部类和嵌套类

* 下面InterClass就是一个嵌套类。外部类可以访问嵌套类，但是嵌套类不能访问外部类。

```
class OuterCls {
    class InterClass {
        
    }
}
```

* 在java中外部类和内部类是可以相互访问，所以在kotlin中通过在`inner`来标记是一个内部类，才可以访问外部类。

### 6.数据类：通过`data`修饰class

* 普通类只会继承Any，只提供了标准和set/get/构造函数，里面的方法都没有复写。
* 数据类实现了具体的方法，例如copy/toString/hashCode/equals等等，可以直接利用这些方法。
* 但是数据类在实现这些方法的时候，仅处理主构造函数中的输入参数。如果数据类有次构造函数，需要注意这些方法的使用。
* `fun component1()`对应主构造函数的输入参数，并且顺序只能为1,2,3...
