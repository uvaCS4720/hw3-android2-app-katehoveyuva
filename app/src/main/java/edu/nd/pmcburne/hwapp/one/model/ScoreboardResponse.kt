package edu.nd.pmcburne.hwapp.one.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// 1. The root response containing the list of games
@Serializable
data class ScoreboardResponse(
    @SerialName("games") val games: List<GameWrapper>
)

// 2. The wrapper object inside the array
@Serializable
data class GameWrapper(
    @SerialName("game") val game: Game
)

// 3. The actual game data
@Serializable
data class Game(
    @SerialName("gameID") val gameId: String,
    @SerialName("gameState") val gameState: String,
    @SerialName("currentPeriod") val currentPeriod: String,
    @SerialName("contestClock") val contestClock: String,
    @SerialName("startTime") val startTime: String,
    @SerialName("home") val homeTeam: TeamDetail,
    @SerialName("away") val awayTeam: TeamDetail
)

// 4. The team details for both Home and Away
@Serializable
data class TeamDetail(
    @SerialName("score") val score: String,
    @SerialName("names") val names: TeamNames,
    @SerialName("winner") val isWinner: Boolean
)

// 5. The specific team names (the assignment mentions team names, and "short" is usually the cleanest for UI)
@Serializable
data class TeamNames(
    @SerialName("short") val shortName: String
)