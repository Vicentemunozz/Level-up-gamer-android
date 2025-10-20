package com.example.app_1a.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.app_1a.ui.viewmodel.AdminViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Panel de Administrador", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = uiState.productName,
                    onValueChange = { viewModel.onProductNameChange(it) },
                    label = { Text("Nombre del Producto") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.productPrice,
                    onValueChange = { viewModel.onProductPriceChange(it) },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.addProduct() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Agregar Producto")
                }
                uiState.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Lista de Productos", style = MaterialTheme.typography.titleLarge)

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(products) { product ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(product.name)
                    Text(numberFormat.format(product.price))
                }
                Divider()
            }
        }
    }
}

