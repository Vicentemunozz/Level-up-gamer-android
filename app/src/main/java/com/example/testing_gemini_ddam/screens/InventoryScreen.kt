package com.example.testing_gemini_ddam.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testing_gemini_ddam.inventory.InventoryViewModel

@Composable
fun InventoryScreen(navController: NavController, inventoryViewModel: InventoryViewModel = viewModel()) {
    val products by inventoryViewModel.products.collectAsState()

    var productName by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productQuantity,
                onValueChange = { productQuantity = it },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { 
            inventoryViewModel.addProduct(productName, productQuantity, imageUri?.toString())
            productName = ""
            productQuantity = ""
            imageUri = null
        }) {
            Text("Añadir producto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(products) { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${product.name} - ${product.quantity}")
                    Button(onClick = { inventoryViewModel.deleteProduct(product) }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}