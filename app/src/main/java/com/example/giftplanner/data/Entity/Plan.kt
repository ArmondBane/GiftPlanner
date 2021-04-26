package com.example.giftplanner.data.Entity

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "plans")
@Parcelize
data class Plan (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val holidayName: String = "",
    val recipient_id: Int = 0,
    val present_id: Int = 0
) : Parcelable {
    val dateFormatted: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(
            Date(date)
        )
    val dateFormattedAsYear: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("yyyy", Locale.getDefault()).format(
            Date(date)
        )
    val dateFormattedAsMonth: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("MM", Locale.getDefault()).format(
            Date(date)
        )
    val dateFormattedAsDay: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("dd", Locale.getDefault()).format(
            Date(date)
        )
}