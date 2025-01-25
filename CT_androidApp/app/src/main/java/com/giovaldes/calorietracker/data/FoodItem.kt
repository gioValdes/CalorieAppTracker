package com.giovaldes.calorietracker.data

import com.giovaldes.calorietracker.domain.FoodRepository

// --- Domain Model ---
data class FoodItem(
    val id: Int,
    val name: String,
    val calories: Int
)

// --- Use Case ---
class AddFoodItemUseCase(private val repository: FoodRepository) {
    suspend operator fun invoke(foodItem: FoodItem) {
        repository.addFoodItem(foodItem)
    }
}

class GetFoodItemsUseCase(private val repository: FoodRepository) {
    suspend operator fun invoke(): List<FoodItem> {
        return repository.getFoodItems()
    }
}