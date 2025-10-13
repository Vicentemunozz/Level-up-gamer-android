package com.example.intento_proyecto.ui.theme.screens
// ruta: app/src/main/java/com/example/intento_proyecto/ui/screens/ShopScreen.kt

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.intento_proyecto.data.Product // Importa el modelo de datos
import com.example.intento_proyecto.ui.theme.shop.ShopViewModel // Importa tu ViewModel
import com.example.intento_proyecto.ui.theme.components.CartDialog // Importa el diálogo del carrito
import com.example.intento_proyecto.ui.theme.components.ProductGrid // Importa la grilla de productos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(viewModel: ShopViewModel = viewModel()) {
    // Observa los estados del ViewModel para que la UI se actualice automáticamente
    // DESPUÉS (Correcto)
    val products = viewModel.allProducts.value
    val cartItems = viewModel.cartItems.value
    val isCartDialogVisible = viewModel.isCartDialogVisible.value
    val cartTotal = viewModel.cartTotal.value


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎮 Gaming ZoSne") },
                actions = {
                    // Botón del carrito en la barra superior
                    IconButton(onClick = { viewModel.showCartDialog() }) {
                        BadgedBox(
                            badge = {
                                // Muestra un contador solo si hay items en el carrito
                                if (cartItems.isNotEmpty()) {
                                    Badge { Text("${cartItems.size}") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        // Contenido principal de la pantalla: la grilla de productos
        ProductGrid(
            products = products,
            onProductAction = viewModel::onProductAction, // Pasa la función del ViewModel directamente
            isProductInCart = viewModel::isProductInCart,
            modifier = Modifier.padding(paddingValues)
        )

        // Muestra el diálogo del carrito solo si el estado 'isCartDialogVisible' es true
        if (isCartDialogVisible) {
            CartDialog(
                cartItems = cartItems,
                total = cartTotal,
                onDismiss = viewModel::hideCartDialog,
                onRemoveItem = viewModel::removeFromCart
            )
        }
    }
}

