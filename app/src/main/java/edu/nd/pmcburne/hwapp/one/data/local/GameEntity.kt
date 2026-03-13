package edu.nd.pmcburne.hwapp.one.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity (
    @PrimaryKey val gameId: String,
    val dateSelected: String, // e.g., "2026/02/17"
    val gender: String,       // "men" or "women"
    val homeTeamName: String,
    val homeTeamScore: String,
    val awayTeamName: String,
    val awayTeamScore: String,
    val gameState: String,    // e.g., "final" or "live"
    val currentPeriod: String,
    val contestClock: String
)