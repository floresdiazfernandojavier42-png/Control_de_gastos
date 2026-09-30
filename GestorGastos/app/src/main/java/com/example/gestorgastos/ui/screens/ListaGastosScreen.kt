package com.example.gestorgastos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gestorgastos.data.Gasto
import com.example.gestorgastos.ui.components.GastoItem
import com.example.gestorgastos.viewmodel.GastoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaGastosScreen(
    viewModel: GastoViewModel,
    onAgregar: () -> Unit,
    onEditar: (Int) -> Unit
) {
    val gastos by viewModel.gastos.collectAsStateWithLifecycle()
    var porEliminar by remember { mutableStateOf<Gasto?>(null) }
    val total = gastos.sumOf { it.monto }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Gastos") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregar,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) { Icon(Icons.Filled.Add, contentDescription = "Agregar gasto") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Total gastado", style = MaterialTheme.typography.labelLarge)
                    Text(
                        "$" + String.format("%.2f", total),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (gastos.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aún no hay gastos. Toca + para agregar uno.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(gastos, key = { it.id }) { gasto ->
                        GastoItem(
                            gasto = gasto,
                            onEditar = { onEditar(gasto.id) },
                            onEliminar = { porEliminar = gasto }
                        )
                    }
                }
            }
        }
    }

    porEliminar?.let { gasto ->
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            title = { Text("Eliminar gasto") },
            text = { Text("¿Seguro que quieres eliminar \"${gasto.descripcion}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminar(gasto)
                    porEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { porEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}
