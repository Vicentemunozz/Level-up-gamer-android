// Asegúrate de que este paquete coincida con la ubicación de tu archivo
package com.example.intento_proyecto.ui.theme.shop

// --- IMPORTACIONES ESENCIALES ---
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue // <--- ¡LA IMPORTACIÓN MÁS IMPORTANTE!
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.intento_proyecto.data.Product

class ShopViewModel : ViewModel() {

    // Estado para la lista de todos los productos
    private val _allProducts = mutableStateOf<List<Product>>(emptyList())
    val allProducts: State<List<Product>> = _allProducts

    // Estado para los ítems en el carrito
    private val _cartItems = mutableStateOf<List<Product>>(emptyList())
    val cartItems: State<List<Product>> = _cartItems

    // Estado para controlar la visibilidad del diálogo del carrito
    private val _isCartDialogVisible = mutableStateOf(false)
    val isCartDialogVisible: State<Boolean> = _isCartDialogVisible

    // Calcula el total del carrito automáticamente.
    // 'by derivedStateOf' crea un objeto State<Double>.
    // La importación de 'getValue' permite que '.value' funcione en el Screen.
    val cartTotal by derivedStateOf {
        _cartItems.value.sumOf { it.price }
    }

    init {
        // Carga los productos cuando el ViewModel se crea por primera vez
        loadProducts()
    }

    private fun loadProducts() {
        // En una aplicación real, estos datos vendrían de una API o una base de datos local
        _allProducts.value = listOf(
            Product(1, "Teclado Mecánico RGB", "HyperX Alloy Origins", 129.99, "https://via.placeholder.com/300/e43f5a/ffffff?text=Teclado"),
            Product(2, "Mouse Gamer Inalámbrico", "Logitech G Pro", 89.99, "https://via.placeholder.com/300/1f4068/ffffff?text=Mouse"),
            Product(3, "Monitor Curvo 144Hz", "Samsung Odyssey G5", 299.99, "https://via.placeholder.com/300/e43f5a/ffffff?text=Monitor"),
            Product(4, "Silla Gamer Ergonómica", "Secretlab TITAN Evo", 459.00, "https://via.placeholder.com/300/1f4068/ffffff?text=Silla"),
            Product(5, "Headset 7.1 Surround", "Razer Kraken Ultimate", 99.99, "https://via.placeholder.com/300/e43f5a/ffffff?text=Headset"),
            Product(6, "GPU de Alto Rendimiento", "NVIDIA GeForce RTX 4070", 549.99, "https://via.placeholder.com/300/1f4068/ffffff?text=GPU")
        )
    }

    // --- LÓGICA DE INTERACCIÓN ---

    fun onProductAction(product: Product) {
        if (isProductInCart(product)) {
            removeFromCart(product)
        } else {
            addToCart(product)
        }
    }

    private fun addToCart(product: Product) {
        _cartItems.value = _cartItems.value + product
    }

    fun removeFromCart(product: Product) {
        _cartItems.value = _cartItems.value.filterNot { it.id == product.id }
    }

    fun isProductInCart(product: Product): Boolean {
        return _cartItems.value.any { it.id == product.id }
    }

    fun showCartDialog() {
        _isCartDialogVisible.value = true
    }

    fun hideCartDialog() {
        _isCartDialogVisible.value = false
    }
}
