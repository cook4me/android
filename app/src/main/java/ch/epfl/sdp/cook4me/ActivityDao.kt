package ch.epfl.sdp.cook4me

import androidx.room.*

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity")
    fun getAll(): List<Activity>

    @Query("SELECT * FROM activity ORDER BY RANDOM() LIMIT 1;")
    fun getRandom(): List<Activity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(activity: Activity)

    @Delete
    fun delete(user: Activity)
}