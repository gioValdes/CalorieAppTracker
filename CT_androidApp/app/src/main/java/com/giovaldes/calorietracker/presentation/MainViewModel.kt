package com.giovaldes.calorietracker.presentation

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val STYLE_KEY = "style"
        private const val ACTIVE_KEY = "active"
        private const val IS_ALERT_EMOJI_KEY = "isAlertEmoji"
    }

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    private val colorMap =
        mapOf(
            "whiteStyle" to Color.White,
            "blackStyle" to Color.Black,
            "default" to Color.Gray,
        )

    private val _colorStyle = MutableStateFlow(Color.Gray)
    val colorStyle: StateFlow<Color> get() = _colorStyle

    private val _optionTitle = MutableStateFlow("Calorie Tracker")
    val optionTitle: StateFlow<String> get() = _optionTitle

    private val _isActive = MutableStateFlow(true)
    val isActive: StateFlow<Boolean> get() = _isActive

    init {
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        val configSettings =
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf(STYLE_KEY to "default", ACTIVE_KEY to true, IS_ALERT_EMOJI_KEY to false))

        fetchAndActivate()
    }

    private fun fetchAndActivate() {
        viewModelScope.launch {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val colorString = remoteConfig.getString(STYLE_KEY) ?: "default"
                    val showAlertEmoji = remoteConfig.getBoolean(IS_ALERT_EMOJI_KEY)
                    val activeStatus = remoteConfig.getBoolean(ACTIVE_KEY)

                    _colorStyle.value = colorMap[colorString] ?: Color.Red
                    _optionTitle.value = if (showAlertEmoji) "🚨 Calorie Tracker 🚨" else "Calorie Tracker"
                    _isActive.value = activeStatus

                    Log.d("MainViewModel", "Remote config updated: $colorString, $showAlertEmoji, $activeStatus")
                } else {
                    Toast.makeText(getApplication(), "Remote Config fetch failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
