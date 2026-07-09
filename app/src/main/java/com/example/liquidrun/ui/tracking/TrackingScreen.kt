package com.example.liquidrun.ui.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@Composable
fun TrackingScreen(onFinishTracking: () -> Unit) {
    var isTracking by remember { mutableStateOf(false) }
    var distance by remember { mutableStateOf(0.0) }
    var timeSeconds by remember { mutableStateOf(0) }
    var showFinishDialog by remember { mutableStateOf(false) }

    // Mock tracking timer
    LaunchedEffect(isTracking) {
        while(isTracking) {
            delay(1000)
            timeSeconds++
            distance += 0.005 // Mock speed
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Distance Circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = String.format("%.2f", distance),
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text("Kilometer", color = Color.White.copy(alpha = 0.4f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(Color.White.copy(alpha = 0.04f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Zeit", String.format("%02d:%02d", timeSeconds / 60, timeSeconds % 60))
                StatItem("km/h", if (timeSeconds > 0) String.format("%.1f", (distance) / (timeSeconds / 3600.0)) else "—")
                StatItem("Ziel", "—")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mini Map Live
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                val singapore = LatLng(1.35, 103.87)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(singapore, 15f)
                }
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL),
                    uiSettings = MapUiSettings(myLocationButtonEnabled = false, compassEnabled = false)
                ) {
                    // Polyline for Route
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp)) // Space for bottom button
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Button(
                onClick = {
                    if (isTracking) {
                        showFinishDialog = true
                    } else {
                        isTracking = true
                    }
                },
                modifier = Modifier.size(width = 180.dp, height = 60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTracking) Color(0xFFFF0055) else Color(0xFF00FFC6),
                    contentColor = if (isTracking) Color.White else Color.Black
                )
            ) {
                Text(
                    text = if (isTracking) "Stopp" else "Start",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showFinishDialog) {
            AlertDialog(
                onDismissRequest = { showFinishDialog = false },
                title = { Text("Training beenden?") },
                text = { Text("Möchtest du das Training wirklich beenden und speichern?") },
                confirmButton = {
                    TextButton(onClick = {
                        isTracking = false
                        showFinishDialog = false
                        onFinishTracking()
                    }) {
                        Text("Beenden & Speichern")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showFinishDialog = false }) {
                        Text("Weiter laufen")
                    }
                }
            )
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(label, fontSize = 12.sp, color = Color.White.copy(alpha = 0.4f))
    }
}
