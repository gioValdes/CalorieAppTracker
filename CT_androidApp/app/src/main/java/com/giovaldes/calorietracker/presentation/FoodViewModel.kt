import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovaldes.calorietracker.data.AddFoodItemUseCase
import com.giovaldes.calorietracker.data.FoodItem
import com.giovaldes.calorietracker.data.GetFoodItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodViewModel(
    private val addFoodItemUseCase: AddFoodItemUseCase,
    private val getFoodItemsUseCase: GetFoodItemsUseCase
) : ViewModel() {

    private val _foodItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodItems: StateFlow<List<FoodItem>> get() = _foodItems

    private val sampleFoods = listOf(
        FoodItem(0, "🍫 Chocolate", 395, System.currentTimeMillis()),
        FoodItem(1, "🍎 Apple", 95, System.currentTimeMillis()),
        FoodItem(2, "🍌 Banana", 105, System.currentTimeMillis()),
        FoodItem(3, "🥪 Sandwich", 250, System.currentTimeMillis())
    )

    private var currentIndex = 0
    private val _totalCalories = MutableStateFlow(0)
    val totalCaloriesFlow: StateFlow<Int> get() = _totalCalories

    init {
        loadFoodItems()
    }

    fun addFoodItem() {
        viewModelScope.launch {
            val foodItem = sampleFoods[currentIndex]
            addFoodItemUseCase(foodItem) // Añadimos el alimento al repositorio
            _totalCalories.value += foodItem.calories // Sumamos las calorías
            currentIndex = (currentIndex + 1) % sampleFoods.size // Índice cíclico
            loadFoodItems() // Actualizamos la lista observable
        }
    }

    private fun loadFoodItems() {
        viewModelScope.launch {
            _foodItems.value = getFoodItemsUseCase()
        }
    }
}
