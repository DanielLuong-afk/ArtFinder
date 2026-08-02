package com.group2.artfinder.ui.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.group2.artfinder.ui.theme.*
import com.group2.artfinder.viewmodel.VisitedArtworkViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtworkPhotosScreen(
    navController:    NavController,
    artworkId:        Int,
    artworkTitle:     String,
    visitedViewModel: VisitedArtworkViewModel
) {
    val context       = LocalContext.current
    val photos        by visitedViewModel.photos.observeAsState(emptyList())
    val uploadLoading by visitedViewModel.uploadLoading.observeAsState(false)

    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) { visitedViewModel.loadPhotos(artworkId) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { visitedViewModel.uploadPhoto(context, artworkId, it) {} }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraUri?.let { visitedViewModel.uploadPhoto(context, artworkId, it) {} }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            val uri  = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            cameraUri = uri
            cameraLauncher.launch(uri)
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
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = OffWhite)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(artworkTitle, style = MaterialTheme.typography.titleMedium, color = OffWhite, maxLines = 1)
                        Text("${photos.size} photo${if (photos.size != 1) "s" else ""}", style = MaterialTheme.typography.bodySmall, color = Muted)
                    }
                }
            }

            // Points info card
            Surface(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
                shape    = RoundedCornerShape(12.dp),
                color    = MuseumCard
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("✦", color = Gold, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        val pts = when {
                            photos.size in 1..5  -> "10 pts earned"
                            photos.size in 6..10 -> "20 pts earned"
                            photos.isEmpty()     -> "Upload photos to earn points"
                            else                 -> "Max 20 pts earned"
                        }
                        Text(pts, style = MaterialTheme.typography.bodyMedium, color = Gold)
                        Text(
                            "1–5 photos = 10 pts  ·  6–10 photos = 20 pts",
                            style = MaterialTheme.typography.labelSmall,
                            color = Muted
                        )
                    }
                }
            }

            if (photos.isEmpty() && !uploadLoading) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("✦", color = SageDim, style = MaterialTheme.typography.displayMedium)
                        Spacer(Modifier.height(12.dp))
                        Text("No photos yet", style = MaterialTheme.typography.titleMedium, color = Muted)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Take or upload photos of your visit\nto earn points",
                            style     = MaterialTheme.typography.bodySmall,
                            color     = SageDim,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns         = GridCells.Fixed(2),
                    modifier        = Modifier.weight(1f).padding(horizontal = 12.dp),
                    contentPadding  = PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement   = Arrangement.spacedBy(8.dp)
                ) {
                    items(photos) { photo ->
                        if (photo.startsWith("data:")) {
                            val bitmap = remember(photo) {
                                try {
                                    val base64 = photo.removePrefix("data:image/jpeg;base64,")
                                    val bytes  = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)
                                    android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                } catch (e: Exception) { null }
                            }
                            if (bitmap != null) {
                                Image(
                                    bitmap             = bitmap.asImageBitmap(),
                                    contentDescription = "Visit photo",
                                    contentScale       = ContentScale.Crop,
                                    modifier           = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MuseumCard)
                                )
                            }
                        } else {
                            AsyncImage(
                                model              = photo,
                                contentDescription = "Visit photo",
                                contentScale       = ContentScale.Crop,
                                modifier           = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MuseumCard)
                            )
                        }
                    }
                    if (uploadLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MuseumCard),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Gold)
                            }
                        }
                    }
                }
            }

            // Bottom buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MuseumSurface)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick  = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = MuseumBlack),
                    enabled  = !uploadLoading && photos.size < 10
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Camera")
                }
                OutlinedButton(
                    onClick  = {
                        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            Manifest.permission.READ_MEDIA_IMAGES
                        else
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        galleryLauncher.launch("image/*")
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape    = RoundedCornerShape(12.dp),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, Gold),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = Gold),
                    enabled  = !uploadLoading && photos.size < 10
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Gallery")
                }
            }
        }
    }
}