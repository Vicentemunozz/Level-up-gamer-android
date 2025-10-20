package com.example.app_1a.data.repository

import com.example.app_1a.data.db.dao.UserDao
import com.example.app_1a.data.db.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun findUserByEmail(email: String): User? {
        return userDao.findUserByEmail(email)
    }

    suspend fun findUserById(userId: Int): User? {
        return userDao.findUserById(userId)
    }
}

