package com.example.gestorgastos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gestorgastos.data.Gasto
import com.example.gestorgastos.viewmodel.GastoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val CATEGORIAS = listOf("Comida", "Transporte", "Servicios", "Ocio", "Salud", "Otros")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormGastoScreen(
    viewModel: GastoViewModel,
    gastoId: Int?,
    onVolver: () -> Unit
) {
    val formato = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply { isLenient = false } }

    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(formato.format(Date())) }
    var menuAbierto by remember { mutableStateOf(false) }
    var intentoGuardar by remember { mutableStateOf(false) }

    // Si venimos a editar, cargamos el registro
    LaunchedEffect(gastoId) {
        if (gastoId != null) {
            viewModel.obtener(gastoId)?.let {
                descripcion = it.descripcion
                monto = it.monto.toString()
                categoria = it.categoria
                fecha = it.fecha
            }
        }
    }

    // ---- Validaciones ----
    val errorDescripcion = when {
        descripcion.isBlank() -> "La descripción es obligatoria"
        descripcion.trim().length < 3 -> "Mínimo 3 caracteres"
        else -> null
    }
    val montoNum = monto.toDoubleOrNull()
    val errorMonto = when {
        monto.isBlank() -> "El monto es obligatorio"
        montoNum == null -> "Ingresa un número válido"
        montoNum <= 0 -> "El monto debe ser mayor que 0"
        else -> null
    }
    val errorCategoria = if (categoria.isBlank()) "Selecciona una categoría" else null
    val errorFecha = try {
        formato.parse(fecha); null
    } catch (e: Exception) {
        "Formato válido: dd/MM/yyyy"
    }
    val hayErrores = listOf(errorDescripcion, errorMonto, errorCategoria, errorFecha).any { it != null }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (gastoId == null) "Nuevo gasto" else "Editar gasto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                isError = intentoGuardar && errorDescripcion != null,
                supportingText = { if (intentoGuardar) errorDescripcion?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = intentoGuardar && errorMonto != null,
                supportingText = { if (intentoGuardar) errorMonto?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = menuAbierto,
                onExpandedChange = { menuAbierto = !menuAbierto }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAbierto) },
                    isError = intentoGuardar && errorCategoria != null,
                    supportingText = { if (intentoGuardar) errorCategoria?.let { Text(it) } },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                    CATEGORIAS.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                categoria = opcion
                                menuAbierto = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (dd/MM/yyyy)") },
                isError = intentoGuardar && errorFecha != null,
                supportingText = { if (intentoGuardar) errorFecha?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    intentoGuardar = true
                    if (!hayErrores) {
                        viewModel.guardar(
                            Gasto(
                                id = gastoId ?: 0,
                                descripcion = descripcion.trim(),
                                monto = montoNum!!,
                                categoria = categoria,
                                fecha = fecha
                            )
                        )
                        onVolver()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(top = 8.dp)
            ) {
                Text(if (gastoId == null) "Guardar" else "Actualizar")
            }
        }
    }
}
