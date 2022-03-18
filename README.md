# Kotlin学习笔记

马上就要入职新的公司了，以前的Android开发的技术都用不到了，争取在入职之前把Kotlin、Compose及Rxjava的内容全部搞定。  
加油，拼搏少妇！

## 汇总kotlin几个关键点

### 一、几个特殊的函数

#### 1.高阶函数

* 作为传入参数或返回值的函数被称为高阶函数。
* 当高阶函数作为一个函数(为了方便后面描述，称该函数为**函数A**)的传入参数：目的是为了代替java的接口回调。
    - 定义一个传入参数格式为：`action : (String,Int)-> Unit`
        - (1) `action`为参数名，在函数体内通过`action(...)`来完成回调。
        - (2) `(String,Int)`为输入参数的数据类型；
        - (3) `Unit`为返回值的数据类型。
    - 在调用**函数A**时，为输入参数传值，可以通过两种方式：
        - (1) `lambda表达式（本质就是匿名函数）`，如`{a:String,b:Int -> ... }`。
            - 1)在调用**函数A**时，若该输入参数为最后一个，可直接跟在**函数A**
              的括号的后面。
            - 2)在调用**函数A**时，若只有该高阶函数一个参数，则*函数A**的括号可省略。
        - (2) `::函数名（又称为具名函数）`，即将另外一个具体已声明的函数通过`::函数名`传入即可，该具体已声明的函数的输入类型和返回值要与前面的`action`的类型一致。
* 当高阶函数作为一个函数(为了方便后面描述，称该函数为**函数A**)的返回值：目的是**函数A**的一些信息或者中间处理值会影响到返回的函数的处理结果，先计算完**函数A**
  里面的逻辑，在调用返回函数进行传值在进行计算（有点装饰者模式感觉，TODO 这个应用场景还没有想明白）。
    - 可将函数直接赋值给一个变量。可直接**函数A**的调用将赋值给一个变量(为了方便后面描述，称该变量为**变量a**)，然后就可通过`变量a(...)`
      来调用该返回的函数。
    - 同样作为**函数A**的返回值时，有两种方式：
        - (1)`lambda表达式`,如` return {a:String,b:Int -> ... }`
        - (2)`::函数名（又称为具名函数）`，即将另外一个具体已声明的函数通过`return ::函数名`返回。

#### 2.内联函数

* 当在声明函数中含有函数类型的传入参数时，通常在 `fun`前添加`infline`关键字。
* 带来的好处：该内联函数在转换成字节码的时候，动态将传入的函数拷贝到调用处，避免额外创建对象来完成传入函数的调用。

#### 3.扩展函数

* 可在任意类上动态添加扩展函数，特别适用于对开源框架或已有的系统类的修改。
    - 实现格式： `fun 类名.函数名(...)`
* 支持在任意类上对属性进行扩展
    - 实现格式： `val/var 类名.成员变量 : 数据类型 get() = ...`
* 支持对可空类型的扩展函数
    - 实现格式： `fun 类名?.函数名(...)`
* 扩展文件
    - 将所有的扩展函数单独写到一个文件，该文件就是扩展文件。
    - 通过`as`对扩展函数进行重命名。

#### 4.内联扩展函数

* let：持有it指向对象。返回的是{}最后一行代码的类型。
    - 应用场景：与空合并符结合进行null逻辑判断，代替if
* run：持有this指向对象。可直接调用对象的成员变量和成员方法。返回的是{}最后一行代码的类型。
    - 应用场景：设定临时变量的作用域。let/with的任何场景。
* apply：持有this。返回的是对象本身，可链式调用。
    - 应用场景：对象实例化赋值。例如`View.inflater`的view绑定数据；多层级判空问题。
* also：持有it。返回的是对象本身。可链式调用。
    - 应用场景：同let。`while (reader.readLine().apply { line = this } != null) `
      代替java的`while ( (line=reader.readLine())!=null )`
* with():不是扩展函数，而是内联函数。对象需要传入，在函数体内持有this，可直接调用传入的对象的成员变量和成员方法。
    - 应用场景：多次调用同一对象的不同方法。可以省去对象名的调用，直接在`{}`内调用对象中的方法即可。
* (T)：T类型。在函数体内持有it指向对象。
* T.()：T的扩展函数。在函数体内持有this指向对象，可以直接调用T的成员变量和成员方法（这个本来就是扩展函数存在的意义）。
* ()：跟T无关。就是一个普通函数，无法访问T，只能通过外部T的实例来访问T的成员变量和成员方法

#### 5.lambda表达式

* 本质上是一个匿名函数，`{}`的最后一行代码的类型就是函数的返回值。
* 可作为函数的传入参数，代替java的接口回调。若在调用该函数的时候，若是最后一个参数，可直接在括号外添加 `{}`即可；若仅有这一个参数，那么括号可直接省略
* 可作为函数的返回值。若该函数就只有 `return lambda表达式`，那么可简写为`fun 函数名(...) = {...}`。(准确的说，函数体内只有一行代码的时候，都可以直接让该函数 =
  仅有的一行代码即可)
* 作为函数的形参时，可以携带接收者（即扩展函数），即该传入参数的数据类型可以是：`String.(Int , Int) -> Unit`。
  当调用的该函数时，传入lambda表达式的时候`{a:Int, b:Int -> 在具体的实现过程中可以直接调用String里面成员变量和成员方法}`
    - `String`:是要增加扩展函数的类
        - `(Int , Int)`：该扩展函数有两个输入参数
        - `Unit`：该lambda表达式的返回值

#### 6.infix中缀表达式

* 实现类似于map中的` "key" to "value" `
* 必须具备的条件，才可以改成中缀表达式的样式：
    - 必须为成员函数或者扩展函数
    - 只能有一个参数，并且参数不能为可变参数或者默认参数。
* 实现原理：
    - 对对象1进行添加扩展函数，但是该扩展函数只能有一个输入参数，并且是对象2类型的输入参数。
    - 该扩展函数通过`infix`进行修饰

 ```  
  infix fun <A,B> A.toPair(b:B):自己想要的返回类型{
       在调用 `A to Pair B` 想要实现具体的逻辑
  }
 ```   

#### 7.invoke函数

* 目的是为了实现可以像函数调用那样去调用对象。(TODO 这个的应用场景还没有想明白。)
* 实现原理：重载`invoke`运算符，例如`operator fun invoke(){ } `

#### 8.顶层函数

* 在kotlin中不需要将方法定义到容器类中，可以直接在一个.kt文件中直接定义一些方法。这些方法就是顶层函数。
* 可直接当静态类来使用
* 在转换成字节码的时候，会将在这些方法的添加到`文件名Kt`的类中，所有的方法都添加了`static`修饰符。

### 二、几个特殊的类

#### 1.数据类

* data class：该类已经实现了copy/toString等方法，可以直接调用。
* 仅处理主构造函数的输入参数，并且输入参数必须至少一个用val/var进行修饰。
* 不能被abstract/open/sealed/inner等进行修饰，只能做数据存储。

#### 2.嵌套类

* 默认的`class A{ class B{} }`中的B为嵌套类，不能直接调用A里面的成员变量和成员方法，但是A可以调用B的。

#### 3.内部类

* 针对嵌套类中的B前面通过`inner`进行修饰来标记为一个内部类，就可以完成A和B之间的成员变量和成员方法的相互调用。

#### 3.单例类

* `object 类名`声明单例类。只有一个实例，并且`init{}`为静态代码块。同java的static类
* `companion object{}`声明伴生对象。在`{}`中可以添加val/var/fun。该伴生对象只会加载一次。

#### 4.枚举类

* `enum class`声明枚举类。每个枚举值都是枚举类本身。所有的枚举类型必须保持一致。
* 枚举值可以是一个具有一定含义的字符串。也可以是同一种类型的类，通过枚举类的主构造函数传入枚举值对应的类对象

#### 5.密封类

* `sealed class`声明密封类。解决枚举类类型不能不一致的问题。

### 三、泛型

#### 1.如何声明泛型

* 声明一个泛型类：`class Generic<T>(val obj:T){}`
* 声明一个泛型方法：` fun <A,B> method(a:A,b:B){} `

#### 2.泛型约束

* 默认的泛型是不支持类型转换，即不能将子类的实例对象赋值给父类声明，也不能将父类的实例对象赋值给子类声明。不支持接收非本身的泛型。
* 协变out：解决子类实例不能赋值给父类声明。也可以让父类声明接收子类或者本身类型的变量。同时也限制了该泛型不能被修改，只能做函数的返回值。同java的`? extends`。
    - 在父类类型前添加 `out`。
* 逆变in：解决父类实例不能赋值给子类声明。也可以让一个子类声明接收父类或本身类型的变量。同时也限制了该泛型不能做函数的返回值，只能做函数的传入参数。同Java的`? super`.
    - 在子类类型前添加 `in`。

#### 3.类型擦除

* 默认的泛型在转换成字节码的时候，是不保留泛型的类型，这样是为了减少内存信息。
* 但是缺点也会引起无法检测该实例是否是某个具体类型。
* 可通过 `inline` + `reifid`来解决问题，如`inline fun <reified T : Activity> openActivity(clazz: Class<T>)`
    - 实现原理：`reifid`是可以让泛型被具化，保留该泛型的类型。而 `inline`是可以将内联函数的字节码直接拷贝到调用处。那么结合之后，每次都会把对应类型的字节码，动态插入到调用点。
* 在java中不能调用这种类型的方法。

#### 4.型变

* 声明处型变：在赋值操作的时候，无法自动完成类型的转换，会提示类型不匹配。可通过`in`和`out`来解决该问题。
* 使用处型变：作为函数的形参，无法自动完成类型的转换。可通过in`和`out`或者星投影的方式来解决该问题。

=============== 详细解析 ================

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

* TODO("...")

### 9.反引号函数

* 标记一些特殊含义的方法名
* 某些java方法恰好是kotlin的关键字，如`is`

[具体对应的类是KotlinContent.kt]

### 10.vararg

* 用来接收多个动态参数

``` 
class KotlinVararg<T>(vararg _objs: T, val isMap: Boolean) {
    /**
     * 创建objs来接收_objs。
     * 因为_objs是一个动态参数，需要通过out设置接收的这个objs只能可读，不能修改
     */
    var objs: Array<out T> = _objs
    
}    
```

那么此时的`_objs`就是一个动态参数，可以动态添加`_objs`中的内容。所以当在使用Array来接收该参数的时候，需要设置该成员变量为只读，即使用`out`来修饰该类型。

[具体对应的类是KotlinVararg.kt]

### 11.kotlin调用java

* 在kotlin调用java代码时，若是`String!`类型的时候，在使用的时候，必须采用`?.xxx`，并且添加类型限定符`String ?`。
* `@file:JvmName("NewName")`
    - 默认的会将`KotlinSpecial.kt`在生成字节码的时候，会自动生成`class KotlinSpecialKt{}`，供Java来调用。
    - 而`@file:JvmName("NewName")`在编译阶段，会将当前`KotlinSpecialKt`类的名字修改为`NewName`。在java中可直接通过`NewName`
      来访问`KotlinSpecialKt`
      类中的内容。
    - 但是要注意的是**该注解必须写在`package xxx`的前面**。
    - 但是如果在同一包名下的两个kt文件都被注解了相同的名字，在编译的时候会报错。通过在`@file:JvmName("NewName")`
      后添加`@file:JvmMultifileClass`，这样就会把这两个类合并到同一个类中。
* `@JvmField`
    - 可以val/var定义的成员变量，在java中直接调用，而不需要调用getXX()。
    - 对于`val nameField: String = "zhangsan"`，经过编译器生成的字节码中，代码如下：
    ``` 
     private static String nameField = "zhangsan";
     public static final String getNameField(){
        return nameField
     }
     public static final void setNameField(String var0){
        this.nameField = var0
     }
    ```
    - 那么对于调用`nameField`只能通过`getNameField()`，并且`getNameField()`用`final`来修饰表示不可修改。 但是如果添加`@JvmField`
      之后，经过编译器生成的字节码如下：
    ``` 
        public static String nameField = "zhangsan";
     ```
  并且不在含有get/set方法，那么此时就可以直接通过`.`的方式来调用该`nameField`。
* `@JvmOverloads`
    - kotlin中声明方法的时候，可以给定输入参数默认值。在调用该方法时，可以只传入非默认值的参数。但是java却不能直接调用，不传默认参数。
    - 通过在方法上添加`@JvmOverloads`，在生成字节码的时候，会进行方法重载，自动生成一个只有非默认参数的方法，供java来调用。
* ` @JvmStatic`
    - 对于kotlin的`companion object {}`伴生对象，kotlin可以直接通过`类名.`的方式直接调用。在生成字节码的时候，会将`companion`
      生成静态类，所以在java中调用的时候，只能`类名.Companion.`进行调用。
    - 通过对`companion object {}`里的成员变量添加`@JvmField`和对方法添加 ` @JvmStatic`
      。在生成字节码的时候，会将`companion object {}`所在类中生成对应方法的静态方法，在静态方法中去调用`类名.Companion.`，而java中直接通过`类名.`
      的方式直接调用。

[具体对应的类是KotlinToJava.kt]

### 11.infix 中缀表达式

* 通过在方法前添加`infix`得到一个中缀表达式,例如 `mapOf("key1" to "value1")`，这里的`to`就是有个中缀表达式，其源码实现如下：

``` 
public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)
```

* 中缀表达式的使用方式为[对象1 表达式符 对象2]
* 得到一个中缀表达式的前提条件：
    - 必须为成员函数或者扩展函数；
    - 必须只有一个参数
    - 参数不能为可变参数或者默认参数
* 一个中缀表达式的实现过程：
    - 必须对对象1进行扩展函数
    - 必须将对象2作为参数传入

``` 
data class FakePair<A, B>(var a: A, var b: B) {

}
//实现一个中缀表达式
infix fun <A, B> A.toPair(b: B): FakePair<A, B> =
    FakePair(this, b)
//具体的使用方式：
fun main() {
    val pair: FakePair<String, String> = "aaa" toPair "bbb"
    println(pair.toString())
}  
```

[具体对应的类是KotlinInfix.kt]

### 12.by 委托

* kotlin直接支持委托模式。通过 `by`实现委托。
* 类委托：一个类定义的方法实际是调用另一个类对象的方法来实现。

## 二、函数类型

在kotlin中可以将函数作为一种数据类型。数据类型的表示形式为`(输入参数的数据类型)->返回值的数据类型`，例如`(String,Int)->String`

### 1.将函数类型赋值给变量

像Int/String一样，可以直接将函数赋值给一个变量。有两种方式：

* (1)lambda表达式（本质就是一个匿名函数），其中最后一行代码为方法的返回值。
    - 其形式为`{ x:Int,y:Int(parameters) -> x+y(body)}`
    - lambda表达式如果作为函数的最后一个参数，那么就可以直接放在括号外面，并且可以省略括号。即`person.maxBy({ p:Person -> p.age })`
      可以简写为`person.maxBy{ p:Person -> p.age }`
    - lambda表达式作为形参声明时，可以携带接收者（也就是扩展函数）。即`String(接收者).(Int,Int)(参数类型)->Unit(返回值)`

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

## 4.高阶函数

当一个函数作为输入参数或者返回值时称为高阶函数。

## 5.invoke约定

可以让对象像函数一样直接调用。

``` 
class Person(val name:String){
    operator fun invoke(){
        println("my name is $name")
    }
} 
//使用对象
val person = Person()
person()
```

[具体对应的类是KotlinMethodType.kt]

## 三、数组和集合

### 1.基本定义

[具体对应的类是KotlinListArrayMap.kt]

### 2.List的几个扩展函数

* map
    - 持有的it：指向集合中的每个元素。
    - `map {}`返回值：一个新集合，该新集合的元素是`{}`中的最后一行代码的返回值。**所以一定要注意最后一行代码。**
    - 应用场景：对原集合元素进行包装得到一个新的元素；类型转换，例如将`List<String>`转换`List<Int>`。
* flatMap
    - 持有的it：指向集合中的每个元素。通常该函数用于嵌套集合，所以该元素仍然是一个集合。
    - `flatMap {}`返回值：一个新集合。`{}`要求最后一行代码必须返回一个集合，所以新集合的元素就是将最后一行代码必须返回一个集合中的元素添加到新集合中。
    - 应用场景：将嵌套关系的集合`List<List>`的元素的每个元素添加到一个新的集合`List`中,该新集合的每个元素就是之前嵌套集合的所有元素。

``` 
//原集合
val list = listOf(
    listOf("123", "456", "789", "1232132"),
    listOf("123abc", "456def", "789hij"),
    listOf("123qwe", "456asd", "789zxc")
)
//经过flatMap
val list1 = list.flatMap {
    //就是集合list的每个元素，仍然也是一个集合
    it
}
//得到的新的集合list1就是list中的每个元素
[123, 456, 789, 1232132, 123abc, 456def, 789hij, 123qwe, 456asd, 789zxc]
```  

* filter
    - 持有的it：就是集合中的每个元素。
    - `filter {}`返回值：一个新集合。`{}`要求最后一行代码必须返回一个`true`/`false`，根据`{}`最后一行是`true`来决定是否把it添加到新的集合中。
    - 应用场景：对集合中的元素进行过滤。
* zip
    - 作用：就是将集合1和集合2合并到一起，返回一个新的集合。该集合的元素是一个Pair类型
    - `zip {}`返回值：一个新集合。该集合是一个List<Pair>类型的集合。集合中的每个元素包含集合1和集合2的元素，通过`it.first`和`it.second`
      来获取集合1和集合2的元素。
    - 通过`toMap`将新集合转换成一个Map。

[具体对应的类是KotlinListExtension.kt]

## 四、类

### 1.类构造器

* 主构造器：跟在类名后面的()为主构造器.默认的为()，可省略。含有最多的输入参数。
    - (1)若输入变量为临时变量，可直接采用`_name:String`。该值只可在`init{}`中使用，在其他地方无法使用，需要定义成员变量`var name = _name`
      接收该临时变量才可以在类的其他地方使用。
    - (2)通过`var/val name:String`，可直接使用输入变量
    - (3)在`init{}`增加主构造函数的逻辑，转换成字节码只是`{}`代码块，非`static{}`静态代码块。
* 次构造器：通过`constructor():this()`声明次构造函数。最后通过主构造函数统一管理。
* 先调用主构造函数(在输入参数中的类成员变量) -> init{}(与主构造函数的类成员变量同时生成) -> 次构造函数

### 2.类的一些基本概念

* 类里面的代码按顺序执行，一般要将对变量赋值放在最前面。在使用变量之前，一定先要将变量进行赋值。
* 类都会继承Any，默认的final进行修饰，不能被继承。->通过`open class`来移除final。
* `对象 is class`：判断对象是不是class的对象。
* `对象 as class`：将对象转换成class对象。
* 懒加载有两种方式：
    - `lateinit`：用于var修饰的变量。在生命周期流程中进行获取或初始化变量。例如`lateinit var lazy1: String`，**在使用的时候必须进行手动初始化**。
    - `lazy()` ：用于val修饰的变量。惰性加载,在使用该变量的时候，会主动加载初始化变量的方法，即lambda表达式里面的内容。
        - 接收的是一个lambda表达式，并且最后一行代码必须返回一个Lazy<T>的实例函数。
        - 在构造函数中含有一个枚举mode：
            - `LazyThreadSafetyMode.SYNCHRONIZED`：初始化变量的时候，进行双重锁检查，保证该值只在一个线程中计算，并且所有的线程中都会得到相同的值。
            - `LazyThreadSafetyMode.PUBLICATION`：多个线程会同时执行，初始化属性的函数会被多次调用，但只有一个返回的值当作委托属性的值。
            - `LazyThreadSafetyMode.NONE`：没有双重锁检查，不能用在多线程。

```
    val lazy2 by lazy {
        lazyVar()
    }
    private fun lazyVar(): String {
        return "1222"
    }
    //含有mode的惰性加载
    val instance: SingletonLazySync by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { SingletonLazySync() }
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

* `object 类名`声明单例类(这个就是[饿汉模式])，只有一个该类的实例，此时`init{}`为静态代码块。在转换成字节码时，该类会通过`public static final`进行修饰。
* `companion object{}`声明伴生对象(这个就是[懒汉模式])，`{}`
  里面可定义val/var/fun。该伴生对象只会加载一次。在转换成字节码时，该类会通过`public static final`
  进行修饰。

``` 
class SingletonLazy {
    companion object {
        private var instance: SingletonLazy? = null
            get() {
                if (field == null) {
                    field = SingletonLazy()
                }
                return field
            }
        fun getInstanceAction() = instance!!
    }

    fun show() {
        println("show")
    }
}
//就可以直接调用单例
    SingletonLazy.getInstanceAction().show()
```  

* [懒汉安全模式] 只需要在上面的`getInstanceAction()`添加`@Synchronized`即可。
* [懒汉双重模式] java中在实例定义的时候添加`volatile`保证其他线程修改的时候，可以同步到另外线程+获取实例方法中添加`synchronized`
  保证每次只有一个线程可以修改。在kotlin中的代码如下：

``` 
class SingletonLazySync private constructor() {
    companion object {
        val instance: SingletonLazySync by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { SingletonLazySync() }
    }

    fun show(){
        println("show")
    }
}
//可以直接使用instance
SingletonLazySync.instance.show()
```  

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
* 可对里面的运算符进行重载，完成新的逻辑。通过`operator fun 运算符`的方式进行重载。可通过`operator fun 类.`找到要重载的运算符。
* 数据类的应用场景：
    - (1)服务器返回的响应JavaBean、ResponseBean。
    - (2)主构造函数至少有一个val/var修饰的输入参数。
    - (3)不能被abstract/open/sealed/inner等进行修饰，只能做数据存储。
    - (4)应用于需要进行比较的时候。可以方便的利用里面的具体方法。

[具体对应的类是KotlinClass.kt和KotlinSpecialClass.kt]

### 7.接口类和抽象类

* 同java一致，里面的成员变量和方法都是public open
* 接口中不能有主构造函数，也就是只能定义为`interface xxx{}`
* 实现接口的时候，里面的成员变量和方法都需要复写。成员变量在复写的时候，可通过实现类的主构造函数或者set/get进行复写。
* 接口类中的成员变量可以定义为val，但是一旦赋值不可修改。
* 抽象类同java。

[具体对应的类是KotlinInterface.kt]

### 8.枚举类：使用`enum`进行修饰

* 本身就是一个class。每个枚举值都是枚举类本身。即`枚举值 is 枚举类` 为true。所有的枚举值类型必须一致。
* 枚举值可以为一个常量字符串，例如：

```
enum class Week {
    星期一,
    星期二
}
```

* 可以通过主构造函数为枚举值赋值，例如：

```
enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00)
}
```

* 枚举值也可以为一个类。但是此时枚举类的主构造函数的输入参数就是该类枚举值类型。例如：

```
//定义的类的枚举值
data class Job(val content: String, val title: String) {
}
//定义的枚举类，输入参数必须为类枚举值
enum class Jobs(val job: Job) {
    DOCTOR(Job("看病", "doctor")),
    TEACHER(Job("教书", "teacher")),
    POLICE(Job("抓坏人", "police"));

    fun job() {
        println("in The job is ${job.title} , the content is ${job.content}")
    }
}
//使用枚举类
Jobs.DOCTOR.job()
```

### 9.密封类：使用`sealed`修饰。

* 可解决枚举类的枚举值类型不一致的问题，可对枚举值进行扩展，增加额外的参数。
* 具体实现方式就是：通过`object定义成员变量，并且还要继承本类`，例如：

```
//定义密封类
sealed class JobSealed {
    object DOCTOR : JobSealed()
    object TEACHER : JobSealed()
    class POLICE(val name: String) : JobSealed()
}
//使用密封类，需要扩展的枚举值通过主构造函数传入
class JobSummary(val job: JobSealed) {

    fun showJob() =
        when (job) {
            is JobSealed.DOCTOR -> "白衣天使"
            is JobSealed.TEACHER -> "教书育人"
            is JobSealed.POLICE -> "人民公仆 ${job.name}"
        }
}
println(JobSummary(JobSealed.POLICE("张三")).showJob())
```

[具体对应的类是KotlinSpecialClass.kt]

## 五、kotlin的内联扩展函数

### 1.内联扩展函数 let

* [持有it指向该对象]。
* 返回值类型[最后一行代码的类型]
* [应用场景] (1)与空合并符进行null逻辑处理，代替if；(2)明确该对象的作用范围。

``` 
    var result = name?.let {
        "这个名字为$it"
    } ?: "还没有起名字"
```

### 2.内联函数 with

* 并不是作为扩展函数，而是一个内联函数，对象是作为参数传入。
* 在函数体内[持有this指向对象]。即直接调用对象中的方法或成员变量。
* 返回值类型[最后一行代码的类型]
* [应用场景] (1)多次对同一对象进行修改。即同时调用同一个对象的多个方法。可以省去对象名的调用，直接在{}调用对象的方法即可。

``` 
      if(service == null) ?: return
      with(service){
        setName("xxx")
        doSome()
        ddd()
        sddd()
    }
```

* 可采用下面三种形式：
    - (1) `with(user,{ })`
    - (2) `with(user){ }`
    - (3) `with(user,::isLogin)`

### 3.内联扩展函数 run

* [持有this指向对象]。即直接调用对象中的方法或成员变量。
* 返回值类型[最后一行代码的类型]
* let和with的结合体。可直接通过run{}来省去if判断，同时在{}中可直接调用对象的成员变量和方法。
* [应用场景] (1)设定临时变量的作用域。(2)let/with的任何场景。

``` 
   service?.run{
        setName("xxx")
        doSome()
        ddd()
        sddd()
   }
```

### 4.内联扩展函数 apply

* [持有this指向对象]。即直接调用对象中的方法或成员变量。
* 返回值类型[对象本身]。
* 与run作用差不多，但是返回值类型不同。
* [应用场景] 对象实例化时需要对属性进行赋值。例如通过inflater出一个view时，需要给view进行绑定数据。特别是model向view model转化实例化过程。或者多层级判空问题

### 5.内联扩展函数 also

* [持有it指向对象]
* 返回值类型[对象本身]
* [应用场景]同let。

``` 
private fun also(reader: BufferedReader) {
    var line: String?;
    while (reader.readLine().also { line = it } != null) {
        println(line)
    }
}
```

### 6.内联扩展函数 takeIf和takeUnless

* [持有it指向对象]
* takeIf返回值类型{}中的最后一行为true，则返回对象本身。否则返回null。takeUnless恰好相反。
* [应用场景] 只需要单个if分支语句。配合其他扩展函数。（相当于在{}中放了一个if条件，如果满足该条件，则返回对象本身，可继续链式调用）

[具体对应的类是KotlinApplyLet.kt]

### 6.总结

* [apply和also]
    - [相同点] 都是返回对象本身
    - [不同点] 匿名函数持有对象不一样：apply持有的this；also持有的是it
    - [应用场景] 链式调用。apply持有this，可以直接调用其自身的方法和成员变量，用于对象初始化；
* [run和let]
    - [相同点] 都是返回的匿名函数的最后一行代码的类型
    - [不同点] 匿名函数持有对象不一样：run持有的this；let持有的是it
    - [应用场景]let与空合并符进行null逻辑处理，代替if。run限定作用域
* [with]
    - 同run，只不过是需要将对象传入到with

### 7.自定义内置函数

* T.()：为T的扩展函数，那么就可以在该扩展函数体内直接访问T的成员变量和成员函数。那么在函数体可以直接使用this
* (T)：就是一个T类型。在函数体内持有it
* ()：一个普通的函数，在函数体内只能通过T的外部变量来访问其变量和函数。
* 自定义let。其特点：持有的是it，返回的是匿名函数的最后一行代码。思路如下：
    - 对I的扩展函数 -> 定义为`fun I.mLet()`。
    - 传入的lambda表达式需要具备：持有的是it，返回值为最后一行代码的类型 -> 定义输入参数为`lambda: (I) -> O`。
    - 采用了lambda表达式作为传入参数，为了避免创建额外的对象 -> 该函数使用`inline`进行修饰。
    - 由于调用let的时候需要返回匿名函数的最后一行代码的返回值 -> 直接采用lambda的返回值。

``` 
private inline fun <I, O> I.mLet(llambda: (I) -> O) = lambda(this)
```  

* 自定义apply。奇特的：持有this，返回的对象本身。思路如下：
    - 对I的扩展函数 -> 定义为` fun I.mApply()`
    - 传入的lambda表达式需要具备：持有this，无需返回值。`mApply()`
      返回的是对象本身，并不是该lambda表达式返回值。另外这个lambda表达式要能够直接访问到其对象的成员变量和函数，所以需要将输入参数定义为一个扩展函数 ->
      定义输入参数为`lambda: I.() -> Unit`。
    - 采用了lambda表达式作为传入参数，为了避免创建额外的对象 -> 该函数使用`inline`进行修饰。
    - 由于调用apply的时候需要返回对象本身 -> 该`mApply()`直接返回this。

``` 
private inline fun <I> I.mApply(llambda: I.() -> Unit): I {
    //lambda(this) //默认就有this，所以可以直接不添加this
    lambda()
    return this
}
```  

[具体对应的类是KotlinInline.kt]

## 六、泛型

### 1.声明泛型类

* `class Generic<T> (val obj:T)`

### 2.声明泛型方法

* 只有一个泛型：`fun <B> show(item : B)`
* 多个泛型：`fun <O, R> map(isMap: Boolean, input2: R, mapAction1: (T, R) -> O)`

### 3.泛型约束

* 指定泛型上界，可通过`fun <B : String> show(item: B)限制上界范围`
* 协变out，使用out修饰的泛型[作用一] 表示该泛型只能做返回值，不能作为输入参数进行修改。可以这样[记忆out的作用： 返回值 -> output -> out]

``` 
    interface Producer<out T> {
        //这样是可以的
       fun producer(): T
        //不能这样操作，直接编译不通过。
        fun consumer(t:T)
    }
    
``` 

* 协变out，使用out修饰的泛型[作用二]表示该泛型可以接收其子类或者本身，相当于java的`? extends``(在java中也是只能返回值,不能修改)。上例中的T只能接收子类或本身。
  有两个类父类Animal和子类Dog，那么在实现上面的Producer<out T>接口的时候，该泛型支持如下操作：

``` 
    open class Animal {
    }
    class Dog : Animal() {
    }
    class ProducerClass : Producer<Animal>{
    }
    class ProducerDogClass : Producer<Dog> {
    }

```

* 协变out，使用out修饰的泛型[作用三]表示泛型具体的子类对象 可以赋值给
  父类声明。默认情况下，不支持将泛型一个子类赋值给一个父类的声明。可以这样[记忆out的作用：本身一个子类就可以赋值给一个父类，所以为协变]

``` 
    val p1: Producer<Animal> = ProducerClass()
    val p2: Producer<Animal> = ProducerDogClass()
```

* 逆变in,使用in修饰的泛型[作用一]表示泛型只能做函数的参数，不能做返回值。可以这样[记忆in的作用：输入参数 -> input -> in(反过来就是逆变的拼音)]

``` 
interface Consumer<in T> {
     //不能这样操作，直接编译不通过。
     fun producer(): T
     //这样是可以的
     fun consumer(t: T)
}
```

* 逆变in,使用in修饰的泛型[作用二]表示该泛型可以接收父类或其本身，相当于java的`? super`(在java中也是只能修改,不能返回)。上例的T只能接收父类或本身。
* 逆变in,使用in修饰的泛型[作用三]
  表示泛型的父类对象可以赋值给子类的声明。默认情况下泛型的一个具体的父类实现是不能赋值给一个子类声明的。可以这样[记忆in的作用：本身一个父类是不可以赋值给一个父类，而这里通过in改变了这个理论，称为逆变]

``` 
val c3: Consumer<Dog> = ConsumerClass()
```

* 大胆总结下：out/in都是[为了让泛型可以将子类或者父类具体的实现 赋值给 父类或者子类的声明，前者是out，后者是int]，但差别在于out是[限制了该泛型只能做函数的返回值]
  ，相当于java的`? extends`，此时传入的泛型[必须为T的子类或本身] ；而in是[限制了该泛型只能做函数的输入参数]，相当于java的`? super`
  ，此时传入的泛型[必须为T的父类或本身]。
* 若是该泛型既可以做函数返回值又可以做函数的输入参数，那么就不要添加任何限制。

### 4.类型擦除

* 在使用泛型的时候，例如`Array<T>`当创建的`Array<String>`、`Array<Int>`实例时，在运行的时候都会被擦除为`Array<*>`。
* [好处]可以减少内存中的类型信息。
* [缺点]在运行时无法检测该泛型实例是否是某个具体类型，无法保留泛型的任何信息。
* [解决方案]通过`inline+reified`来保留泛型的具体类型。
* [原理]`inline`可以直接将内联函数的字节码动态插入到调用处。`reified`可以将泛型被具化，保留该泛型类型。那么两者结合之后，编译器在每次调用时生成对应类型的字节码，动态插入到调用点。
* [带来的问题]在java中不能调用实化类型参数的函数。

### 5.型变

泛型是不型变的，也就是无法自动完成类型的转换。

* [(1) 声明处型变]指的是在赋值操作的时候，无法完成类型的转换。例如无法直接将子类的泛型实例赋值给父类的泛型实例，这个赋值操作本质上是合理、安全的。

``` 
    var dogs: Array<Dog> = arrayOf(Dog(), Dog(), Dog())
    var animals: Array<Animal> = dogs
```

* 上面的代码直接会有错误提示：类型不匹配。
* Array<T>是不可以直接类型转换，但是在使用List、Set、Map的时候，是可以直接完成上面的类似赋值操作。原因在于List在定义的时候就已经让泛型支持协变，代码如下：

``` 
  public interface List<out E> : Collection<E>
```

* 所以针对上面的代码错误，可以直接在声明的时候`var animals: Array<out Animal> = dogs`。那么该`animals`也就只能作为函数的返回值，不能进行修改。
* 针对无法将父类的泛型实例赋值给父类的泛型实例，可以使用`in`来解决该问题。
* [声明处型变] ----------------- end ----------------
* [(2) 使用处型变]指的是在作为函数的传入参数的时候，无法完成类型的转换。同样也是通过`in`和`out`来解决。
* 使用处型变也是一种[类型投影 1]
* [使用处型变] ----------------- end ----------------
* [类型投影 2] 星投影被称为另外一种类型投影，用`*`来表示
* 适用于泛型参数的类型不重要的场景
* [类型投影] ----------------- end ----------------

[具体对应的类是KotlinGenericType.kt]

## 七、扩展函数和扩展属性

### 1.扩展函数

* [应用场景] 可以比较方便在开源框架或者已有的系统类的基础上动态增加方法。
* [定义方式] `fun 类名.方法名`
* [优先级]
    - 若重复定义类中已有的方法，则覆盖已有的方法，并且优先使用。
    - 若同时添加相同名字的扩展函数，则编译不通过，此种情况不允许。
    - 可以对超类Any进行增加扩展函数，那么所有的类都会增加相应的功能，尽量减少这种方式。

``` 
fun StringBuffer.length(): Int {
    println(this)
    return 3
}
```  

* 如果还想对一系列的类都增加扩展函数，那么可以采用通过对泛型增加扩展函数。

```
fun <T> T.show() = println("${if (this is String) "是String" else " 不是String"}")
```

* 这里有个小提示：哪个类或者变量执行 `.`操作，那么后面引用的`this`就是这个类或者变量

### 2.扩展属性

* [应用场景] 可以比较方便在开源框架或者已有的系统类的基础上动态增加成员变量
* [定义方式] `val/var 类名.成员变量:数据类型 get() = `

``` 
val String.name: String
    get() = "123"
```

### 3.对可空类型的扩展函数

* [定义方式] `fun 类名?.方法名`

[具体对应的类是KotlinExtension.kt]

### 4.扩展文件

* 将所有的扩展函数单独写到一个文件中，在使用这些扩展函数的时候将文件导入直接使用。
* 一般里面的扩展函数都是public，否则无法调用到。
* 在写扩展函数的时候，尽量对父类进行扩展，方便子类都能使用该扩展函数。
* 在使用扩展文件的时候，若不在同一个包下面，需要导入，例如

```
import com.wj.kotlin.randomItemValuePrintln
```

* 支持重命名扩展，可以使得代码更简洁，例如：

```
import com.wj.kotlin.randomItemValuePrintln as r
```

这样可以直接使用`r`代替`randomItemValuePrintln`扩展函数，如下：

```  
    //list.randomItemValuePrintln()
    list.r()
```

[具体对应的类是KotlinExtension.kt和KotlinExtensionFile.kt]

## 八、协程

### 1.线程和进程

* 一个APP应用就是一个进程，一个进程中有多个线程。
* 一个线程有5种状态：初始化、可运行、运行中、阻塞和销毁，其中可运行、运行中、阻塞状态可以相互转换。

### 2.线程之间的协作-生产者-消费者模式

* 线程之间的协作涉及下面几部分，每个状态都非常耗费性能：
    - 同步锁
    - 线程阻塞状态和可运行状态切换
    - 线程上下文切换
* 线程之间的调度是有系统完成，切换或阻塞开销比较大。

### 3.协程 coroutine

* 线程框架。**使异步任务可以同步代码的调用方式**。多任务并发的操作手段。
* 本质就是在编译阶段通过插入相关代码使得代码段可以分段执行。
* 一个线程上可以有多个协程。每个协程就是一个耗时任务。协程依赖于线程，但协程挂起的时候无需阻塞线程，有开发者自行控制。
* 相对于线程优势：
    - 协程之间的切换不涉及到线程上下文、状态的切换，不存在资源、数据并发，无需加锁，只要状态切换即可。
    - 协程是非阻塞(也有阻塞API)，一个协程在进入到阻塞时，并不是阻塞当前线程，而是将当前线程去执行其他任务。
    - 开发人员控制协程的切换。协程在空闲的时候（如等待IO、网络数据未到达），放弃控制权，然后让所在线程去执行其他协程，在合适的时间在唤醒协程。
      相比较于线程是通过阻塞当前线程，此时只是空耗CPU，而不执行任何计算任务，造成浪费。

### 4.协程的挂起

* 通过对函数前添加`suspend`修饰符仅是提醒该函数需要挂起，里面要配合`withContext()`来实现一个挂起函数。
* 该挂起函数可以自动的切换线程，并且可以自动的挂起和恢复。
    - 每一次有主线程到IO线程，都是一次协程挂起suspend；每一次的IO线程到主线程，都是一次协程的恢复resume。
    - 挂起只是将执行流程转移到了其他线程，主线程并未被阻塞。
* 如果挂起函数及里面的调用的函数都必须是挂起函数。

### 5.协程上下文CoroutineContext

* Job(协程唯一标识)+CoroutineDispatcher(调度器)+ContinuationInterceptor(拦截器)+CoroutineName(协程名称)

### 6.协程作用域

* 协程必须在作用域中才能启动。作用域中定义了一些父子协程的规则，kotlin协程通过作用域来管控域中的所有协程。
    - 顶级作用域：没有父协程的协程所在的作用域。
    - 协同作用域：协程中启动新协程(子协程)，此时子协程所在的作用域默认为协同作用域，子协程抛出的未捕获异常都将传递给父协程处理，父协程同时也会被取消；
    - 主从作用域：与协同作用域父子关系一致，区别在于子协程出现未捕获异常时不会向上传递给父协程。
* 父子协程间的规则：
    - 父协程被取消，所有的子协程均被取消；
    - 父协程只有等子协程执行完毕后才会进入最终完成状态，而不管父协程本身的协程体是否已经执行完。
    - 子协程会继承父协程上下文的元素，如果有相同的key，则被覆盖。
* 在实际开发中，很少需要一个全局协程

### 7.协程取消

*

### 8.如何创建一个协程

* `GlobalScope.launch`创建一个[全局的协程]。该`GlobalScope`为单例，声明周期贯穿整个JVM，注意内存泄漏
* `runBlocking {}`创建一个[阻塞的协程]。阻塞当前线程，知道里面的代码执行完毕。等价于`Thread.sleep`
* 继承`CoroutineScope`创建一个[自定义的作用域]。只需要实现里面的`coroutineContext`成员变量即可。如

``` 
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext
```

* 通过`val mainScope = MainScope()`快速创建[基于Android主线程协程的作用域]。可以方便的控制所有它范围内的协程的取消。

``` 
public fun MainScope(): CoroutineScope = ContextScope(SupervisorJob() + Dispatchers.Main)
```

默认的会创建一个`SupervisorJob()` 和 `Dispatchers.Main`
类型的CoroutineContext，如果还想在为CoroutineContext添加其他的元素，可通过如下的方式`val scope = MainScope() + CoroutineName("MyActivity").`

* 通过 `coroutineScope {}``supervisorScope {}`创建[子作用域]。**只能在已有的协程作用域内调用**
  。前者在子作用域中出现异常，父协程和子协程都会被取消。而后者出现异常的时候不会影响到其他的子协程。
* 通过[协程作用域函数]来创建协程。协程作用域确定协程间的父子关系以及取消或者异常处理。
    - 创建不阻塞的当前线程的协程
        - `launch{}`：返回一个`Job`，用于协程监督与取消，用于无返回值的场景。
        - `async{ return@async  xxxx }`：返回一个`Job`的子类`Deferred`，可通过`await()`获取完成时的返回值。
    - 创建阻塞当前线程的协程

### 9.Job(作业)->创建的协程返回值、协程的工作任务

* `Job`的生命周期包括：New(新建)、Active(活跃)、Completing(完成中)(await children)、Completed(已完成)、Cancelling(取消中)(
  await children)、Cancelled(已取消)
    - 协程状态相关的API
        - `isActive`：是否存活
        - `isCancelled`：是否取消
        - `isCompleted`:是否完成
        - `children`：所有子作业
    - 协程控制相关API
        - `cancel()`：取消协程。
            - 同一作用域中，会取消它的所有子协程。但不会影响到其余兄弟协程。当该作用域已经取消之后，则不能再次启动新的协程。
            - 子线程会通过抛出`CancellationException`异常而被取消，父协程是不需要任何额外操作。默认的会创建一个`CancellationException`
              ，也可以新建传入。
            - 执行取消操作之后，不会立即停止，进入到取消中，只有任务完成才会变成已取消。所以在协程中要定期检查协程是否处于`isActive`
        - `join()`：阻塞当前线程直到协程执行完毕
        - `cancelAndJoin()`：取消并等待协程完成
        - `cancelChildren()`：取消所有的子协程
        - `attachChild(child:ChildJob)`：附加一个子协程到当前协程上
* 对于Completing或Cancelling，会等着所有的子协程完成后，才会进入已完成或已取消状态
    
  
    


