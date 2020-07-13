package com.fs.fscomapp.dataBase

import androidx.room.*
import com.fs.fscomapp.entities.UserLogClass

@Dao
public interface userDao {

    @Query("SELECT * FROM tablaUserLog ORDER BY id")
    fun loadAllUser(): MutableList<UserLogClass?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(userLog: UserLogClass?)

    @Update
    fun updatePerson(userLog: UserLogClass?)

    @Delete
    fun delete(userLog: UserLogClass?)

    @Query("SELECT * FROM tablaUserLog WHERE id = :id")
    fun loadUserById(id: Int): UserLogClass?

}