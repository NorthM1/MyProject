package com.example.myproject.ui.action

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myproject.base.BaseFragment
import com.example.myproject.databinding.FragmentActionBinding
import com.google.android.filament.gltfio.Animator
import io.github.sceneview.node.ModelNode

class ActionFragment : BaseFragment<FragmentActionBinding>() {

    private var modelNode: ModelNode?=null
    private lateinit var animator: Animator
    private var startOffset = 0f
    private val aniIndex=0

    private var playStartTime = -1L
    private var animDuration: Float=0f
    var playbackSpeed = 1.0f
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentActionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sceneview.lifecycle=lifecycle
        loadModel()
    }

    private fun loadModel() {
        // 2.x 正确的加载方式：通过 modelLoader
        binding.sceneview.modelLoader.loadModelAsync(
            fileLocation = "models/myBlend.glb"
        ) { model ->
            if (model == null) {
                Log.e("TAG", "模型加载失败")
                return@loadModelAsync
            }

            // 用加载好的 instance 构造 ModelNode
            val node = ModelNode(
                modelInstance = model.instance,
                autoAnimate=true,
                scaleToUnits = null
            )
                .apply {
                    transform(
                        position = io.github.sceneview.math.Position(y = -2f) // 放在相机前方
                    )
                }
            Log.d("log", "scaleToUnits后缩放比: ${node.scale}")

            binding.sceneview.addChildNode(node)
            modelNode = node

            // 查看模型有哪些动画
            animator = model.instance.animator
            Log.d("log", "动画数量: ${animator.animationCount}")
            for (i in 0 until animator.animationCount) {
                Log.d("log", "动画[$i]: ${animator.getAnimationName(i)}")
            }
            animDuration=animator.getAnimationDuration(aniIndex)
            Log.d("log", "动画时长: ${animDuration}")

            //modelNode?.playAnimation(0)
            //            animator.applyAnimation(0, time)
            //            animator.updateBoneMatrices() // 必须调用，否则骨骼不更新
//            autoFitCamera(model.instance)

        }
    }

    override fun initView() {
        // TODO: initialize action views
    }

    override fun initData() {
        // TODO: load action data
    }
}

