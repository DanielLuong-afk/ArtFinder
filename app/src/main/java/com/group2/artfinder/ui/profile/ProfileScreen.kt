package com.group2.artfinder.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.group2.artfinder.ui.auth.artTextFieldColors
import com.group2.artfinder.ui.theme.*
import com.group2.artfinder.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()
    val profile by viewModel.profile.observeAsState()

    var nameInput by remember { mutableStateOf("") }
    var saved     by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.loadProfile() }
    LaunchedEffect(profile) {
        profile?.get("name")?.let { nameInput = it.toString() }
    }

    val email  = profile?.get("email")?.toString() ?: ""
    val points = (profile?.get("points") as? Long)?.toInt() ?: 0
    val initial = nameInput.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MuseumBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Brush.verticalGradient(listOf(MuseumDark, MuseumBlack)))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement   = Arrangement.Center,
                    horizontalAlignment   = Alignment.CenterHorizontally
                ) {
                    // Avatar circle
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(GoldDim, MuseumSurface))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(initial, color = Gold, fontSize = 28.sp)
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(nameInput, style = MaterialTheme.typography.headlineSmall, color = OffWhite)
                    Text(email, style = MaterialTheme.typography.bodySmall, color = Muted)
                }
            }

            // Points badge
            Spacer(Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                color    = MuseumCard
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("✦", color = Gold, fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "$points pts",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Gold
                        )
                        Text("Collection points", style = MaterialTheme.typography.bodySmall, color = Muted)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Edit profile card
            Surface(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                color    = MuseumCard
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Edit Profile",
                        style = MaterialTheme.typography.titleMedium,
                        color = Gold
                    )
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value         = nameInput,
                        onValueChange = { nameInput = it; saved = false },
                        label         = { Text("Display Name") },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = artTextFieldColors()
                    )
                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick  = { viewModel.updateName(nameInput); saved = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = MuseumBlack)
                    ) {
                        Text("Save Changes", style = MaterialTheme.typography.titleMedium)
                    }

                    if (saved) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "✓  Profile updated",
                            color     = Gold,
                            style     = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier  = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Logout
            OutlinedButton(
                onClick  = {
                    viewModel.logout()
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                border   = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
            ) {
                Text("Sign Out", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
