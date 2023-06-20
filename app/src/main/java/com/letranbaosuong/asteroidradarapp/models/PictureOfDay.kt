package com.letranbaosuong.asteroidradarapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
@Entity(tableName = "picture_database")
data class PictureOfDay(
    @PrimaryKey
    val url: String,
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
) : Parcelable