package com.example.riseandroid.fake

import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.IMovieRepo
import com.example.riseandroid.repository.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMovieRepo : IMovieRepo {

    private val fakeMovies = listOf(
        MovieModel(
            id = 1,
            title = "Fake Movie1",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
            genre = "",
            duration = "",
            director = "",
            description = "",
            video = "",
            videoPlaceholderUrl = ""
        ),
        MovieModel(
            id = 2,
            title = "Fake Movie2",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            genre = "",
            duration = "",
            director = "",
            description = "",
            video = "",
            videoPlaceholderUrl = ""
        )
    )

    override suspend fun getAllMoviesList(
        selectedDate: String,
        selectedCinemas: List<String>
    ): Flow<List<MovieModel>> {
        return flow {
            emit(fakeMovies)
        }
    }


}

