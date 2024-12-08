package com.example.riseandroid.fake

import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.repository.IMovieRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMovieRepo : IMovieRepo {

    private val fakeMoviesResponse = listOf(
        ResponseMovie(
            id = 1,
            title = "Fake Movie1",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
            genre = "",
            duration = 100,
            director = "",
            description = "",
            videoPlaceholderUrl = "",
            releaseDate = "12-12-2021",
            bannerImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            posterImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            movieLink = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            eventId = 1
        ),
        ResponseMovie(
            id = 2,
            title = "Fake Movie2",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            genre = "",
            duration = 100,
            director = "",
            description = "",
            videoPlaceholderUrl = "",
            releaseDate = "12-12-2021",
            bannerImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            posterImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            movieLink = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            eventId = 2
        )
    )

    override suspend fun getAllMoviesList(
        selectedDate: String,
        selectedCinemas: List<String>,
        searchTitle: String?
    ): Flow<List<ResponseMovie>> {
        return flow {
            emit(fakeMoviesResponse)
        }
    }

    override suspend fun getMovieById(id: Int): ResponseMovie {
        return fakeMoviesResponse.find { it.id == id }!!
    }


}

