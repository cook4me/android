package ch.epfl.sdp.cook4me

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Activity::class], version = 1)
    abstract class ActivityDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}