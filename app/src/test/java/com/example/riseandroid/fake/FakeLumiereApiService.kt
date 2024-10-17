package com.example.riseandroid.fake

import com.example.riseandroid.model.Movie
import com.example.riseandroid.network.LumiereApiService

class FakeLumiereApiService : LumiereApiService {
    override suspend fun getMovies(): List<Movie> {
        return FakeDataSource.LoadNonRecenMoviesMock()
    }
}