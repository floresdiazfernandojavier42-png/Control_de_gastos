package com.example.gestorgastos.data

import kotlinx.coroutines.flow.Flow

class GastoRepository(private val dao: GastoDao) {
    val gastos: Flow<List<Gasto>> = dao.listar()

    suspend fun obtener(id: Int): Gasto? = dao.obtener(id)
    suspend fun insertar(gasto: Gasto) = dao.insertar(gasto)
    suspend fun actualizar(gasto: Gasto) = dao.actualizar(gasto)
    suspend fun eliminar(gasto: Gasto) = dao.eliminar(gasto)
}
