package ch.epfl.sdp.cook4me.persistence.repository

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split("\\")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString("\\")
    }
}