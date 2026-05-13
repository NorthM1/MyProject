# 项目 Agent 配置说明书
## 一、项目基础信息（AI 必知）
1. 项目类型：基于虚拟人的术后康复辅助系统
2. 应用包名：com.example.myproject
3. 项目结构：单模块 / 多模块组件化 (app + feature + base + network + db)

## 二、核心技术栈（严格遵循）
### 1. 开发语言
- 主语言：Kotlin（全面优先）
- 禁止：Java 新代码编写，仅维护旧代码
- 禁止：混合编程随意性

### 2. 架构模式
- 架构：MVVM + Repository 分层
- 分层规则：UI → ViewModel → Repository → DataSource(Network/DB)
- 基类统一：
  - BaseActivity
  - BaseFragment
  - BaseViewModel
  - BaseRepository

### 3. UI 开发
- UI 方案：采用xml，禁止使用compose
- 设计规范：Material Design 3
- 禁止：直接操作 View 违背生命周期
- 列表： RecyclerView (XML)

### 4. Jetpack 全家桶
- 必用：ViewModel、StateFlow/SharedFlow、Lifecycle、Hilt、Room、Navigation
- 异步：Kotlin 协程 + CoroutineScope
- 禁止：LiveData 新代码、RxJava 新业务

### 5. 第三方核心依赖
- 网络：Retrofit + OkHttp + Moshi/Kotlinx Serialization
- 图片：Glide
- 数据库：Room
- 路由：Navigation

## 三、工程化规范（强制遵守）
### 1. 文件命名规范
- Activity：XxxActivity
- Fragment：XxxFragment
- ViewModel：XxxViewModel
- Repository：XxxRepository
- DataSource：XxxDataSource
- Entity：XxxEntity
- UI State：XxxUiState
- 单例：object / 懒加载，禁止静态单例

### 2. 包结构规范
- ui：页面
- viewmodel：VM
- repository：数据仓库
- datasource：网络/本地数据源
- model：数据类、UiState
- utils：工具类
- base：基类

### 3. 代码规范
1. 所有网络请求必须处理：加载中、成功、失败、空数据
2. ViewModel 禁止持有 View/Context 引用（防内存泄漏）
3. 协程必须绑定生命周期：viewModelScope / lifecycleScope
4. 异常统一捕获，禁止裸奔 try-catch
5. 字符串/颜色/尺寸统一放入资源文件，禁止硬编码
6. 权限申请遵循 Android 官方规范
7. 日志统一使用 Logcat 工具类，禁止 println

## 四、Android 专属约束（禁止项）
1. 禁止修改：root 目录 build.gradle、settings.gradle、gradle.properties
2. 禁止修改：AndroidManifest.xml 核心配置（AI 仅提供建议）
3. 禁止使用：已废弃 API、高版本 API 不做兼容
4. 禁止：内存泄漏、ANR 风险代码
5. 禁止：直接在 UI 层做网络/DB 耗时操作
6. 禁止：硬编码包名、版本号、签名信息

## 五、Agent 工作权限（安全边界）
1. 允许操作：业务代码（ui、viewmodel、repository、model）
2. 允许操作：工具类、简单组件
3. 禁止操作：
   - 签名文件、local.properties、密钥配置
   - 构建脚本、混淆配置、清单文件核心权限
   - base 基类、di 核心注入
4. 修改规则：所有代码修改必须先预览 Diff，人工确认后应用
5. 任务规则：复杂需求分模块完成，禁止全项目批量修改

## 六、通用业务规则
1. 网络统一处理：加载状态、错误提示、Token 过期刷新
2. UI 状态统一：StateFlow 驱动，单向数据流
3. 页面跳转：统一路由/Navigation
4. 弹窗/Toast：统一封装工具类


