package edu.nd.pmcburne.hwapp.one.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hwapp.one.data.GameRepository
import edu.nd.pmcburne.hwapp.one.data.RetrofitClient
import edu.nd.pmcburne.hwapp.one.data.local.AppDatabase
import edu.nd.pmcburne.hwapp.one.data.local.GameEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GameViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Initialize the Data Sources
    private val database = AppDatabase.getDatabase(application)
    private val repository = GameRepository(RetrofitClient.apiService, database.gameDao())

    // 2. UI State Variables (These survive screen rotations!)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _games = MutableStateFlow<List<GameEntity>>(emptyList())
    val games: StateFlow<List<GameEntity>> = _games.asStateFlow()

    // Requirement: Initially set to today's date
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _selectedGender = MutableStateFlow("men")
    val selectedGender: StateFlow<String> = _selectedGender.asStateFlow()

    // Keeps track of our database listener so we don't accidentally listen to multiple dates at once
    private var databaseJob: Job? = null

    init {
        // Fetch data immediately when the app launches
        updateData()
    }

    // 3. The Core Update Function
    fun updateData() {
        val date = _selectedDate.value
        val gender = _selectedGender.value

        // Format the date to match the API requirements
        val year = date.format(DateTimeFormatter.ofPattern("yyyy"))
        val month = date.format(DateTimeFormatter.ofPattern("MM"))
        val day = date.format(DateTimeFormatter.ofPattern("dd"))
        val dateString = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        // A. Listen to the local database for offline persistence
        databaseJob?.cancel() // Stop listening to the old date if they changed it
        databaseJob = viewModelScope.launch {
            repository.getGamesStream(dateString, gender).collect { localGames ->
                _games.value = localGames
            }
        }

        // B. Fetch fresh data from the network
        viewModelScope.launch {
            _isLoading.value = true
            repository.refreshGames(gender, year, month, day, dateString)
            _isLoading.value = false
        }
    }

    // 4. User Interaction Functions (The UI will call these)
    fun setDate(newDate: LocalDate) {
        _selectedDate.value = newDate
        updateData()
    }

    fun toggleGender() {
        _selectedGender.value = if (_selectedGender.value == "men") "women" else "men"
        updateData()
    }
}