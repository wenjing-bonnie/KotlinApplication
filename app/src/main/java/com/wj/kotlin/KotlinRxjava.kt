package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/17
 *
 * 使用kotlin来实现Rxjava
 */
/**
 *
//上游 订阅 下游
// 起点 上游  可以先不用管，用 create 创建了一个 Observable
Observable.create(new ObservableOnSubscribe<Integer>() {
//发射器 发射事件
@Override
public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

}
})
// 订阅
.subscribe(
// 终点 下游 也可以不用管，new 了一个 Observer
new Observer<Integer>() {
@Override
public void onSubscribe(Disposable d) {
}

@Override
public void onNext(Integer integer) {
}

@Override
public void onError(Throwable e) {
}

@Override
public void onComplete() {
}
});
 */
/**
 * 中转站，保持我们的记录。用来记录传递的值create{}中的返回值
 */
class RxJavaCoreClassObject<T>(var itemValue: T)

/**
 * 实现功能要求：create是输入源，不需要任何参数，只需要将最后一行的内容返回即可
 */
//inline fun <O> create(createAction: () -> O): RxJavaCoreClassObject<O> {
//    //将createAction这个lambda表达式的返回值赋值给了中转站
//    return RxJavaCoreClassObject(createAction())
//}
/**
 * 简化的代码如下：
 * 1.方法体只有一行代码，所以可以去掉{ return }，直接用 =
 * 2.类型推断，所以直接去掉返回值的类型: RxJavaCoreClassObject<O>
 */
inline fun <O> create(createAction: () -> O) =
    //将createAction这个lambda表达式的返回值赋值给了中转站
    RxJavaCoreClassObject(createAction())

/**
 * 实现功能要求：输入源就是create的返回值，也就是传入到中转类的itemValue。
 * 那么解决方案就是直接对中转类进行扩展，从而直接调用扩展类的成员方法和成员变量
 * 输出即 map本身处理之后的内容即可
 * 因为要持续对RxJavaCoreClassObject进行操作，那么就必须返回RxJavaCoreClassObject
 */
//inline fun <I, O> RxJavaCoreClassObject<I>.map(mapAction: I.() -> O): RxJavaCoreClassObject<O> {
////因为是对RxJavaCoreClassObject的扩展函数，所以可以直接调用RxJavaCoreClassObject里面的成员变量
//    return RxJavaCoreClassObject(mapAction(itemValue))
//}

/**
 * 简化之后的代码：
 */
inline fun <I, O> RxJavaCoreClassObject<I>.map(mapAction: I.() -> O) =
    RxJavaCoreClassObject(mapAction(itemValue))

/**
 * 只要把上面输入的内容，打印输出即可
 * 输入：同样还是对中转站的itemValue进行处理
 */
inline fun <I> RxJavaCoreClassObject<I>.observer(observerAction: I.() -> Unit): Unit {
    return observerAction(itemValue)
}


fun main() {
    //这样create返回了中转站RxJavaCoreClassObject，后面可以持续对该类继续操作
    create {
        "1234324"
    }.map {
        "[${this}]"
    }.observer {
        println("${this}")
    }


}