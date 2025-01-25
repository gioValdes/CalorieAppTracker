package com.giovaldes.calorietracker

import io.mockk.*
import FoodViewModel
import app.cash.turbine.test
import com.giovaldes.calorietracker.data.AddFoodItemUseCase
import com.giovaldes.calorietracker.data.FoodItem
import com.giovaldes.calorietracker.data.GetFoodItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before
import kotlinx.coroutines.test.*
import org.junit.After


@ExperimentalCoroutinesApi
class FoodViewModelTest {

    private lateinit var viewModel: FoodViewModel
    private val addFoodItemUseCase: AddFoodItemUseCase = mockk()
    private val getFoodItemsUseCase: GetFoodItemsUseCase = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FoodViewModel(addFoodItemUseCase, getFoodItemsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addFoodItem should add food and update total calories`() = runTest {
        // Mock getFoodItemsUseCase to return an empty list initially
        coEvery { getFoodItemsUseCase() } returns emptyList()

        // Mock addFoodItemUseCase to just complete without action
        coEvery { addFoodItemUseCase(any()) } just Runs

        // Observe the totalCaloriesFlow and foodItems
        viewModel.totalCaloriesFlow.test {
            // Initial state
            assertEquals(0, awaitItem())

            // Add a food item (Chocolate, 395 cal)
            viewModel.addFoodItem()

            // Verify addFoodItemUseCase is called with the correct FoodItem
            coVerify { addFoodItemUseCase(FoodItem(0, "🍫 Chocolate", 395)) }

            // Check if total calories are updated
            assertEquals(395, awaitItem())
        }

        viewModel.foodItems.test {
            // Wait for loadFoodItems to update
            assertEquals(emptyList<FoodItem>(), awaitItem()) // Initial empty state

            // Simulate loadFoodItems being called after adding the item
            coEvery { getFoodItemsUseCase() } returns listOf(FoodItem(0, "🍫 Chocolate", 395))
            viewModel.loadFoodItems()

            // Verify the updated list of food items
            assertEquals(
                listOf(FoodItem(0, "🍫 Chocolate", 395)),
                awaitItem()
            )
        }
    }
}
