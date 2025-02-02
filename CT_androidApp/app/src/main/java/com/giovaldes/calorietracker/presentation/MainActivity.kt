package com.giovaldes.calorietracker.presentation

import FoodViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.giovaldes.calorietracker.R
import com.giovaldes.calorietracker.data.AddFoodItemUseCase
import com.giovaldes.calorietracker.data.GetFoodItemsUseCase
import com.giovaldes.calorietracker.domain.FoodDataSource
import com.giovaldes.calorietracker.domain.FoodRepositoryImpl
import com.giovaldes.calorietracker.ui.theme.CalorieTrackerTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, getString(R.string.camera_permission_granted), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // Evitar capturas de pantalla y previsualización en multitarea
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE,
        )

        // Solicitar permisos en tiempo de ejecución si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        val dataSource = FoodDataSource()
        val repository = FoodRepositoryImpl(dataSource)
        val addFoodItemUseCase = AddFoodItemUseCase(repository)
        val getFoodItemsUseCase = GetFoodItemsUseCase(repository)
        val foodViewModel = FoodViewModel(addFoodItemUseCase, getFoodItemsUseCase)
        val mainViewModel = MainViewModel(application)

        setContent {
            CalorieTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FoodTrackerScreen(
                        mainViewModel = mainViewModel,
                        foodViewModel = foodViewModel,
                        paddingValues = innerPadding,
                    )
                }
            }
        }
    }
}

@Composable
fun FoodTrackerScreen(
    mainViewModel: MainViewModel,
    foodViewModel: FoodViewModel,
    paddingValues: PaddingValues,
) {
    val context = LocalContext.current // Obtén el contexto actual
    val foodItems = foodViewModel.foodItems.collectAsState(initial = emptyList()).value
    val totalCalories = foodViewModel.totalCaloriesFlow.collectAsState(initial = 0).value
    val totalCaloriesText = stringResource(R.string.total_calories)
    val backgroundColor = mainViewModel.colorStyle.collectAsState(initial = Color.Gray).value
    val isActive = mainViewModel.isActive.collectAsState(initial = true).value
    val title = mainViewModel.optionTitle.collectAsState(initial = "Calorie Tracker").value
    val showAlert = !isActive

    LaunchedEffect(isActive) {
        if (showAlert) {
            Toast.makeText(
                context,
                context.getString(R.string.feature_is_inactive),
                Toast.LENGTH_LONG,
            )
                .show()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                foodViewModel.addFoodItem()
            }) {
                Text("+", textAlign = TextAlign.Center)
            }
        },
    ) { innerPadding ->

        Column(
            modifier =
                Modifier
                    .background(backgroundColor)
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(innerPadding)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Text(
                text = "$totalCaloriesText : $totalCalories",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
            ) {
                items(foodItems) { foodItem ->
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
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
