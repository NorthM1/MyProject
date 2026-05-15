package com.example.myproject

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.mediapipe.tasks.vision.core.RunningMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class DoctorExtractionActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var overlay: OverlayView
    private lateinit var btnLoad: Button
    private lateinit var btnStart: Button
    private lateinit var tvResult: TextView
    private lateinit var progressBar: ProgressBar

    private var videoUri: Uri? = null
    private lateinit var poseLandmarkerHelper: PoseLandmarkerHelper

    private val COLLECT_TIME=100L

    // 选择视频的Launcher
    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            videoUri = it
            videoView.setVideoURI(it)
            videoView.start()
            btnStart.isEnabled = true
            tvResult.text = "视频已导入，准备提取..."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_extraction)

        videoView = findViewById(R.id.video_view)
        overlay = findViewById(R.id.overlay)
        btnLoad = findViewById(R.id.btn_load)
        btnStart = findViewById(R.id.btn_start)
        tvResult = findViewById(R.id.tv_result)
        progressBar = findViewById(R.id.progress_bar)
        val topAppBar = findViewById<View>(R.id.topAppBar)

        // 重新播放设置
        videoView.setOnCompletionListener { it.start() }

        // 返回按钮逻辑（可选，如果有 navigation icon 的话）
        // (topAppBar as? com.google.android.material.appbar.MaterialToolbar)?.setNavigationOnClickListener { finish() }

        btnLoad.setOnClickListener {
            pickVideo.launch("video/*")
        }

        btnStart.setOnClickListener {
            startExtraction()
        }
    }

    private fun startExtraction() {
        val uri = videoUri ?: return

        btnLoad.isEnabled = false
        btnStart.isEnabled = false
        progressBar.visibility = View.VISIBLE
        tvResult.text = "正在分析提取动作关键帧，请稍候..."

        lifecycleScope.launch(Dispatchers.Default) {
            // 初始化 PoseLandmarkerHelper 为 VIDEO 模式
            poseLandmarkerHelper = PoseLandmarkerHelper(
                context = applicationContext,
                runningMode = RunningMode.VIDEO,
                minPoseDetectionConfidence = 0.5f,
                minPoseTrackingConfidence = 0.5f,
                minPosePresenceConfidence = 0.5f,
                currentDelegate = PoseLandmarkerHelper.DELEGATE_CPU
            )

            try {
                // 调用封装好的 detectVideoFile 提取视频关键帧
                // 采用默认 33ms 一帧的间隔模拟 30fps
                val resultBundle = poseLandmarkerHelper.detectVideoFile(uri, COLLECT_TIME)

                if (resultBundle != null && resultBundle.results.isNotEmpty()) {
                    // 将提取结果整合压缩并保存为本地 JSON 文件
                    val savedFile = savePoseDataToJson(resultBundle)

                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        btnLoad.isEnabled = true
                        btnStart.isEnabled = true

                        val frameCount = resultBundle.results.size
                        tvResult.text = "分析完成！共提取: $frameCount 帧\n采集间隔: ${COLLECT_TIME}ms\n已保存至:\n${savedFile.absolutePath}"
                        // 后续可以将 savedFile.absolutePath 存储到数据库中
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        btnLoad.isEnabled = true
                        btnStart.isEnabled = true
                        tvResult.text = "提取失败，未能生成动作结果"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    btnLoad.isEnabled = true
                    btnStart.isEnabled = true
                    tvResult.text = "分析异常: ${e.message}"
                    Toast.makeText(this@DoctorExtractionActivity, "遇到错误: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } finally {
                // 分析完成后清理
                poseLandmarkerHelper.clearPoseLandmarker()
            }
        }
    }

    /**
     * 将 MediaPipe 的繁杂数据整合压缩为单一 JSON 结构，并存储到本地
     */
    private fun savePoseDataToJson(resultBundle: PoseLandmarkerHelper.ResultBundle): File {
        val rootArray = JSONArray()

        resultBundle.results.forEachIndexed { index, poseResult ->
            val landmarks = poseResult.landmarks()
            if (landmarks.isNotEmpty()) {
                val frameObj = JSONObject()
                frameObj.put("frameIndex", index)
                frameObj.put("timestampMs", index * COLLECT_TIME)

                val pointsArray = JSONArray()
                // mediapipe 的标准人体 33 个关键点
                landmarks[0].forEach { point ->
                    // 将关键点的 4 个主要维度 (x,y,z,visibility) 压缩为一个简易数组 [x, y, z, vis]
                    // 这样可以极大节省 JSON 文件的字符串体积
                    val pArray = JSONArray()
                    pArray.put(point.x().toDouble())
                    pArray.put(point.y().toDouble())
                    pArray.put(point.z().toDouble())
                    pArray.put(point.visibility().orElse(0f).toDouble())

                    pointsArray.put(pArray)
                }
                frameObj.put("points", pointsArray)
                rootArray.put(frameObj)
            }
        }

        // 写入应用的私有内部存储 (无需任何外置存储权限)
        val fileName = "doctor_pose_${System.currentTimeMillis()}.json"

        // 修改为保存到外部公共下载目录，极其容易找到并导出到电脑
        val downloadsDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val file = File(downloadsDir, fileName)
        file.writeText(rootArray.toString())
        return file
    }
}
