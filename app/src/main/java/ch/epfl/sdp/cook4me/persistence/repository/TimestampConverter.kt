package ch.epfl.sdp.cook4me.persistence.repository

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import java.util.*

class TimestampConverter {
    @TypeConverter
    fun fromTimestamp(time: Timestamp): Long {
        return time.toDate().time
    }

    @TypeConverter
    fun dateToTimestamp(milliseconds: Long): Timestamp {
        return Timestamp(Date(milliseconds))
    }
}