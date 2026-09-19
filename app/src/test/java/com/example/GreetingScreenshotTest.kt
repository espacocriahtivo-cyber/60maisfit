package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollTo
import com.example.data.AppRepository
import com.example.ui.screens.StudentHomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [34])
class GreetingScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun app_dashboard_screenshot_and_menu_verification() {
        val repository = AppRepository()
        composeTestRule.setContent {
            MyApplicationTheme {
                StudentHomeScreen(
                    profile = repository.studentProfile.value,
                    onStartWorkoutClick = {},
                    onProgressClick = {},
                    onHealthClick = {},
                    onExercisesClick = {},
                    onRemindersClick = {},
                    onPlansClick = {},
                    onAccessibilityClick = {},
                    onNavigateBottom = {}
                )
            }
        }

        // Verify that the requested header texts are present
        composeTestRule.onNodeWithText("Tela inicial").assertIsDisplayed()
        composeTestRule.onNodeWithText("60+fit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Musculação e Funcionalidade").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movimento, força e autonomia para envelhecer com mais segurança.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Menu principal:").assertIsDisplayed()

        // Verify the 9 menu options exist
        composeTestRule.onNodeWithText("Meu Perfil").assertIsDisplayed()
        composeTestRule.onNodeWithText("Minha Avaliação").assertIsDisplayed()
        composeTestRule.onNodeWithText("Meu Treino").assertIsDisplayed()
        composeTestRule.onNodeWithText("Biblioteca de Exercícios").assertIsDisplayed()
        composeTestRule.onNodeWithText("Minha Evolução").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Minha Saúde").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Meu Plano").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Notificações").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Fale com o Profissional").performScrollTo().assertIsDisplayed()

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
    }
}
