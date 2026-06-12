package com.group2.artfinder.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val authResult by viewModel.authResult.observeAsState()

    var name         by remember { mutableStateOf("") }
    var email        by remember { mutableStateOf("") }
    var password     by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authResult) {
        if (authResult == true) {
            navController.navigate("artList") { popUpTo("register") { inclusive = true } }
        } else if (authResult == false) {
            errorMessage = "Registration failed. Try a different email."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MuseumBlack, MuseumDark, MuseumSurface)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Gold)
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("✦", color = Gold, fontSize = 36.sp)
            Spacer(Modifier.height(8.dp))
            Text("Join ArtFinder", style = MaterialTheme.typography.headlineLarge, color = OffWhite)
            Text(
                "Start your personal art collection",
                style     = MaterialTheme.typography.bodyMedium,
                color     = Muted,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            Surface(
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(20.dp),
                color         = MuseumCard,
                tonalElevation = 0.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Create your account", style = MaterialTheme.typography.headlineSmall, color = OffWhite)
                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value         = name,
                        onValueChange = { name = it },
                        label         = { Text("Full Name") },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = artTextFieldColors()
                    )
                    Spacer(Modifier.height(12.dp))

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
                        onClick  = { viewModel.register(email, password, name) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = MuseumBlack)
                    ) {
                        Text("Create Account", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account?", color = Muted, style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Sign in", color = Gold, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
