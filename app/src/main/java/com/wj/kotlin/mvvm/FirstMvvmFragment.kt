package com.wj.kotlin.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.wj.kotlin.databinding.FragmentFirstMvvmBinding

/**
 * create by wenjing.liu at 2022/3/27
 */
class FirstMvvmFragment : Fragment() {

    private var _binding: FragmentFirstMvvmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FirstMvvmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("Fragment onCreateView = " + viewModel.hashCode())
        _binding = FragmentFirstMvvmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Fragment onCreate = ")
    }

    override fun onResume() {
        super.onResume()
        println("Fragment onResume = ")
    }
}