package com.example.myproject.data.local.testdata

import com.example.myproject.data.local.entity.UserEntity

object TestUserEntity {
    fun getTestUserEntity(): UserEntity{
        return UserEntity(
            _id = "user_1001",
            name = "张建国 (膝关节置换)",
            surgeryDate = 1710460800000L)
    }
}