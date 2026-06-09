# 基于虚拟人的术后康复辅助系统

## 项目简介

本项目为本科毕业设计，面向膝关节术后患者的居家康复场景，设计并实现了一套基于虚拟人技术的康复训练辅助系统。系统以 Android 为移动端平台，采用 MVVM 架构结合 Room 本地数据库，通过 SceneView 渲染三维虚拟人模型进行标准动作示范，借助 MediaPipe Pose Landmarker 提取人体骨骼关键点，并基于 DTW（动态时间规整）算法实现患者动作与标准动作的自动对比分析，采用四维度加权评分模型输出分段评估报告，为患者提供直观的动作矫正参考。

---

## 技术栈

| 分类 | 技术选型 |
|------|----------|
| 开发语言 | Kotlin |
| 最低 SDK | Android 9.0 (API 28) |
| 目标 SDK | Android 16 (API 36) |
| 架构模式 | MVVM + Repository + Room |
| 3D 渲染引擎 | SceneView (Google Filament) |
| 姿态检测 | MediaPipe Pose Landmarker (33 关键点) |
| 数据库 | Room (SQLite) + KSP |
| 视频播放 | ExoPlayer (Media3) |
| 相机 | CameraX |
| 异步处理 | Kotlin Coroutines |
| 构建系统 | Gradle (Kotlin DSL) |

---

## 系统功能模块

### 1. 首页仪表盘（HomeFragment）
- 展示患者姓名、术后康复天数、累计打卡次数、任务完成率统计卡片
- 顶部可折叠式头图区域，下拉展示完整信息
- 今日训练任务列表，实时显示各动作完成状态

### 2. 康复方案管理（PlanFragment / PlanDetailActivity）
- 预设三个康复阶段方案：术后 1-2 周初期方案、3-4 周进阶方案、5-8 周力量方案
- 方案列表支持按状态筛选（全部 / 进行中 / 待开始 / 已完成 / 收藏）
- 方案详情页展示动作列表（含组数、次数）、整体进度条、统计概览

### 3. 虚拟人动作展示与训练（SceneViewActivity）
- 加载 GLB 格式三维人体模型，通过 SceneView 渲染标准康复动作动画
- 播放控制：播放 / 暂停、快进 / 快退、重置视角
- 三档变速播放：0.5× / 1.0× / 2.0×
- 训练记录：支持标记每组训练完成，自动记录至本地数据库
- 一键跳转至对比分析

### 4. 标准动作帧序列提取（DoctorExtractionActivity）
- 导入医生标准动作视频，使用 MediaPipe 逐帧提取 33 个骨骼关键点坐标
- 将关键点序列压缩为 JSON 格式并保存至本地下载目录
- 提取结果可直接用于后续对比分析模块，无需重复处理视频

### 5. 动作对比分析（AnalysisActivity / AnalysisActivityOnlyPatient）
- **双模式对比**：
  - 模式 A：医生视频 + 患者视频并排播放，实时骨架覆盖
  - 模式 B：3D 虚拟人 + 患者视频对比，适用于无法获取医生视频的场景
- **姿态提取**：MediaPipe 逐帧提取 33 个关键点，计算 10 维关节角度特征向量（左右肘角、左右肩纵角、左右肩横角、左右躯干侧倾角、左右躯干扭转角）
- **子序列 DTW**：在长患者序列中定位与标准动作最佳匹配的子段，消除无关背景动作干扰
- **四维度分段评分**：基于 DTW 全局对齐路径动态分块，每段综合评估
- **分段列表交互**：点击任意分段可跳转至对应视频时刻，骨架覆盖层实时反馈

---

## 核心算法流程

### 步骤一：关节角度特征提取
从 MediaPipe 输出的人体 33 关键点中，选取 10 组三元组（肩-肘-腕、髋-肩-肘、对侧肩-肩-肘等），计算三维向量夹角，构成每帧的 10 维特征向量。对原始角度序列进行滑动窗口平滑（窗口大小 3）。

### 步骤二：子序列 DTW
在患者完整动作序列中搜索与医生标准序列最相似的时间子段。使用 Sakoe-Chiba Band 窗口约束（窗口比例 0.15）降低 DP 计算开销。DP 矩阵第 0 行置零，允许匹配从患者序列任意位置起始。回溯得到最佳匹配的起止帧索引。

### 步骤三：全局对齐路径与动态分块
在医生序列和患者有效子段之间，通过标准 DTW 建立帧级对齐映射 `alignment[i] = 医生第 i 帧 → 患者有效段第 j 帧`。以 5 秒（50 帧）为固定窗口沿医生时间轴滑动切块，每段的患者帧边界由对齐路径确定，消除个体动作速度差异对分段的影响。

### 步骤四：四维度加权评分模型

每段综合空间三维和时间维共四个子分：

| 维度 | 子分 | 计算方法 | 权重 |
|------|------|----------|:----:|
| 空间-平均偏差 | S_avg | `max(0, 1 - (avgDiff/30)^1.5) × 100` | 50% |
| 空间-超阈值比 | S_err | `(1 - 超阈值帧占比) × 100`（阈值15°） | 20% |
| 空间-最大偏差 | S_max | `max(0, 1 - maxDiff/90) × 100` | 20% |
| 时间-速度偏移 | S_speed | `max(0, 1 - SRD/0.8) × 100` | 10% |

**S_speed 计算过程：**
1. 利用 DTW 对齐路径计算局部瞬时速率 `r_i = (path[i+w] - path[i]) / w`，w = 5
2. 速度偏移率 `SRD = 均值(|r_i - 1.0|)`，理想同步时 r_i ≈ 1.0
3. 线性惩罚映射至 [0, 100]，上限阈值 0.8

### 步骤五：总分汇总
```
总分 = Σ(分段得分 × 运动复杂度权重) / Σ复杂度权重
```
运动复杂度由医生帧各关节角度的标准差衡量。评分等级：≥90 优秀 / 80-89 良好 / ＜80 需改进。

---

## 项目结构

```
app/src/main/java/com/example/myproject/
├── base/
│   ├── BaseActivity.kt              # ViewBinding 泛型 Activity 基类
│   └── BaseFragment.kt              # ViewBinding 泛型 Fragment 基类
├── data/local/
│   ├── dao/                         # Room DAO 接口（9 个）
│   │   ├── ActionDao.kt
│   │   ├── ActionStepDao.kt
│   │   ├── PlanDao.kt
│   │   ├── PlanActionDao.kt
│   │   ├── TrainingSessionDao.kt
│   │   ├── TrainingSessionActionProgressDao.kt
│   │   ├── TrainingActionRecordDao.kt
│   │   ├── UserDao.kt
│   │   ├── UserPlanDao.kt
│   │   └── model/                  # 联表查询 POJO
│   ├── entity/                      # Room 实体类（9 个表）
│   ├── testdata/                    # 预置种子数据（演示用）
│   ├── AppDatabase.kt              # 数据库定义与单例
│   └── DatabaseSeeder.kt           # 首次创建时自动填充
├── repository/
│   └── PlanRepository.kt           # 统一数据仓库层
├── ui/
│   ├── activity/
│   │   ├── MainActivity.kt         # 底部导航宿主
│   │   └── PlanDetailActivity.kt   # 方案详情页
│   ├── adapter/
│   │   ├── PlanAdapter.kt          # 方案列表适配器
│   │   ├── ActionAdapter.kt        # 动作列表适配器
│   │   └── TodayTaskAdapter.kt     # 今日任务适配器
│   ├── fragment/
│   │   ├── HomeFragment.kt         # 首页仪表盘
│   │   └── PlanFragment.kt         # 方案列表页
│   ├── home/
│   │   ├── HomeData.kt             # 首页聚合数据模型
│   │   └── TodayTask.kt            # 今日任务模型
│   ├── model/                       # UI 层数据模型
│   └── viewmodel/                   # ViewModel 层
├── util/
│   └── ToastUtil.kt                # 全局 Toast 工具
├── AnalysisActivity.kt             # 双视频对比分析
├── AnalysisActivityOnlyPatient.kt  # 3D 虚拟人 + 患者视频分析
├── SceneViewActivity.kt            # 3D 模型查看与训练
├── DoctorExtractionActivity.kt     # 标准动作帧序列提取工具
├── OverlayView.kt                  # 自定义 View：骨架关键点与连线绘制
├── PoseLandmarkerHelper.kt         # MediaPipe Pose Landmarker 封装
├── SegmentAdapter.kt               # 分析分段列表适配器
└── MyApplication.kt                # Application：全局 userId 持有
```

---

## 数据库设计

| 表名 | 说明 | 核心字段 |
|------|------|----------|
| `user` | 用户信息 | id, name, surgery_date |
| `action` | 康复动作 | id, name, duration_seconds, file_uri |
| `action_step` | 动作步骤分解 | id, action_id, step_number, description |
| `plan` | 康复方案 | id, name, total_weeks, daily_minutes |
| `plan_action` | 方案-动作关联 | plan_id, action_id, groups, times |
| `user_plan` | 用户-方案绑定 | user_id, plan_id, status, start_date |
| `training_session` | 训练会话 | user_id, plan_id, train_date, completed |
| `training_session_action_progress` | 逐动作进度 | session_id, action_id, completed_groups |
| `training_action_record` | 逐组完成记录 | session_id, action_id, status, created_at |

首次启动时自动建表并通过 `DatabaseSeeder` 填充预设康复方案与演示数据。

---

## 构建与运行

1. 使用 Android Studio Hedgehog (2023.1.1) 或更新版本打开项目
2. 等待 Gradle 自动同步依赖
3. 将 MediaPipe 模型文件 `pose_landmarker_full.task` 放入 `app/src/main/assets/` 目录
4. 将 GLB 动画模型文件放入 `app/src/main/assets/models/` 目录
5. 连接 Android 设备或启动模拟器（API ≥ 28）
6. 选择 `app` 模块，点击 Run

---

