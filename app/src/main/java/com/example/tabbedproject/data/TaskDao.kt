package com.example.tabbedproject.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * from task_table WHERE user_username = :username")
    fun getUserTasks(username: String): LiveData<List<Task>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE from task_table")
    suspend fun deleteAll()

}