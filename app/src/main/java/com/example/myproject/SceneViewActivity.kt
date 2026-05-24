package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.Box
import com.google.android.filament.gltfio.Animator
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.sceneview.SceneView
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode

class SceneViewActivity : AppCompatActivity() {

    private lateinit var sceneView: SceneView
    private lateinit var seekBar: SeekBar
    private var modelNode: ModelNode?=null
    private lateinit var animator: Animator
    private var startOffset = 0f
    private val aniIndex=0

    private var playStartTime = -1L
    private var animDuration: Float=0f
    private var playbackSpeed = 1.0f
    private var isPlaying = false

    // UI 按钮
    private lateinit var btnPlayPause: FloatingActionButton
    private lateinit var btnCompare: MaterialButton
    private lateinit var btnRewind: ImageButton
    private lateinit var btnForward: ImageButton
    private lateinit var btnCompleteTraining: MaterialButton
    private lateinit var btnSpeed05: MaterialButton
    private lateinit var btnSpeed1: MaterialButton
    private lateinit var btnSpeed2: MaterialButton
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var btnResetView: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sceneview)
        sceneView = findViewById<SceneView>(R.id.scene_view)
        sceneView.lifecycle=lifecycle
        seekBar=findViewById<SeekBar>(R.id.seek_bar)
        btnPlayPause = findViewById(R.id.btn_play_pause)
        btnCompare = findViewById(R.id.btn_compare_analysis)
        btnRewind = findViewById(R.id.btn_rewind)
        btnForward = findViewById(R.id.btn_forward)
        btnCompleteTraining = findViewById(R.id.btn_complete_training)
        btnSpeed05 = findViewById(R.id.btn_speed_05)
        btnSpeed1 = findViewById(R.id.btn_speed_1)
        btnSpeed2 = findViewById(R.id.btn_speed_2)
        tvCurrentTime = findViewById(R.id.tv_current_time)
        tvTotalTime = findViewById(R.id.tv_total_time)
        btnResetView = findViewById(R.id.btn_reset_view)

        btnPlayPause.setOnClickListener {
            // 切换播放/暂停
            if (!::animator.isInitialized) return@setOnClickListener
            if (!isPlaying) {
                // 开始播放，从当前进度开始
                isPlaying = true
                btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
                playAnimationFrom(seekBar.progress)
            } else {
                // 暂停
                isPlaying = false
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
                Choreographer.getInstance().removeFrameCallback(frameCallback)
            }
        }

        btnCompare.setOnClickListener {
            // 跳转到 AnalysisActivity
            val intent = Intent(this, AnalysisActivity::class.java)
            startActivity(intent)
        }

        btnRewind.setOnClickListener {
            if (!::animator.isInitialized) return@setOnClickListener
            // 后退 5 秒
            val curTime = (seekBar.progress / 100f) * animDuration
            val newTime = (curTime - 5f).let { if (it < 0f) (it + animDuration) else it }
            val newProgress = ((newTime / animDuration) * 100).toInt()
            if (isPlaying) playAnimationFrom(newProgress) else {
                animator.applyAnimation(aniIndex, newTime)
                animator.updateBoneMatrices()
                seekBar.progress = newProgress
                tvCurrentTime.text = formatTime(newTime)
            }
        }

        btnForward.setOnClickListener {
            if (!::animator.isInitialized) return@setOnClickListener
            // 前进 5 秒
            val curTime = (seekBar.progress / 100f) * animDuration
            val newTime = (curTime + 5f) % animDuration
            val newProgress = ((newTime / animDuration) * 100).toInt()
            if (isPlaying) playAnimationFrom(newProgress) else {
                animator.applyAnimation(aniIndex, newTime)
                animator.updateBoneMatrices()
                seekBar.progress = newProgress
                tvCurrentTime.text = formatTime(newTime)
            }
        }

        btnCompleteTraining.setOnClickListener {
            // 占位点击事件：后续实现完成训练逻辑
            Log.d("SceneView", "Complete training clicked")
        }

        btnSpeed05.setOnClickListener { setPlaybackSpeed(0.5f) }
        btnSpeed1.setOnClickListener { setPlaybackSpeed(1.0f) }
        btnSpeed2.setOnClickListener { setPlaybackSpeed(2.0f) }

        btnResetView.setOnClickListener {
            sceneView.cameraNode.position =
                io.github.sceneview.math.Position(z = 9f)
        }

        loadModel()
    }



    private fun loadModel() {
        // 2.x 正确的加载方式：通过 modelLoader
        sceneView.modelLoader.loadModelAsync(
            fileLocation = "models/doctor_action_default.glb"
        ) { model ->
            if (model == null) {
                Log.e("TAG", "模型加载失败")
                return@loadModelAsync
            }

            // 用加载好的 instance 构造 ModelNode
            val node = ModelNode(
                modelInstance = model.instance,
                autoAnimate=false,
                scaleToUnits = null
            )
                .apply {
                transform(
                    position = io.github.sceneview.math.Position(y = -2f) // 放在相机前方
                )
            }
            Log.d("log", "scaleToUnits后缩放比: ${node.scale}")

            sceneView.addChildNode(node)
            modelNode = node

            // 查看模型有哪些动画
            animator = model.instance.animator
            Log.d("log", "动画数量: ${animator.animationCount}")
            for (i in 0 until animator.animationCount) {
                Log.d("log", "动画[$i]: ${animator.getAnimationName(i)}")
            }
            animDuration=animator.getAnimationDuration(aniIndex)
            Log.d("log", "动画时长: ${animDuration}")
            runOnUiThread {
                tvTotalTime.text = formatTime(animDuration)
                createSeekBar()
            }
        //modelNode?.playAnimation(0)
        //            animator.applyAnimation(0, time)
        //            animator.updateBoneMatrices() // 必须调用，否则骨骼不更新
//            autoFitCamera(model.instance)

        }
    }

    fun createSeekBar(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                p0: SeekBar?,
                p1: Int,
                p2: Boolean
        ) {
                if (p2) {
                    // 用户主动拖动
                    if (isPlaying) playAnimationFrom(p1)
                    else {
                        // 仅更新到指定帧，不启动播放
                        startOffset = p1 * animDuration / 100
                        animator.applyAnimation(aniIndex, startOffset)
                        animator.updateBoneMatrices()
                        tvCurrentTime.text = formatTime(startOffset)
                    }
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })
    }

    fun playAnimationFrom(fromProgress: Int) {
        startOffset = fromProgress * animDuration/100  // 换算成秒
        Log.d("log", "跳转时长: $startOffset")
        animator.applyAnimation(aniIndex, startOffset)
        animator.updateBoneMatrices() // 必须调用，否则骨骼不更新
        playStartTime = -1L
        Choreographer.getInstance().removeFrameCallback(frameCallback)
        Choreographer.getInstance().postFrameCallback(frameCallback)

//        sceneView.onFrame = { frameTime ->
//            // 第一帧时记录起始时间
//            if (playStartTime < 0L) {
//                playStartTime = frameTime.nanoseconds
//            }
//
//            val elapsed = (frameTime.nanoseconds - playStartTime) / 1_000_000_000f
//            val time = (startOffset + elapsed) % animDuration  // 超出则循环
//
//            modelAnimator?.applyAnimation(animIndex, time)
//            modelAnimator?.updateBoneMatrices()
//        }
    }

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (playStartTime < 0L) {
                playStartTime = frameTimeNanos
            }

            val elapsed = (frameTimeNanos - playStartTime) / 1_000_000_000f
            val time = (startOffset + elapsed * playbackSpeed).mod(animDuration)

            animator.applyAnimation(aniIndex, time)
            animator.updateBoneMatrices()

            // 同步 SeekBar 位置
            val progress = ((time / animDuration) * 100).toInt()
            seekBar.progress = progress
            tvCurrentTime.text = formatTime(time)
            if (isPlaying) Choreographer.getInstance().postFrameCallback(this)
        }
    }

    // 停止播放
    fun stopAnimation() {
        isPlaying = false
        Choreographer.getInstance().removeFrameCallback(frameCallback)
    }

    private fun setPlaybackSpeed(speed: Float) {
        if (playbackSpeed == speed) return
        // 如果正在播放，从当前位置无缝切换到新速度
        if (isPlaying && ::animator.isInitialized) {
            val curTime = (seekBar.progress / 100f) * animDuration
            startOffset = curTime
            playStartTime = -1L
        }
        playbackSpeed = speed
        updateSpeedButtons()
    }

    private fun updateSpeedButtons() {
        btnSpeed05.setStrokeColorResource(if (playbackSpeed == 0.5f) R.color.active_speed else R.color.inactive_speed)
        btnSpeed05.setTextColor(if (playbackSpeed == 0.5f) getColor(R.color.active_speed_text) else getColor(R.color.inactive_speed_text))
        btnSpeed1.setStrokeColorResource(if (playbackSpeed == 1.0f) R.color.active_speed else R.color.inactive_speed)
        btnSpeed1.setTextColor(if (playbackSpeed == 1.0f) getColor(R.color.active_speed_text) else getColor(R.color.inactive_speed_text))
        btnSpeed2.setStrokeColorResource(if (playbackSpeed == 2.0f) R.color.active_speed else R.color.inactive_speed)
        btnSpeed2.setTextColor(if (playbackSpeed == 2.0f) getColor(R.color.active_speed_text) else getColor(R.color.inactive_speed_text))
    }

    private fun formatTime(seconds: Float): String {
        val totalSecs = seconds.toInt()
        val min = totalSecs / 60
        val sec = totalSecs % 60
        return "%d:%02d".format(min, sec)
    }

    override fun onPause() {
        super.onPause()
        // 暂停播放，避免后台持续回调
        if (isPlaying) {
            isPlaying = false
            Choreographer.getInstance().removeFrameCallback(frameCallback)
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Choreographer.getInstance().removeFrameCallback(frameCallback)
    }

    private fun autoFitCamera(instance: ModelInstance) {
        val rm = sceneView.engine.renderableManager
        var maxHalf = 0f
        var centerY = 0f

        for (entity in instance.entities) {
            if (!rm.hasComponent(entity)) continue
            val ri = rm.getInstance(entity)

            // 需要先创建 Box 对象作为输出参数
            val box = Box()
            rm.getAxisAlignedBoundingBox(ri, box)

            val half = maxOf(box.halfExtent[0], box.halfExtent[1], box.halfExtent[2])
            if (half > maxHalf) {
                maxHalf = half
                centerY = box.center[1]
            }
        }

        if (maxHalf == 0f) {
            sceneView.cameraNode.position = io.github.sceneview.math.Position(z = 3f)
            return
        }

        val distance = (maxHalf / Math.tan(Math.toRadians(30.0)) * 1.5).toFloat()
        sceneView.cameraNode.position =
            io.github.sceneview.math.Position(x = 0f, y = centerY, z = distance)
        sceneView.cameraNode.far = distance * 100f

        Log.d("Camera", "包围盒半径: $maxHalf, 相机距离: $distance")
    }
}