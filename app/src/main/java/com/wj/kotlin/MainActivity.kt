package com.wj.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.tv_test)
        textView.setOnClickListener { view: View ->
            view.getTag()
        }
        mainScope()
        // val result = mainScope().await()
    }


    private fun mainScope(): Deferred<String> {

        /**
         * 主线程的 这个只能应用于Android的
         */
        launch {
            println("mainScope:  ${Thread.currentThread().name}")
            launch(Dispatchers.IO) {
                println("in scope :  ${Thread.currentThread().name}")
                delay(1000)
            }
            delay(2000)
            //创建一个子作用域。只能在已有的协程作用域中调用。出现异常时会抛出异常（父协程和子协程都会被取消）
            coroutineScope {
                println("coroutineScope :  ${Thread.currentThread().name}")
            }
            //出现异常的时候不会影响到其他子协程
            supervisorScope {
                println("supervisorScope :  ${Thread.currentThread().name}")
            }

        }

        return async {
            return@async "133e"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}