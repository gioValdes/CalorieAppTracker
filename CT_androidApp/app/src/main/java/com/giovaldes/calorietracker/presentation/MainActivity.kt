package com.giovaldes.calorietracker.presentation

import com.google.firebase.FirebaseApp

import FoodViewModel
import android.content.ContentValues.TAG
import androidx.compose.material3.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.giovaldes.calorietracker.data.AddFoodItemUseCase

import com.giovaldes.calorietracker.data.GetFoodItemsUseCase
import com.giovaldes.calorietracker.domain.FoodDataSource
import com.giovaldes.calorietracker.domain.FoodRepositoryImpl
import com.giovaldes.calorietracker.ui.theme.CalorieTrackerTheme
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.ui.graphics.Color
import com.giovaldes.calorietracker.R
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

var colorStyle = Color.Gray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        val colorMap = mapOf(
            "whiteStyle" to Color.White,
            "blackStyle" to Color.Black,
            "100" to Color.Gray
        )

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val colorString = remoteConfig.getString("style") ?: "blue"
                    val bol = remoteConfig.getBoolean("active") ?: false // TODO desactivar usuario
                    Log.d("colorString", colorString)
                    val color = colorMap[colorString] ?: Color.Red
                    colorStyle = color

                    val updated = task.result
                    Log.d(TAG, "Config params updated: $updated")
                    Toast.makeText(
                        this,
                        "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Fetch failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }





        val dataSource = FoodDataSource()
        val repository = FoodRepositoryImpl(dataSource)
        val addFoodItemUseCase = AddFoodItemUseCase(repository)
        val getFoodItemsUseCase = GetFoodItemsUseCase(repository)
        val viewModel = FoodViewModel(addFoodItemUseCase, getFoodItemsUseCase)


        setContent {
            CalorieTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FoodTrackerScreen(
                        viewModel = viewModel,
                        paddingValues = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun FoodTrackerScreen(
    viewModel: FoodViewModel,
    paddingValues: PaddingValues
)
{
    val foodItems = viewModel.foodItems.collectAsState(initial = emptyList()).value
    val totalCalories = viewModel.totalCaloriesFlow.collectAsState(initial = 0).value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.addFoodItem()
            }) {
                Text("+", textAlign = TextAlign.Center)
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .background(colorStyle)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🚨 Calorie Tracker 🚨",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Total Calories: $totalCalories",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight()
            ) {
                items(foodItems) { foodItem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = foodItem.name, fontSize = 18.sp)
                            Text(text = "${foodItem.calories} cal", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}