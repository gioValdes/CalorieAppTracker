package com.giovaldes.calorietracker

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.giovaldes.calorietracker.presentation.MainActivity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.giovaldes.calorietracker", appContext.packageName)
    }

    // Regla para iniciar MainActivity
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testInitialUIState() {
        // Verifica que el texto "🚨 Calorie Tracker 🚨" esté visible
        composeTestRule.onNodeWithText("🚨 Calorie Tracker 🚨").assertExists()

        // Verifica que el texto "Total Calories: 0" esté visible
        composeTestRule.onNodeWithText("Total Calories: 0").assertExists()
    }

    @Test
    fun testAddFoodItem() {
        // Haz clic en el botón de agregar comida
        composeTestRule.onNodeWithText("+").performClick()

        // Verifica que se haya agregado un ítem de comida
        //composeTestRule.onNodeWithText("🍫 Chocolate 395 cal").assertExists()
        composeTestRule.onNodeWithText("Total Calories: 395").assertExists()
    }

    @Test
    fun testTotalCaloriesUpdate() {
        // Haz clic en el botón de agregar comida dos veces
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("+").performClick()

        // Verifica que el total de calorías se haya actualizado correctamente
        composeTestRule.onNodeWithText("Total Calories: 490").assertExists()
    }


}