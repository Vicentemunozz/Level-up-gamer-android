package com.example.intento_proyecto.ui.theme.components

// ruta: app/src/main/java/com/example/intento_proyecto/ui/components/ProductGrid.kt


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.intento_proyecto.data.Product

@Composable
fun ProductGrid(
    products: List<Product>,
    onProductAction: (Product) -> Unit,
    isProductInCart: (Product) -> Boolean,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp), // Las columnas se adaptan al tamaño de pantalla
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard( // Reutilizamos el componente ProductCard
                product = product,
                onActionClick = { onProductAction(product) },
                isInCart = isProductInCart(product)
            )
        }
    }
}
