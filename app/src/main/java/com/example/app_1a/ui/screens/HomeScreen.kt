package com.example.app_1a.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_1a.AppViewModelFactory
import com.example.app_1a.ui.viewmodel.HomeViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val products by viewModel.products.collectAsState()
    var showProducts by remember { mutableStateOf(true) } // Cambiado a true por defecto para verlos al entrar

    // Formato de moneda chilena
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Bienvenido a Level-Up Gamer",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { showProducts = !showProducts }) {
            Text(if (showProducts) "Ocultar Productos" else "Mostrar Productos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = showProducts,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (products.isEmpty()) {
                    item {
                        Text(
                            "Cargando productos...",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(products) { product ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )

                                // Columna para los precios (CLP y USD)
                                Column(horizontalAlignment = Alignment.End) {
                                    // Precio en Pesos
                                    Text(
                                        text = numberFormat.format(product.price),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // 🟢 NUEVO: Precio en Dólares (API Externa)
                                    Text(
                                        text = viewModel.convertToUsd(product.price),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}