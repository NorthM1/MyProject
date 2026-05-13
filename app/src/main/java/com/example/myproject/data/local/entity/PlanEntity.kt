package com.example.myproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myproject.data.local.entity.UserPlanEntity.Companion.Status

/**
 * 方案模板实体类（系统预置/基础方案）
 * 数据库表名：plan
 *
 * 核心业务职责：
 * 1. 存储康复方案的基础模板（预置方案不可删除，自定义方案可编辑）
 * 2. 仅定义方案的基础配置（周期、时长、频率），不存储用户执行进度
 * 3. 与 UserPlanEntity 是一对多关系：一个模板可被多个用户使用
 */
@Entity(tableName = "plan")
data class PlanEntity(
    /**
     * 主键 ID
     * 预置方案使用固定ID，自定义方案可自动生成
     * 非自增设计，用于区分系统预置模板
     */
    @PrimaryKey
    val _id: String,

    /** 方案名称（如：膝关节术后1-2周康复计划） */
    @ColumnInfo(name = "name")
    val name: String,

    /** 方案封面图片本地URI，为空时UI显示默认占位图 */
    @ColumnInfo(name = "cover_image_uri")
    val coverImageUri: String? = null,

    /** 方案总周期（周），总训练天数 = totalWeeks * 7 */
    @ColumnInfo(name = "total_weeks")
    val totalWeeks: Int,

    /** 每日建议训练时长（单位：分钟），用于方案卡片展示 */
    @ColumnInfo(name = "daily_minutes")
    val dailyMinutes: Int,

    /**
     * 训练频率规则
     * 0：每日训练 | 1：隔日训练
     */
    @ColumnInfo(name = "frequency")
    val frequency: Int,

    /**
     * 是否为系统预置方案
     * true：系统预置，禁止用户删除
     * false：用户自定义，支持编辑/删除
     */
    @ColumnInfo(name = "is_preset")
    val isPreset: Boolean = true
) {
    companion object {
        /** 训练频率常量定义 */
        object Frequency {
            const val DAILY = 0     // 每日训练
            const val ALTERNATE = 1 // 隔日训练
        }
    }
}