package com.example.gestorgastos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Query("SELECT * FROM gastos ORDER BY id DESC")
    fun listar(): Flow<List<Gasto>>

    @Query("SELECT * FROM gastos WHERE id = :id")
    suspend fun obtener(id: Int): Gasto?

    @Insert
    suspend fun insertar(gasto: Gasto)

    @Update
    suspend fun actualizar(gasto: Gasto)

    @Delete
    suspend fun eliminar(gasto: Gasto)
}
