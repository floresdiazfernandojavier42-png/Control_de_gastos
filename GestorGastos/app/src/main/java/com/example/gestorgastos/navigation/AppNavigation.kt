package com.example.gestorgastos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestorgastos.ui.screens.FormGastoScreen
import com.example.gestorgastos.ui.screens.ListaGastosScreen
import com.example.gestorgastos.viewmodel.GastoViewModel

object Rutas {
    const val LISTA = "lista"
    const val FORM = "form"
    const val FORM_EDITAR = "form/{id}"
}

@Composable
fun AppNavigation(viewModel: GastoViewModel) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rutas.LISTA) {
        composable(Rutas.LISTA) {
            ListaGastosScreen(
                viewModel = viewModel,
                onAgregar = { nav.navigate(Rutas.FORM) },
                onEditar = { id -> nav.navigate("form/$id") }
            )
        }
        composable(Rutas.FORM) {
            FormGastoScreen(
                viewModel = viewModel,
                gastoId = null,
                onVolver = { nav.popBackStack() }
            )
        }
        composable(
            route = Rutas.FORM_EDITAR,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            FormGastoScreen(
                viewModel = viewModel,
                gastoId = entry.arguments?.getInt("id"),
                onVolver = { nav.popBackStack() }
            )
        }
    }
}
