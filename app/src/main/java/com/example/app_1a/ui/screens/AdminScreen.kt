package com.example.app_1a.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager // 🟢 IMPORTANTE: Para evitar el crash
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.app_1a.ui.viewmodel.AdminViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val products by viewModel.allProducts.collectAsState()

    // Formateador de moneda (Pesos Chilenos)
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    // 🟢 1. OBTENEMOS EL GESTOR DE FOCO
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Panel de Administrador",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- FORMULARIO DE CREACIÓN / EDICIÓN ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // El título cambia si estamos editando o creando
                Text(
                    text = if (uiState.selectedProduct == null) "Agregar Nuevo Producto" else "Editar Producto",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.productName,
                    onValueChange = { viewModel.onProductNameChange(it) },
                    label = { Text("Nombre del Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.productPrice,
                    onValueChange = { viewModel.onProductPriceChange(it) },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fila de Botones
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Botón Cancelar (Solo visible si estamos editando)
                    if (uiState.selectedProduct != null) {
                        OutlinedButton(
                            onClick = { viewModel.clearSelection() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                    }

                    // Botón Guardar (Cambia de texto dinámicamente)
                    Button(
                        onClick = {
                            // 🟢 2. SOLUCIÓN CRASH: Cerramos el teclado antes de guardar
                            focusManager.clearFocus()
                            viewModel.saveProduct()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (uiState.selectedProduct == null) "Agregar" else "Actualizar")
                    }
                }

                // Mensaje de Error
                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- LISTA DE PRODUCTOS ---
        Text(
            "Inventario Actual",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Columna de Datos (Nombre y Precio)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = numberFormat.format(product.price),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Columna de Acciones (Editar y Borrar)
                        Row {
                            // Botón Editar (Lápiz)
                            IconButton(onClick = { viewModel.selectProduct(product) }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Botón Eliminar (Basura)
                            // 🟢 3. SOLUCIÓN NULL: Usamos el operador Elvis ?: 0
                            IconButton(onClick = { viewModel.deleteProduct(product.id ?: 0) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}