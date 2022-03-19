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
        //CoroutineStart.DEFAULT 默认的立即创建一个协程
        val job = launch(start = CoroutineStart.LAZY) {
            println("${this::class.javaObjectType}")
            launchJob()
            val result = ayncLaunchJob().await()
            println("return result is $result")
            coroutineScope()
            supervisorScope()

        }
        job.start()
        println("the job  ${job.children.count()}")
    }

    private suspend fun coroutineScope() = coroutineScope {
        launch {
            println("coroutineScope :  ${Thread.currentThread().name}")
        }
    }

    private suspend fun supervisorScope() = supervisorScope {
        launch {
            println("supervisorScope :  ${Thread.currentThread().name}")
        }
    }



    private suspend fun launchJob() = launch(Dispatchers.IO) {
        // withContext() {
        println("开启一个 launchJob 协程")
        delay(1000)
        println("launchJob ${Thread.currentThread().name}")
        //  }
    }

    private suspend fun ayncLaunchJob() = async(Dispatchers.IO) {
        println("开启一个 ayncLaunchJob 协程")
        delay(2000)
        println("ayncLaunchJob ${Thread.currentThread().name}")
        return@async "returnResult"
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}