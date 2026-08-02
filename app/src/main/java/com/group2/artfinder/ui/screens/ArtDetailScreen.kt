package com.group2.artfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.group2.artfinder.model.ArtworkItem
import com.group2.artfinder.ui.theme.*
import com.group2.artfinder.viewmodel.ArtViewModel
import com.group2.artfinder.viewmodel.VisitedArtworkViewModel
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtDetailScreen(
    navController:    NavController,
    artworkId:        Int?,
    viewModel:        ArtViewModel,
    visitedViewModel: VisitedArtworkViewModel
) {

    val artworks by viewModel.artworks.observeAsState(initial = emptyList<ArtworkItem>())
    val artwork  = artworks.find { it.id == artworkId }

    val isVisited by visitedViewModel.isVisited.observeAsState(false)

    LaunchedEffect(artworkId) {
        artworkId?.let { visitedViewModel.checkIfVisited(it) }
    }

    LaunchedEffect(Unit) {
        if (artworks.isEmpty()) viewModel.loadArtworks()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MuseumBlack)
    ) {
        if (artwork == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Gold)
                    Spacer(Modifier.height(12.dp))
                    Text("Loading artwork…", color = Muted, style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    if (!artwork.image_id.isNullOrEmpty()) {
                        val imageUrl = "https://www.artic.edu/iiif/2/${artwork.image_id}/full/843,/0/default.jpg"
                        AsyncImage(
                            model              = imageUrl,
                            contentDescription = artwork.title,
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            Modifier.fillMaxSize().background(MuseumSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✦", color = GoldDim, style = MaterialTheme.typography.displayLarge)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, MuseumBlack)
                                )
                            )
                    )

                    IconButton(
                        onClick  = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(MuseumBlack.copy(alpha = 0.5f), RoundedCornerShape(50))
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OffWhite
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    if (!artwork.artwork_type_title.isNullOrEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = GoldDim.copy(alpha = 0.35f)
                        ) {
                            Text(
                                text     = artwork.artwork_type_title.uppercase(),
                                style    = MaterialTheme.typography.labelSmall,
                                color    = Gold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                    }

                    Text(
                        text  = artwork.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = OffWhite
                    )


                    if (!artwork.artist_display.isNullOrEmpty()) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text      = artwork.artist_display,
                            style     = MaterialTheme.typography.bodyMedium,
                            color     = Muted,
                            fontStyle = FontStyle.Italic
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Surface(
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(16.dp),
                        color         = MuseumCard
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Artwork Details",
                                style = MaterialTheme.typography.titleMedium,
                                color = Gold
                            )
                            Spacer(Modifier.height(12.dp))

                            DetailRow("Date",    artwork.date_display)
                            DetailRow("Medium",  artwork.medium_display)
                            DetailRow("Gallery", artwork.gallery_title)
                            DetailRow("Origin",  artwork.place_of_origin)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick  = { artwork?.let { visitedViewModel.addVisitedArtwork(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = if (isVisited) GoldDim else Gold,
                            contentColor   = MuseumBlack
                        )
                    ) {
                        Text(
                            if (isVisited) "✓  Added to Collection" else "✦  Mark as Visited",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick  = {
                            val lat = artwork.latitude ?: 41.8796
                            val lng = artwork.longitude ?: -87.6237
                            navController.navigate("artMap/${artwork.title}/$lat/$lng/$isVisited")
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape    = RoundedCornerShape(12.dp),
                        border   = androidx.compose.foundation.BorderStroke(1.dp, Gold),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = Gold)
                    ) {
                        Text("✦  View on Map", style = MaterialTheme.typography.titleMedium)
                    }

                    if (isVisited) {
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick  = {
                                navController.navigate("artworkPhotos/${artwork.id}/${artwork.title}")
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape    = RoundedCornerShape(12.dp),
                            border   = androidx.compose.foundation.BorderStroke(1.dp, Gold),
                            colors   = ButtonDefaults.outlinedButtonColors(contentColor = Gold)
                        ) {
                            Text("✦  My Visit Photos", style = MaterialTheme.typography.titleMedium)
                        }
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String?) {
    if (!value.isNullOrEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.Top
        ) {
            Text(
                text     = label.uppercase(),
                style    = MaterialTheme.typography.labelSmall,
                color    = Muted,
                modifier = Modifier.width(72.dp)
            )
            Text(
                text     = value,
                style    = MaterialTheme.typography.bodyMedium,
                color    = OffWhite,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(color = MuseumSurface, thickness = 0.5.dp)
    }
}
