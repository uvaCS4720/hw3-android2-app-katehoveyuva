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

@Composable
fun GameListScreen(viewModel: GameViewModel = viewModel()) {
    // Collecting state from the ViewModel
    val games by viewModel.games.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedGender by viewModel.selectedGender.collectAsState()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "NCAA Scores", style = MaterialTheme.typography.headlineMedium)

                // Add your Date Picker and Toggle buttons here!
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { viewModel.toggleGender() }) {
                        Text(text = "Show $selectedGender")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = selectedDate.toString())
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(games) { game ->
                    // We will build a specific "GameCard" component next
                    Text(text = "${game.awayTeamName} vs ${game.homeTeamName}", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}