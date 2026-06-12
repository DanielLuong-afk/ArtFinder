package com.group2.artfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.group2.artfinder.ui.theme.*
import com.group2.artfinder.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val authResult by viewModel.authResult.observeAsState()

    var email        by remember { mutableStateOf("") }
    var password     by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authResult) {
        if (authResult == true) {
            navController.navigate("artList") { popUpTo("login") { inclusive = true } }
        } else if (authResult == false) {
            errorMessage = "Incorrect email or password."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MuseumBlack, MuseumDark, MuseumSurface)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Text(
                text  = "✦",
                color = Gold,
                fontSize = 36.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text  = "ArtFinder",
                style = MaterialTheme.typography.displayMedium,
                color = OffWhite
            )
            Text(
                text      = "Discover the world's great artworks",
                style     = MaterialTheme.typography.bodyMedium,
                color     = Muted,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            Surface(
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(20.dp),
                color         = MuseumCard,
                tonalElevation = 0.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "Welcome back",
                        style = MaterialTheme.typography.headlineSmall,
                        color = OffWhite
                    )
                    Text(
                        "Sign in to your collection",
                        style = MaterialTheme.typography.bodySmall,
                        color = Muted
                    )
                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value         = email,
                        onValueChange = { email = it },
                        label         = { Text("Email") },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = artTextFieldColors()
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value                = password,
                        onValueChange        = { password = it },
                        label                = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier             = Modifier.fillMaxWidth(),
                        shape                = RoundedCornerShape(12.dp),
                        colors               = artTextFieldColors()
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Text(errorMessage, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick  = { viewModel.login(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = MuseumBlack)
                    ) {
                        Text("Sign In", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("New to ArtFinder?", color = Muted, style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Create account", color = Gold, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun artTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = Gold,
    unfocusedBorderColor = Muted,
    focusedLabelColor    = Gold,
    unfocusedLabelColor  = Muted,
    focusedTextColor     = OffWhite,
    unfocusedTextColor   = OffWhite,
    cursorColor          = Gold,
    unfocusedContainerColor = MuseumDark,
    focusedContainerColor   = MuseumDark
)
