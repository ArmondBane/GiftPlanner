package com.example.giftplanner.data.Entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "presents")
@Parcelize
data class Present (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val cost: Double = 0.0
) : Parcelable {}