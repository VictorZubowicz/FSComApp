package com.fs.fscomapp.dataBase

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fs.fscomapp.entities.PuntoCom
import com.fs.fscomapp.entities.UserLogClass


@Database(entities = [UserLogClass::class], version = 1, exportSchema = false)

public  abstract class userDatabase : RoomDatabase() {

    abstract fun userDao(): userDao

    companion object {
        var INSTANCE: userDatabase? = null

        fun getAppDataBase(context: Context): userDatabase? {
            if (INSTANCE == null) {
                synchronized(userDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        userDatabase::class.java,
                        "myDBlog"
                    ).allowMainThreadQueries().build() // No es lo mas recomendable que se ejecute en el mainthread
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}


@Database(entities = [PuntoCom::class], version = 1, exportSchema = false)

public  abstract class puntoComDatabase : RoomDatabase() {

    abstract fun puntosDao(): puntoComDao

    companion object {
        var INSTANCE: puntoComDatabase? = null

        fun getAppDatabasePuntosCom(context: Context): puntoComDatabase? {
            if (INSTANCE == null) {
                synchronized(puntoComDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        puntoComDatabase::class.java,
                        "DbPuntos"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build() // No es lo mas recomendable que se ejecute en el mainthread
                    //).addMigrations().build() //
                }
            }
            return INSTANCE
        }

        fun destroyDatabasePuntosCom(){
            INSTANCE = null
        }
    }
}