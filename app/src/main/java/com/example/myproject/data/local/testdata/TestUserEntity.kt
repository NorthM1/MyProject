package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.UserEntity

object TestUserEntity {
    /**
     * 测试用户：膝关节置换术后14天
     * 手术日期动态计算为14天前，保证康复天数始终为14
     */
    fun getTestUserEntity(): UserEntity {
        val surgeryDate = System.currentTimeMillis() - 14 * 86_400_000L
        return UserEntity(
            _id = "user_1001",
            name = "小明",
            surgeryDate = surgeryDate
        )
    }
}
