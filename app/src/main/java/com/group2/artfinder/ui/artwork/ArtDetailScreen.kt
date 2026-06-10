package com.group2.artfinder.ui.artwork

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.group2.artfinder.data.model.ArtworkItem
import com.group2.artfinder.viewmodel.ArtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtDetailScreen(navController: NavController, artworkId: Int?) {
    val viewModel: ArtViewModel = viewModel()
    val artworks by viewModel.artworks.observeAsState(initial = emptyList<ArtworkItem>())

    val artwork = artworks.find { it.id == artworkId }

    LaunchedEffect(Unit) {
        if (artworks.isEmpty()) viewModel.loadArtworks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artwork Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (artwork == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (!artwork.image_id.isNullOrEmpty()) {
                    val imageUrl =
                        "https://www.artic.edu/iiif/2/${artwork.image_id}/full/843,/0/default.jpg"
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = artwork.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(artwork.title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))

                DetailRow("Artist", artwork.artist_display)
                DetailRow("Type", artwork.artwork_type_title)
                DetailRow("Date", artwork.date_display)
                DetailRow("Medium", artwork.medium_display)
                DetailRow("Gallery", artwork.gallery_title)
                DetailRow("Origin", artwork.place_of_origin)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String?) {
    if (!value.isNullOrEmpty()) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}