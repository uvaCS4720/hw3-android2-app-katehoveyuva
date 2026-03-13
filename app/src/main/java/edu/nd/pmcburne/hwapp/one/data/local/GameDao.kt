package edu.nd.pmcburne.hwapp.one.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    // Satisfies the requirement to overwrite old scores with new ones
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    // Retrieves games for offline mode.
    // Returning a Flow means your ViewModel and Jetpack Compose UI will automatically update whenever the database changes!
    @Query("SELECT * FROM games WHERE dateSelected = :date AND gender = :gender")
    fun getGames(date: String, gender: String): Flow<List<GameEntity>>
}