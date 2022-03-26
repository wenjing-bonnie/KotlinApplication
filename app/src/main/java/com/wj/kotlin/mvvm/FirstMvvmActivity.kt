package com.wj.kotlin.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.*
import com.wj.kotlin.R
import com.wj.kotlin.databinding.ActivityFirstMvvmBinding
import kotlinx.coroutines.launch

class FirstMvvmActivity : AppCompatActivity() {
    lateinit var viewModel: FirstMvvmViewModel

    // viewModels()
    private lateinit var binding: ActivityFirstMvvmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //1.初始化ViewModel，用来为页面UI渲染获取数据
        viewModel = ViewModelProvider(this)[FirstMvvmViewModel::class.java]
        //2.初始化ViewBinding，用来实例化控件，可以直接通过"binding."的方式调用控件
        binding = ActivityFirstMvvmBinding.inflate(layoutInflater)
        //需要将binding.root传入到setContentView中，让Activity绑定布局。
        //一定不要在传入布局id，否则会添加多次，同时监听器也会添加到错误的布局对象中。
        setContentView(binding.root)
        //在使用控件的时候，可以直接使用Binding对象中的属性，无需findViewById
        binding.tvText.setText("初始赋值")
        //3.设置生命周期的监听，可以将与生命周期相关的内容转移到Observer中进行处理
        lifecycle.addObserver(FirstLifecycleObserver())
        //4.为viewModel的LiveData设置Observer，可以在被观察对象发生变化的时候，回调到onChanged，从而渲染UI
//        val observer = object:Observer<String>{
//            override fun onChanged(it: String?) {
//                binding.tvText.setText(it)
//            }
//        }
        val observer = Observer<String> { name ->
            println("变化了的name为：$name")
            binding.tvText.setText(name)
        }
        viewModel.currentName.observe(this, observer)

        binding.btnButton.setOnClickListener {
            btnClickListener()
        }




        lifecycle.coroutineScope.launch { }
        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }
    }


    private fun btnClickListener() {
        println("点击事件了......")
        viewModel.loadUserFromServer()
    }

}