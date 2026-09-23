package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
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
        composeTestRule.onNodeWithTag("home_top_screen_badge").assertIsDisplayed()
        composeTestRule.onNodeWithText("60+fit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Musculação e Funcionalidade").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movimento, força e autonomia para envelhecer com mais segurança.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Opções e recursos:").assertIsDisplayed()

        // Verify menu options exist
        composeTestRule.onNodeWithTag("menu_item_safety_check").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_profile").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_assessment").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_workout").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_condition_workouts").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_video_library").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_evolution").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_health").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_plans").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_reminders").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("menu_item_contact_professional").performScrollTo().assertIsDisplayed()

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
    }
}
