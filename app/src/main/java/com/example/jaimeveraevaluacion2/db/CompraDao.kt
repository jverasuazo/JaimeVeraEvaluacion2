package com.example.jaimeveraevaluacion2.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CompraDao {

    //Seleccionar todo y ordenar ascendente por compra realizada 0 o 1
    @Query("SELECT * FROM compra ORDER BY realizada ASC")
    fun findAll(): List<Compra>

    @Query("SELECT COUNT(*) FROM compra")
    fun contar(): Int

    @Insert
    fun insertar(compra:Compra):Long

    @Update
    fun actualizar(compra:Compra)

    @Delete
    fun eliminar(compra:Compra)

}