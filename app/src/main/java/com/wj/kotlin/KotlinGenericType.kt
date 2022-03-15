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
 * 3.类型限定
 * 传入的R只能是Animal或其子类，并且实现了Fly接口.这里的限定
 * fun <R> show1(item: R) where R : Animal, R : Fly
 * 3.int/out
 * 针对泛型，默认情况下子类不可以赋值给父类，
 * 但是通过out/?extends来修饰，就才可以将子类对象赋值给声明处的父类
 * 协变-out: 相当于java的? extends。该类型的参数只能做返回值，不能进行修改。
 * 该类型必须是子类或者本身（上界约束）
 * 父类泛型声明处是可以接收子类具体实现类
 * 应用场景：将泛型作为方法返回，output -> out。
 *
 * 逆变-in（下界约束）: 相当于java的 ? super。该类型的参数只能做输入参数，不能返回
 * 该类型必须是父类或者本身
 * 应用场景：传入泛型作为函数的参数，input -> in
 * 不变-既不用in又不用out：既可以作为函数参数也可以作为返回值
 *
 */

interface Producer<out T> {
    fun producer(): T
    //fun consumer(t:T)
}

interface Consumer<in T> {
    // fun producer(): T
    fun consumer(t: T)
}

open class Animal {

}

class Dog : Animal() {

}

class ProducerClass : Producer<Animal> {
    override fun producer(): Animal {
        return Animal()
    }
}

class ProducerDogClass : Producer<Dog> {
    override fun producer(): Dog {
        return Dog()
    }

}

interface Fly {

}


fun out() {

    //默认的是不能将父类对象赋值给一个子类声明
    //var animal:Dog = Animal()

    //接口类对象 = 实现类
    val p1: Producer<Animal> = ProducerClass()
    val p2: Producer<Animal> = ProducerDogClass()
}

//class Generic1<out T>(val animal: T) {
//    fun toAnimal(): T {
//        return animal
//    }
//    fun getAnimal(animal: T){
//
//    }
//}

fun <R> show1(item: R) where R : Animal, R : Fly {

}

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
