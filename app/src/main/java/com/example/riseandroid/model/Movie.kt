package com.example.riseandroid.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
data class Movie(val title: String,@DrawableRes val posterResourceId : Int) {
}
