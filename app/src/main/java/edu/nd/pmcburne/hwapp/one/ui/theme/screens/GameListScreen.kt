package edu.nd.pmcburne.hwapp.one.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.nd.pmcburne.hwapp.one.viewmodel.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import edu.nd.pmcburne.hwapp.one.data.local.GameEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(viewModel: GameViewModel = viewModel()) {
    val games by viewModel.games.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedGender by viewModel.selectedGender.collectAsState()

    // State to show/hide the date picker dialog
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // When the user confirms a date in the dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        // Convert millis to LocalDate and update ViewModel
                        val date = java.time.Instant.ofEpochMilli(selectedMillis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        viewModel.setDate(date)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NCAA Scores") },
                actions = {
                    // Gender Toggle Button
                    TextButton(onClick = { viewModel.toggleGender() }) {
                        Text(selectedGender.uppercase())
                    }
                    // Date Picker Trigger Button
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(imageVector = androidx.compose.material.icons.Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (isLoading && games.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(games) { game ->
                        GameCard(game = game)
                    }
                }
            }
        }
    }


}

@Composable
fun GameCard(game: GameEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 1. Status Row (e.g., "Final", "2nd - 10:45", or "7:00 PM")
            val statusText = when (game.gameState.lowercase()) {
                "final" -> "Final"
                "live" -> "${game.currentPeriod} - ${game.contestClock}"
                else -> game.gameState // Handles start times for upcoming games
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.labelLarge,
                color = if (game.gameState.lowercase() == "live") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 2. Teams and Scores Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Away Team
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = game.awayTeamName, style = MaterialTheme.typography.titleMedium)
                    Text(text = game.awayTeamScore, style = MaterialTheme.typography.headlineSmall)
                }

                Text(
                    text = "vs",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                // Home Team
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = game.homeTeamName, style = MaterialTheme.typography.titleMedium)
                    Text(text = game.homeTeamScore, style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}