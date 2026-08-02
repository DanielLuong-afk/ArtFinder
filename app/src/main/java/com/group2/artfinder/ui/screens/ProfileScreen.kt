package com.group2.artfinder.ui.screens

import androidx.compose.foundation.BorderStroke
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
import com.group2.artfinder.data.UpdateResult
import com.group2.artfinder.ui.theme.*
import com.group2.artfinder.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()
    val user         by viewModel.user.observeAsState()
    val updateResult by viewModel.updateResult.observeAsState()
    val isLoading    by viewModel.isLoading.observeAsState(false)

    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var username  by remember { mutableStateOf("") }
    var dob       by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.loadProfile() }

    LaunchedEffect(user) {
        user?.let {
            firstName = it.firstName
            lastName  = it.lastName
            username  = it.username
            dob       = it.dob
        }
    }

    // Consume update result
    LaunchedEffect(updateResult) {
        if (updateResult != null && updateResult != UpdateResult.Success) {
            // errors are shown inline — just keep result alive for the UI
        }
        // Success: profile auto-reloads via ViewModel; we clear after showing the toast
    }

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Brush.verticalGradient(listOf(MuseumDark, MuseumBlack)))
            ) {
                Column(
                    modifier            = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(GoldDim, MuseumSurface))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(user?.initial ?: "?", color = Gold, fontSize = 28.sp)
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        user?.displayName ?: "",
                        style = MaterialTheme.typography.headlineSmall,
                        color = OffWhite
                    )
                    if (username.isNotEmpty()) {
                        Text("@$username", style = MaterialTheme.typography.bodySmall, color = Gold)
                    }
                    Text(user?.email ?: "", style = MaterialTheme.typography.bodySmall, color = Muted)
                }
            }

            Spacer(Modifier.height(16.dp))
            Surface(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                color    = MuseumCard
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✦", color = Gold, fontSize = 28.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "${user?.points ?: 0} pts",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Gold
                            )
                            Text("Collection points", style = MaterialTheme.typography.bodySmall, color = Muted)
                        }
                    }

                    val badge = user?.badge ?: "Explorer"
                    if (badge.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(color = MuseumSurface, thickness = 0.5.dp)
                        Spacer(Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val badgeEmoji = when (badge) {
                                "Curator"   -> "🏛️"
                                "Archivist" -> "📜"
                                else        -> "🧭"
                            }
                            Text(badgeEmoji, style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "$badge Badge",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Gold
                                )
                                val badgeDesc = when (badge) {
                                    "Curator"   -> "101–250 pts · Keep exploring!"
                                    "Archivist" -> "251–500 pts · Master collector!"
                                    else        -> "0–100 pts · Your journey begins"
                                }
                                Text(badgeDesc, style = MaterialTheme.typography.bodySmall, color = Muted)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Surface(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                color    = MuseumCard
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Edit Profile", style = MaterialTheme.typography.titleMedium, color = Gold)
                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value         = firstName,
                            onValueChange = { firstName = it },
                            label         = { Text("First Name") },
                            modifier      = Modifier.weight(1f),
                            shape         = RoundedCornerShape(12.dp),
                            colors        = artTextFieldColors()
                        )
                        OutlinedTextField(
                            value         = lastName,
                            onValueChange = { lastName = it },
                            label         = { Text("Last Name") },
                            modifier      = Modifier.weight(1f),
                            shape         = RoundedCornerShape(12.dp),
                            colors        = artTextFieldColors()
                        )
                    }
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value         = username,
                        onValueChange = { username = it },
                        label         = { Text("Username") },
                        placeholder   = { Text("e.g. art_lover42", color = Muted) },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = artTextFieldColors(),
                        isError       = updateResult == UpdateResult.UsernameTaken,
                        supportingText = if (updateResult == UpdateResult.UsernameTaken) {
                            { Text("Username already taken", color = ErrorRed) }
                        } else null
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value         = dob,
                        onValueChange = { dob = it },
                        label         = { Text("Date of Birth") },
                        placeholder   = { Text("YYYY-MM-DD", color = Muted) },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = artTextFieldColors()
                    )
                    Spacer(Modifier.height(16.dp))

                    if (updateResult is UpdateResult.Error) {
                        Text(
                            (updateResult as UpdateResult.Error).message,
                            color = ErrorRed,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    Button(
                        onClick  = {
                            viewModel.clearUpdateResult()
                            viewModel.updateProfile(firstName, lastName, username, dob)
                        },
                        enabled  = !isLoading,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = MuseumBlack)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = MuseumBlack, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Save Changes", style = MaterialTheme.typography.titleMedium)
                        }
                    }

                    if (updateResult == UpdateResult.Success) {
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

            OutlinedButton(
                onClick  = {
                    viewModel.logout()
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                },
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(48.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                border   = BorderStroke(1.dp, ErrorRed)
            ) {
                Text("Sign Out", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}