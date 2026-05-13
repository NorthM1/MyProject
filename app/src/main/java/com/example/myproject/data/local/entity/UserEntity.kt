package com.example.myproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户实体类
 * 数据库表名：user
 *
 * 核心业务职责：
 * 1. 存储康复用户的核心档案信息，是整个系统的用户基础数据
 * 2. [surgeryDate] 手术日期为康复系统的时间原点，用于计算「康复总天数」
 * 3. 作为所有业务表（方案、训练记录等）的关联主键
 *
 * 多用户设计：通过自增主键区分不同用户，所有关联表通过 userId 关联
 */
@Entity(tableName = "user")
data class UserEntity(
    /**
     * 主键 ID
     * 自增主键，唯一标识一个用户
     * 注意：建议改为 Long 类型 + autoGenerate = true 更符合真实项目规范
     */
    @PrimaryKey
    val _id: String,

    /** 患者姓名，用于个人中心、训练页面等UI展示 */
    @ColumnInfo(name = "name")
    val name: String,

    /**
     * 手术日期（Unix毫秒时间戳）
     * 核心计算字段：康复天数 = (当前时间戳 - surgeryDate) / 一天总毫秒数
     * 使用时间戳存储，无需格式转换，直接进行数值计算
     */
    @ColumnInfo(name = "surgery_date")
    val surgeryDate: Long
)

//    /**
//     * 手术日期（Unix 毫秒时间戳）
//     *
//     * 用途：康复天数 = (System.currentTimeMillis() - surgeryDate) / 86_400_000L
//     * 存毫秒时间戳而非字符串，便于直接做算术运算，无需 DateFormat 转换。
//     */
//    @ColumnInfo(name = "surgery_date")
//    val surgeryDate: Long,
//
//    /** 头像本地 URI，可空（用户未设置时为 null，显示默认占位图） */
//    @ColumnInfo(name = "avatar_uri")
//    val avatarUri: String? = null,
//
//    @ColumnInfo(name = "created_at")
//    val createdAt: Long = System.currentTimeMillis(),
//
//    /** 每次编辑个人信息后更新，用于后端增量同步判断 */
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long = System.currentTimeMillis(),
//
//    /** 后端用户 ID，接入真实后端时填充，用于数据同步映射 */
//    @ColumnInfo(name = "server_id")
//    val serverId: String? = null