package com.fs.fscomapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tablaUserLog")
class UserLogClass (id : Int, name : String, tel : String, prior: Int , pass: String) {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id : Int

    @ColumnInfo(name = "name")
    var name : String

    @ColumnInfo(name = "tel")
    var tel : String

    @ColumnInfo(name = "prior")
    var prior : Int

    @ColumnInfo(name = "pass")
    var pass : String

    init {
        this.id = id
        this.name = name
        this.tel = tel
        this.prior = prior
        this.pass = pass
    }
}