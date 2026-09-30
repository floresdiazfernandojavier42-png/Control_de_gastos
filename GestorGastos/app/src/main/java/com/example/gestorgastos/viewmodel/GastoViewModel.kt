package com.example.gestorgastos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorgastos.data.AppDatabase
import com.example.gestorgastos.data.Gasto
import com.example.gestorgastos.data.GastoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GastoViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = GastoRepository(AppDatabase.get(app).gastoDao())

    val gastos: StateFlow<List<Gasto>> = repo.gastos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun obtener(id: Int): Gasto? = repo.obtener(id)

    // Crear (id = 0) o Actualizar (id > 0)
    fun guardar(gasto: Gasto) {
        viewModelScope.launch {
            if (gasto.id == 0) repo.insertar(gasto) else repo.actualizar(gasto)
        }
    }

    fun eliminar(gasto: Gasto) {
        viewModelScope.launch { repo.eliminar(gasto) }
    }
}
