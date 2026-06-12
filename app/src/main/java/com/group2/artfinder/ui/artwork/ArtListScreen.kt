package com.group2.artfinder.ui.artwork

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.group2.artfinder.data.model.ArtworkItem
import com.group2.artfinder.ui.theme.*
import com.group2.artfinder.viewmodel.ArtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtListScreen(navController: NavController) {
    val viewModel: ArtViewModel = viewModel()
    val artworks  by viewModel.artworks.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)

    var searchQuery by remember { mutableStateOf("") }

    //    Only search when the user has stopped typing in the search box for 600ms
    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty()) {
            viewModel.loadArtworks()
        } else {
            kotlinx.coroutines.delay(600)
            viewModel.searchArtworks(searchQuery)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MuseumBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(MuseumDark, MuseumBlack))
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        text  = "✦  ArtFinder",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Gold
                    )
                    Text(
                        text  = "Explore the world's greatest artworks",
                        style = MaterialTheme.typography.bodySmall,
                        color = Muted
                    )
                    Spacer(Modifier.height(16.dp))

                    // Search bar
                    OutlinedTextField(
                        value         = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            if (it.isEmpty()) viewModel.loadArtworks()
                            else viewModel.searchArtworks(it)
                        },
                        placeholder   = { Text("Search artworks, artists…", color = Muted) },
                        leadingIcon   = { Icon(Icons.Default.Search, contentDescription = null, tint = Gold) },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(14.dp),
                        singleLine    = true,
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = Gold,
                            unfocusedBorderColor    = MuseumCard,
                            focusedTextColor        = OffWhite,
                            unfocusedTextColor      = OffWhite,
                            cursorColor             = Gold,
                            focusedContainerColor   = MuseumCard,
                            unfocusedContainerColor = MuseumCard
                        )
                    )
                }
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Gold)
                        Spacer(Modifier.height(12.dp))
                        Text("Loading collection…", color = Muted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding    = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(artworks) { artwork ->
                        ArtworkListItem(artwork) {
                            navController.navigate("artDetail/${artwork.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArtworkListItem(artwork: ArtworkItem, onClick: () -> Unit) {
    Surface(
        modifier      = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape         = RoundedCornerShape(16.dp),
        color         = MuseumCard,
        tonalElevation = 0.dp
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {

            // Thumbnail
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(MuseumSurface)
            ) {
                if (!artwork.image_id.isNullOrEmpty()) {
                    val url = "https://www.artic.edu/iiif/2/${artwork.image_id}/full/200,/0/default.jpg"
                    AsyncImage(
                        model          = url,
                        contentDescription = artwork.title,
                        contentScale   = ContentScale.Crop,
                        modifier       = Modifier.fillMaxSize()
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("✦", color = GoldDim, style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }

            // Info
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .weight(1f)
            ) {
                if (!artwork.artwork_type_title.isNullOrEmpty()) {
                    Text(
                        text  = artwork.artwork_type_title.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Gold
                    )
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    text     = artwork.title,
                    style    = MaterialTheme.typography.titleMedium,
                    color    = OffWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (!artwork.artist_display.isNullOrEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text     = artwork.artist_display,
                        style    = MaterialTheme.typography.bodySmall,
                        color    = Muted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (!artwork.date_display.isNullOrEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text  = artwork.date_display,
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldDim
                    )
                }
            }
        }
    }
}
