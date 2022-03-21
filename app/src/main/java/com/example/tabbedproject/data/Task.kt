package com.example.tabbedproject.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("username"),
            childColumns = arrayOf("user_username"),
            onDelete = ForeignKey.CASCADE
        )
    ), tableName = "task_table"
)
data class Task(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val imageAddress: String,
    val state: String,
    val user_username: String
)
