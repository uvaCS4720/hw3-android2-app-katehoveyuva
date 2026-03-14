package edu.nd.pmcburne.hwapp.one.data

import android.util.Log
import edu.nd.pmcburne.hwapp.one.data.local.GameDao
import edu.nd.pmcburne.hwapp.one.data.local.GameEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val apiService: NcaaApiService,
    private val gameDao: GameDao
) {
    fun getGamesStream(dateSelected: String, gender: String): Flow<List<GameEntity>> {
        return gameDao.getGames(dateSelected, gender)
    }

    suspend fun refreshGames(gender: String, year: String, month: String, day: String, dateString: String) {
        try {
            val response = apiService.getScores(gender, year, month, day)
            val entitiesToSave = response.games.map { wrapper ->
                val game = wrapper.game
                GameEntity(
                    gameId = game.gameId,
                    dateSelected = dateString,
                    gender = gender,
                    homeTeamName = game.homeTeam.names.shortName,
                    homeTeamScore = game.homeTeam.score,
                    awayTeamName = game.awayTeam.names.shortName,
                    awayTeamScore = game.awayTeam.score,
                    gameState = game.gameState,
                    currentPeriod = game.currentPeriod,
                    contestClock = game.contestClock
                )
            }
            gameDao.insertGames(entitiesToSave)
        } catch (e: Exception) {
            Log.e("GameRepository", "Network fetch failed: ${e.message}")
        }
    }
}