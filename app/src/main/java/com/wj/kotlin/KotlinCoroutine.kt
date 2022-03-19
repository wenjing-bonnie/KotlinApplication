package com.wj.kotlin

import android.provider.Settings
import kotlinx.coroutines.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * created by wenjing.liu at 2022/3/16
 *1.进程和线程
 * 进程：拥有代码和打开的文件资源、数据资源、独立的内存空间。
 * 线程：程序的实际执行者。一个进程中至少包含一个主线程，也可以有更多的子线程。线程拥有独立的栈空间。
 *
 * 一个线程有5中状态：初始化、可运行、运行中、阻塞及销毁。可运行、运行中、阻塞三个状态可以相互转换。
 * 改变线程之间的状态是需要耗费一定的CPU资源。所以可以线程池来避免频繁创建线程和线程管理。
 *
 * 2. 线程之间是如何协作？看下线程之间的生产者-消费者模式
 * 涉及到同步锁
 * 涉及线程阻塞状态和可运行状态之间的切换
 * 涉及到线程上下文切换
 * 任何一点，都是非常耗费性能操作。
 *
 * 3.coroutine [kang ru ting]
 * 协程 ：微线程。新的多任务并发的操作手段。 处理耗时操作。
 * 特点：运行在单线程中的并发程序，即在单线程上有开发人员自己调度运行的并行计算。一个线程可以有多个协程。
 * 优点：省去多线程切换时带来线程上下文切换、线程状态切换、线程初始化的性能损耗。
 *  线程是有系统调度，线程切换或线程阻塞的开销都比较大。而协程依赖于线程，但是协程挂起的时候不需要阻塞线程，有开发者控制。
 *  一个线程可以创建任意个协程。
 *  本质上是编译阶段通过插入相关的代码使得代码段能够分段式执行。
 * 4.线程和协程对比
 * (1)Thread：有独立的栈、局部变量、基于进程的共享内存，因此数据共享比较方便。但是多线程时需要加锁控制，过多锁容易出现死锁。
 * 线程之间的调度由内核控制。
 * (2)Coroutine:线程上的优化产物，拥有自己的栈空间和局部变量，共享成员变量。
 * 直接标记方法，由开发人员自己切换、调度。一个线程上可以同时跑多个协程，同一时间只有一个协程被执行。（这个就是并发思想）=>也就是异步编程的同步写法。
 * 在单线程中模拟多线程并发，协程何时运行、暂停有开发人员自行决定。
 * 优势：
 *  1）在同一线程中执行，协程之间切换不涉及线程上下文切换和线程状态改变，不存在资源、数据并发，无需加锁，只要切换状态即可，执行效率比多线程高很多
 *  2）协程是非阻塞（也有阻塞API），一个协程在进入阻塞后不会阻塞当前线程，而是当前线程去执行其他的任务。
 *  3）开发人员控制协程的切换，yield让协程在空闲时放弃执行权（例如等待io，网络数据未到达），然后在合适的时间在resume唤醒协程继续执行。
 *  协程一旦开始运行就不会结束，直到遇到yield交出执行权。
 *
 *  5。kotiin中的协程
 *  将复杂性放入库来简化异步编程。该库可以让用户的代码的相关部分包装为回调、订阅相关事件、在不同线程上调度。
 *  总结几点：
 *  协程是运行在线程上的，一个线程可以同时跑多个协程，每个协程代表一个耗时任务。
 *  开发人员手动控制多个协程之间的运行、切换，而不是像Thread交给系统内核来操作去竞争CPU时间片。
 *  协程在线程中是顺序执行的。
 *  Thread是通过阻塞当前线程（此时只是空耗CPU时间，而不执行任何计算任务，造成浪费）、唤醒。
 *  而协程不会阻塞线程，而是线程在执行某个协程的挂起请求后，会去执行其他计算任务，比如其他协程。
 *
 *
 *
 * 线程框架
 * 让异步并发任务，像同步调用一样
 *
 *
 * 挂起：是指可以自动切回来的线程切换。
 * suspend ：是一个耗时操作，需要自动放到协程里处理，必须去调用别的挂起函数来。里面的函数也都要用suspend来修饰
 *
 * suspend是个提醒作用，告诉你现在这个函数需要挂起
 * suspend ，然后在代码里面添加withContext来实现一个挂起函数
 * 该挂起函数：要完成线程切换：每一次有主线程到IO线程，都是一次协程挂起（suspend）
 *  每一次从IO线程到主线程，都是一次协程恢复健康(resume)
 *  挂起和恢复是挂起函数特有的能力。普通函数是不具备的。挂起只是将线程执行流程转移到其他线程，主线程并未被阻塞
 *
 * 在java中耗时操作
 * 1）开子线程 处理耗时操作，通过回调返回
 * 有子线程就会有回调嵌套的问题。
 *2）Rxjava
 * 操作符 很难
 *
 * 协程可以同时处理多个任务，不需要同时切换线程。？？？
 * 并发/并行：在同一时间做多个任务
 * 并发：同时处理很多事情
 * 并行：同时执行很多事情。
 *
 *
 * 1.上下文：Job(协程的唯一标识)+CoroutinesDispatcher(调度器)+ContinuationInterceptor(拦截器)+CoroutineName(协程名称)
 *
 *
 */

/**
 * first [kang ru ting]
 */
private fun first() {
    println("即将进入第一个coroutine:  ${Thread.currentThread().name}")
    //创建一个非阻塞的协程 为单例对象，生命周期贯穿整个JVM，警惕内存泄漏
    // public object GlobalScope : CoroutineScope
    val job = GlobalScope.launch {
        println("进入第一个coroutine:  ${Thread.currentThread().name}")
        println("${coroutineContext.job}")
        delay(1000)
        println("结束第一个coroutine:  ${Thread.currentThread().name}")

        coroutineScope {
            launch {
                println("进入coroutineScope :  ${Thread.currentThread().name}")
            }
        }
    }
    job.children.map {
        println("子协程为：${it}")
    }

    println("子协程个数为：${job.children.count()}")
    println("跳出了第一个coroutine:  ${Thread.currentThread().name}")
    //  Thread.sleep(2000)
    //阻塞当前线程的协程，会等着里面的代码执行完毕。    等价于Thread.sleep(2000)
    runBlocking {
        println("进入阻塞:  ${Thread.currentThread().name}")
        delay(2000)
    }
    println("休眠结束")
}

class LogInterceptor : ContinuationInterceptor {
    override val key = ContinuationInterceptor
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return object : Continuation<T> {
            override val context: CoroutineContext
                get() = continuation.context

            override fun resumeWith(result: Result<T>) {
                println("result is ${result}")
                continuation.resumeWith(result)
            }

        }
    }
}

private fun lazy() = runBlocking {
    println("1")
    val job = launch(start = CoroutineStart.LAZY) {
        println("2")
    }
    println("3")
    delay(1000)
    println("4")
    job.cancel()

    val supervisorJob = launch(CoroutineName("123") + SupervisorJob()) {

    }

    val job2 = launch(Dispatchers.IO) { }
}

private fun dispatcher() {
    //自定义线程池为调度器实现。这样就会出现线程切换
    Executors.newScheduledThreadPool(10).asCoroutineDispatcher()
        .use { dispatcher ->
            GlobalScope.launch(dispatcher) {

            }
        }
    //但可以使用下面的方式避免线程切换
    Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        .use { dispatcher ->
            GlobalScope.launch(dispatcher) {
            }
        }
    //更简单的API
    GlobalScope.launch(newSingleThreadContext("Dispatcher")) {

    }

}

private fun interceptor() {
    GlobalScope.launch(LogInterceptor()) {
        println("===  interceptor 1 === ")
        delay(1000)
        println("===  interceptor  2 === ")
    }
}


private suspend fun loadUserFromServer(): String {
    var user = ""
    //with
    return user
}

fun main() {
    first()
    println()
    lazy()

    println()
    interceptor()
}