package com.example.riseandroid.mockdata

import com.example.riseandroid.R
import com.example.riseandroid.model.Movie

class MovieListMock {
    fun LoadRecentMoviesMock() : List<Movie> {
        return listOf<Movie>(
            Movie("A new kind of wilderness", R.drawable.screenshot_2024_10_09_224909),
            Movie("Ezra", R.drawable.screenshot_2024_10_08_105150),
        )
    }
    fun LoadAllMoviesMock() : List<Movie> {
        return listOf<Movie>(
            Movie("James bond", R.drawable.screenshot_2024_10_10_102504)
        )
    }
}