
package com.example.gestorgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.gestorgastos.navigation.AppNavigation
import com.example.gestorgastos.ui.theme.GestorGastosTheme
import com.example.gestorgastos.viewmodel.GastoViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: GastoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestorGastosTheme {
                AppNavigation(viewModel)
            }
        }
    }
}
