package ch.epfl.sdp.cook4me.persistence.repository

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import ch.epfl.sdp.cook4me.persistence.model.Recipe
import ch.epfl.sdp.cook4me.persistence.model.RecipeNote

@Database(entities = [Recipe::class, RecipeNote::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun recipeNoteDao(): RecipeNoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface RecipeNoteDao {
    @Query("SELECT * FROM recipeNote")
    fun getAll(): List<RecipeNote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg recipeNotes: RecipeNote)
}

