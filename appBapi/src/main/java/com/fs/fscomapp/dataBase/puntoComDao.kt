package com.fs.fscomapp.dataBase

import androidx.room.*
import com.fs.fscomapp.entities.PuntoCom

@Dao
interface puntoComDao{

    @Query("SELECT * FROM puntoscom ORDER BY id")
    fun loadAllPuntosCom(): MutableList<PuntoCom>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPuntoCom(puntoCom: PuntoCom?)

    @Update
    fun updatePuntoCom(puntoCom: PuntoCom?)

    @Delete
    fun deletePuntoCom(puntoCom: PuntoCom?)

    @Query("DELETE FROM puntoscom")
    fun deleteAllPuntosCom()

    @Query("SELECT * FROM puntoscom WHERE id = :id")
    fun getPuntoComById(id: Int): PuntoCom?

    @Query("SELECT COUNT(id) FROM puntoscom")
    fun getCount(): Int

//    @Query("SELECT id FROM puntoscom WHERE puntoscom.dirmem = dirmem  LIMIT 1 ")
//    fun verSiPuntoLibre(dirmem:Int): Int   genera error

}