package com.example.myproject.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * BaseFragment 基类
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    // 1. 声明内部可空的 _binding
    private var _binding: VB? = null

    // 2. 暴露给子类使用的只读 binding（仅在 onCreateView 到 onDestroyView 之间有效）
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 3. 让子类提供具体的 Binding 实例
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 4. 规范化初始化流程
        initView()
        initData()
    }

    /**
     * 抽象方法：子类需实现并返回对应的 Binding 对象
     */
    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /**
     * 子类在此初始化 UI 逻辑
     */
    abstract fun initView()

    /**
     * 子类在此加载数据或观察 LiveData
     */
    open fun initData() {
        // 可选实现
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 5. 核心：必须在视图销毁时置空，防止内存泄漏
        _binding = null
    }
}