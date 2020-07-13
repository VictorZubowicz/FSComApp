package com.fs.fscomapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PuntosCom")

class PuntoCom (id : Int, nombre : String, descripcion : String,  unidad :String, valorIni: Int, grupo: Int, tipoDato :String, dirmem: Int, formato :String) {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id : Int

    @ColumnInfo(name = "nombre")
    var nombre : String

    @ColumnInfo(name = "descripcion")
    var descripcion : String

    @ColumnInfo(name = "unidad")
    var unidad : String

    @ColumnInfo(name = "ValorIni")
    var valorIni : Int

    @ColumnInfo(name = "grupo")
    var grupo : Int

    @ColumnInfo(name = "tipoDato")
    var tipoDato : String

    @ColumnInfo(name = "dirmem")
    var dirmem : Int

    @ColumnInfo(name = "formato")
    var formato : String


    init {
        this.id = id
        this.nombre = nombre
        this.descripcion = descripcion
        this.unidad = unidad
        this.valorIni = valorIni
        this.grupo = grupo
        this.tipoDato = tipoDato
        this.dirmem = dirmem
        this.formato = formato
    }

}




