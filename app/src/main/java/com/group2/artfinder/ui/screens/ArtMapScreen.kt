package com.group2.artfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.group2.artfinder.ui.theme.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

@Composable
fun ArtMapScreen(
    navController: NavController,
    title:         String,
    latitude:      Double,
    longitude:     Double,
    isVisited:     Boolean
) {
    val artLocation    = LatLng(latitude, longitude)
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(artLocation, 15f)
    }

    val context = androidx.compose.ui.platform.LocalContext.current

    val hasPermission = androidx.core.content.ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    var locationEnabled by remember { mutableStateOf(hasPermission) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationEnabled = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MuseumBlack)
    ) {
        GoogleMap(
            modifier            = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition,
            properties = MapProperties(isMyLocationEnabled = locationEnabled),
            uiSettings = MapUiSettings(
                zoomControlsEnabled     = true,
                myLocationButtonEnabled = locationEnabled
            )
        ) {
            Marker(
                state   = MarkerState(position = artLocation),
                title   = title,
                snippet = if (isVisited) "✓ You visited this!" else "Artwork Location",
                icon    = BitmapDescriptorFactory.defaultMarker(
                    if (isVisited) BitmapDescriptorFactory.HUE_GREEN
                    else BitmapDescriptorFactory.HUE_ORANGE
                )
            )
        }

        IconButton(
            onClick  = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .background(MuseumBlack.copy(alpha = 0.7f), RoundedCornerShape(50))
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = OffWhite
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MuseumCard
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = OffWhite)
                Spacer(Modifier.height(4.dp))
                Text(
                    if (isVisited) "✦ Visited artwork" else "✦ Artwork location",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isVisited) Gold else Muted
                )
            }
        }
    }
}