package com.example.intento_proyecto.ui.theme.components

// ruta: app/src/main/java/com/example/intento_proyecto/ui/components/CartDialog.kt
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intento_proyecto.data.Product
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartDialog(
    cartItems: List<Product>,
    total: Double,
    onDismiss: () -> Unit,
    onRemoveItem: (Product) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🛒 Tu Carrito") },
        text = {
            if (cartItems.isEmpty()) {
                Text(
                    "Tu carrito está vacío.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else {
                Column {
                    // Lista de items en el carrito
                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.name, modifier = Modifier.weight(1f))
                            Text(formatCurrency(item.price), fontWeight = FontWeight.Bold)
                            IconButton(onClick = { onRemoveItem(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Quitar item", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    // Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        Text(formatCurrency(total), fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

private fun formatCurrency(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "ES")).format(price)
}
