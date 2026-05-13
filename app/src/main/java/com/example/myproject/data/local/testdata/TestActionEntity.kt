package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.ActionEntity

object TestActionEntity {
    fun getTestActionList(): List<ActionEntity> {
        return listOf(
            ActionEntity(
                _id = "act_001",
                name = "踝泵运动",
                durationSeconds = 300,
                fileUri = "assets/models/ankle_pump.glb",
                targetMuscles = "小腿腓肠肌、比目鱼肌"
            ),
            ActionEntity(
                _id = "act_002",
                name = "股四头肌等长收缩",
                durationSeconds = 300,
                fileUri = "assets/models/quadriceps_isometrics.glb",
                targetMuscles = "股四头肌"
            ),
            ActionEntity(
                _id = "act_003",
                name = "直腿抬高训练",
                durationSeconds = 480,
                fileUri = "assets/models/straight_leg_raise.glb",
                targetMuscles = "大腿前侧肌群、髂腰肌"
            ),
            ActionEntity(
                _id = "act_004",
                name = "膝关节被动屈伸",
                durationSeconds = 600,
                fileUri = "assets/models/knee_flexion_extension.glb",
                targetMuscles = "膝关节周围肌群"
            )
        )
    }
}

