package com.group2.artfinder.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.group2.artfinder.model.ArtworkItem
import com.group2.artfinder.ui.theme.*
import com.group2.artfinder.viewmodel.ArtViewModel
import com.group2.artfinder.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtListScreen(
    navController: NavController,
    artViewModel:  ArtViewModel
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val user      by profileViewModel.user.observeAsState()
    val artworks  by artViewModel.artworks.observeAsState(emptyList())
    val isLoading by artViewModel.isLoading.observeAsState(false)

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { profileViewModel.loadProfile() }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty()) {
            artViewModel.loadArtworks()
        } else {
            kotlinx.coroutines.delay(600)
            artViewModel.searchArtworks(searchQuery)
        }
    }

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

                    Text(
                        text  = "✦  ArtFinder",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Gold
                    )

                    val greeting = user?.let {
                        val name = it.username.ifEmpty { it.firstName }
                        if (name.isNotEmpty()) "Welcome back, $name" else "Welcome back"
                    } ?: "Welcome back"

                    Text(
                        text  = greeting,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OffWhite
                    )

                    Spacer(Modifier.height(16.dp))

                    // ── Dashboard cards ──────────────────────────────────────
                    Row(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardCard(
                            modifier = Modifier.weight(1f),
                            icon     = "✦",
                            value    = "${user?.points ?: 0}",
                            label    = "Points"
                        )
                        DashboardCard(
                            modifier = Modifier.weight(1f),
                            icon     = "◎",
                            value    = "${user?.visitedCount ?: 0}",
                            label    = "Artworks Visited"
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value         = searchQuery,
                        onValueChange = { searchQuery = it },
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
                    contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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
fun DashboardCard(
    modifier: Modifier = Modifier,
    icon:     String,
    value:    String,
    label:    String
) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(14.dp),
        color    = MuseumCard
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, color = Gold, fontSize = 22.sp)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text  = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = Gold
                )
                Text(
                    text  = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Muted
                )
            }
        }
    }
}

@Composable
fun ArtworkListItem(artwork: ArtworkItem, onClick: () -> Unit) {
    Surface(
        modifier       = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape          = RoundedCornerShape(16.dp),
        color          = MuseumCard,
        tonalElevation = 0.dp
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {

            Box(
                modifier = Modifier
                    .width(90.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
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
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .weight(1f)
            ) {
                artwork.artwork_type_title?.takeIf { it.isNotEmpty() }?.let { type ->
                    Text(
                        text  = type.uppercase(),
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
                artwork.artist_display?.takeIf { it.isNotEmpty() }?.let { artist ->
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text     = artist,
                        style    = MaterialTheme.typography.bodySmall,
                        color    = Muted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                artwork.date_display?.takeIf { it.isNotEmpty() }?.let { date ->
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text  = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldDim
                    )
                }
            }
        }
    }
}