package com.giovaldes.calorietracker.domain

import com.giovaldes.calorietracker.data.FoodItem

interface FoodRepository {
    suspend fun addFoodItem(foodItem: FoodItem)

    suspend fun getFoodItems(): List<FoodItem>
}

class FoodRepositoryImpl(private val dataSource: FoodDataSource) : FoodRepository {
    override suspend fun addFoodItem(foodItem: FoodItem) {
        dataSource.insertFoodItem(foodItem)
    }

    override suspend fun getFoodItems(): List<FoodItem> {
        return dataSource.getAllFoodItems()
    }
}

// Simulating a local data source
class FoodDataSource {
    private val foodItems = mutableListOf<FoodItem>()

    suspend fun insertFoodItem(foodItem: FoodItem) {
        foodItems.add(foodItem)
    }

    suspend fun getAllFoodItems(): List<FoodItem> {
        return foodItems
    }
}
