package com.example.myproject.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * 康复动作实体类
 * 数据库表名：action
 *
 * 核心业务职责：
 * 1. 存储康复动作的基础信息（名称、演示、时长、要点）
 * 2. 与 PlanEntity 是多对多关系，通过 PlanActionEntity 关联
 * 3. 预置动作禁止删除，自定义动作支持编辑/删除
 */
@Entity(
    tableName = "action"
)
data class ActionEntity(
    /** 主键 ID（与后端统一） */
    @PrimaryKey
    val _id: String,

    /** 动作名称（如：膝关节屈伸训练） */
    @ColumnInfo(name = "name")
    val name: String,


    /** 单次训练时长（单位：分） */
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int,

    /** 虚拟人演示文件路径（本地URI或云端URL，如 glb/gltf） */
    @ColumnInfo(name = "file_uri")
    val fileUri: String? = null,

    /** 目标肌肉群（如：股四头肌、腘绳肌） */
    @ColumnInfo(name = "target_muscles")
    val targetMuscles: String? = null,

)