package com.example.giftplanner.data.Entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "recipients")
@Parcelize
data class Recipient (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = ""
) : Parcelable {}