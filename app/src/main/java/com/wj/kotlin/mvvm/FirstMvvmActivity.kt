package com.wj.kotlin.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wj.kotlin.R
import com.wj.kotlin.databinding.ActivityFirstMvvmBinding

class FirstMvvmActivity : AppCompatActivity() {
    lateinit var viewModel: FirstMvvmViewModel

    // viewModels()
    private lateinit var binding: ActivityFirstMvvmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[FirstMvvmViewModel::class.java]
        binding = ActivityFirstMvvmBinding.inflate(layoutInflater)
        //需要将binding.root传入到setContentView中，让Activity绑定布局。
        //一定不要在传入布局id，否则会添加多次，同时监听器也会添加到错误的布局对象中。
        setContentView(binding.root)
        //在使用控件的时候，可以直接使用Binding对象中的属性，无需findViewById
        binding.tvText.setText("1232")
        //viewModel

        val observer = Observer<String> {
            binding.tvText.setText(it)
        }
        viewModel.currentName.observe(this, observer)
    }
}