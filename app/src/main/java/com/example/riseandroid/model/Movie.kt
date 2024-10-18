package com.example.riseandroid.model

import androidx.annotation.DrawableRes

data class Movie(val movieId: Long,
                 val title: String,
                 @DrawableRes val posterResourceId : Int,
                 val description: String?,
                 val genre: String?,
                 val length: String?,
                 val director: String?) {
}
