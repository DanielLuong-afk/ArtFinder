package com.group2.artfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.group2.artfinder.model.VisitedArtwork
import com.group2.artfinder.ui.theme.*
import com.group2.artfinder.viewmodel.VisitedArtworkViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VisitedArtworkScreen(
    navController:   NavController,
    visitedViewModel: VisitedArtworkViewModel
) {
    val artworks  by visitedViewModel.visitedArtworks.observeAsState(emptyList())
    val isLoading by visitedViewModel.isLoading.observeAsState(false)

    LaunchedEffect(Unit) { visitedViewModel.loadVisitedArtworks() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MuseumBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(MuseumDark, MuseumBlack)))
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Text("✦  My Collection", style = MaterialTheme.typography.headlineMedium, color = Gold)
                    Text("Artworks you've visited", style = MaterialTheme.typography.bodySmall, color = Muted)
                }
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Gold)
                }
            } else if (artworks.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✦", color = GoldDim, style = MaterialTheme.typography.displayMedium)
                        Spacer(Modifier.height(12.dp))
                        Text("No visited artworks yet", style = MaterialTheme.typography.titleMedium, color = Muted)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Mark artworks as visited to see them here",
                            style = MaterialTheme.typography.bodySmall,
                            color = GoldDim
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(artworks, key = { it.id }) { artwork ->
                        VisitedArtworkItem(
                            artwork   = artwork,
                            onDelete  = { visitedViewModel.deleteVisitedArtwork(artwork.id) },
                            onViewMap = {
                                val lat = artwork.latitude ?: 41.8796
                                val lng = artwork.longitude ?: -87.6237
                                navController.navigate("artMap/${artwork.title}/$lat/$lng/true")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VisitedArtworkItem(
    artwork:   VisitedArtwork,
    onDelete:  () -> Unit,
    onViewMap: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor   = MuseumCard,
            title = { Text("Remove from collection?", color = OffWhite) },
            text  = { Text("Remove \"${artwork.title}\" from your visited artworks?", color = Muted) },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteDialog = false }) {
                    Text("Remove", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Gold)
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        color    = MuseumCard
    ) {
        Column {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 16.dp))
                        .background(MuseumSurface)
                ) {
                    if (!artwork.image_id.isNullOrEmpty()) {
                        AsyncImage(
                            model              = "https://www.artic.edu/iiif/2/${artwork.image_id}/full/200,/0/default.jpg",
                            contentDescription = artwork.title,
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("✦", color = GoldDim, style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    artwork.artwork_type_title?.takeIf { it.isNotEmpty() }?.let {
                        Text(it.uppercase(), style = MaterialTheme.typography.labelSmall, color = Gold)
                        Spacer(Modifier.height(2.dp))
                    }
                    Text(
                        artwork.title,
                        style    = MaterialTheme.typography.titleMedium,
                        color    = OffWhite,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    artwork.artist_display?.takeIf { it.isNotEmpty() }?.let {
                        Spacer(Modifier.height(2.dp))
                        Text(it, style = MaterialTheme.typography.bodySmall, color = Muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    artwork.visitedAt?.let {
                        Spacer(Modifier.height(4.dp))
                        val date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it.toDate())
                        Text("Visited $date", style = MaterialTheme.typography.labelSmall, color = GoldDim)
                    }
                }

                IconButton(
                    onClick  = { showDeleteDialog = true },
                    modifier = Modifier.align(Alignment.Top)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed)
                }
            }


            HorizontalDivider(color = MuseumSurface, thickness = 0.5.dp)
            TextButton(
                onClick  = onViewMap,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Text("✦  View on Map", color = Gold, style = MaterialTheme.typography.bodySmall)
            }

        }
    }
}