package ch.epfl.sdp.cook4me

import androidx.room.*

@Entity
data class Activity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "activity") val activity: String?,
)