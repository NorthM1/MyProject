package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.ActionStepEntity

object TestActionStepEntity {

    fun getTestActionStepList(): List<ActionStepEntity> {
        return listOf(
            // === 踝泵运动 (act_001) 的分步 ===
            ActionStepEntity(
                _id = 1L,
                actionId = "act_001",
                stepNumber = 1,
                stepDescription = "平躺或双腿伸直坐在床上，放松大腿和小腿肌肉。",
                stepDurationSeconds = 5
            ),
            ActionStepEntity(
                _id = 2L,
                actionId = "act_001",
                stepNumber = 2,
                stepDescription = "缓慢用力向上勾起脚尖，使脚尖朝向自己，保持肌肉紧绷。",
                stepDurationSeconds = 5
            ),
            ActionStepEntity(
                _id = 3L,
                actionId = "act_001",
                stepNumber = 3,
                stepDescription = "缓慢向下踩平脚面，使脚尖向下压，保持肌肉紧绷，稍作停���后重复。",
                stepDurationSeconds = 5
            ),

            // === 股四头肌等长收缩 (act_002) 的分步 ===
            ActionStepEntity(
                _id = 4L,
                actionId = "act_002",
                stepNumber = 1,
                stepDescription = "平躺于床上，双腿伸直，脚尖朝上。",
                stepDurationSeconds = 2
            ),
            ActionStepEntity(
                _id = 5L,
                actionId = "act_002",
                stepNumber = 2,
                stepDescription = "大腿前侧肌肉（股四头肌）用力收缩，将膝盖��侧压向床面。",
                stepDurationSeconds = 3
            ),
            ActionStepEntity(
                _id = 6L,
                actionId = "act_002",
                stepNumber = 3,
                stepDescription = "保持收缩状态5-10秒，然后完全放松休息。",
                stepDurationSeconds = 10
            ),

            // === 膝关节被动屈伸 (act_004) 的分步 ===
            ActionStepEntity(
                _id = 7L,
                actionId = "act_004",
                stepNumber = 1,
                stepDescription = "坐在床边或椅子上，大腿完全在平面上，小腿自然下垂。",
                stepDurationSeconds = 2
            ),
            ActionStepEntity(
                _id = 8L,
                actionId = "act_004",
                stepNumber = 2,
                stepDescription = "利用健侧腿或双手辅助，缓慢将患侧小腿抬起至最大伸直角度。",
                stepDurationSeconds = 10
            ),
            ActionStepEntity(
                _id = 9L,
                actionId = "act_004",
                stepNumber = 3,
                stepDescription = "缓慢放下小腿，使其自然下垂并尽力向后弯曲，在最大角���停留。",
                stepDurationSeconds = 10
            )
        )
    }
}

