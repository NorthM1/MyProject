package com.example.myproject.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.myproject.base.BaseFragment
import com.example.myproject.databinding.FragmentMyBinding

class MyFragment : BaseFragment<FragmentMyBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMyBinding.inflate(inflater, container, false)

    override fun initView() {
        // TODO: initialize my views
    }

    override fun initData() {
        // TODO: load my data
    }
}