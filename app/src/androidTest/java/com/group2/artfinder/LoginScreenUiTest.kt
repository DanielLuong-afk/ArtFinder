package com.group2.artfinder

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.group2.artfinder.ui.screens.LoginScreen
import com.group2.artfinder.ui.screens.RegisterScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    private fun setContent() {
        composeTestRule.setContent {
            navController = TestNavHostController(ApplicationProvider.getApplicationContext())
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("register") { RegisterScreen(navController) }
                    composable("artList") { }
                }
            }
        }
    }

    @Test
    fun loginScreenDisplaysCoreFields() {
        setContent()
        composeTestRule.onNodeWithText("Welcome back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign In").assertIsDisplayed()
    }

    @Test
    fun tappingCreateAccountNavigatesToRegister() {
        setContent()
        composeTestRule.onNodeWithText("Create account").performClick()
        composeTestRule.waitForIdle()
        assertEquals("register", navController.currentDestination?.route)
    }
}