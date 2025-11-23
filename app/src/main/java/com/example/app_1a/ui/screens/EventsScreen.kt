package com.example.app_1a.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_1a.service.NotificationService

// Data class para representar un evento
data class GameEvent(
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)

// Lista de eventos de ejemplo
val sampleEvents = listOf(
    GameEvent("Torneo Nacional de Valorant", "Compite contra los mejores equipos del país por la gloria y grandes premios.", -33.4489, -70.6693),
    GameEvent("Retro Gaming Fest 2025", "Vuelve a vivir la magia de los clásicos. Arcades, consolas retro y charlas.", -35.6751, -71.5430),
    GameEvent("Lanzamiento de 'Cybernetic Dawn'", "Sé el primero en jugar el título más esperado del año. Habrá sorteos y cosplay.", -36.8269, -73.0498),
    GameEvent("Junta Comunitaria de League of Legends", "Conoce a otros jugadores y participa en torneos amistosos 1v1.", -33.0458, -71.6197)
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventsScreen(notificationService: NotificationService) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // --- ANIMACIÓN AÑADIDA AQUÍ ---
            // Cada tarjeta ahora se animará al aparecer en la lista.
            items(sampleEvents, key = { it.title }) { event ->
                EventCard(
                    event = event,
                    modifier = Modifier.animateItemPlacement(
                        tween(durationMillis = 300) // Duración de la animación
                    ),
                    onLocationClick = {
                        val gmmIntentUri = Uri.parse("geo:${event.latitude},${event.longitude}?q=${event.latitude},${event.longitude}(${event.title})")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    },
                    onNotificationClick = {
                        notificationService.showEventNotification(event.title)
                    }
                )
            }
        }
    }
}

@Composable
fun EventCard(
    event: GameEvent,
    modifier: Modifier = Modifier,
    onLocationClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(), // Usamos el modifier para la animación
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(event.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onLocationClick) {
                    Text("Ubicación del evento")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onNotificationClick) {
                    Text("Notifícame")
                }
            }
        }
    }
}

