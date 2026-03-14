package edu.nd.pmcburne.hwapp.one.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. The @Database annotation lists the entities (tables) and the version number.
// If you ever change the GameEntity class later, you would increase the version number.
@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 2. This function gives the rest of your app access to the SQL commands you wrote
    abstract fun gameDao(): GameDao

    // 3. The Singleton pattern to ensure only one database instance exists
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If the instance already exists, return it. If not, create it.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ncaa_scores_database" // The name of the local SQLite file
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}