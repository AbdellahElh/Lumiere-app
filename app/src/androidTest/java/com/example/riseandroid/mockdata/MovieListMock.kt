package com.example.riseandroid.mockdata

import com.example.riseandroid.R
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.model.Program

class MovieListMock {

    fun LoadRecentMoviesMock() : List<MoviePoster> {
        return listOf<MoviePoster>(
            MoviePoster(id = 1, cover = "fakeCover", releaseDate = "fakeDate"),
            MoviePoster(id = 2, cover = "fakeCover", releaseDate = "fakeDate"))
    }

    fun LoadProgramsMock(): List<Program> {
        val jamesBondMovie = Movie(1077050, "James bond", R.drawable.screenshot_2024_10_10_102504, "James Bond has left active service. His peace is short-lived when Felix Leiter, an old friend from the CIA, turns up asking for help, leading Bond onto the trail of a mysterious villain armed with dangerous new technology.", "action", "02h 43m", "Destin Daniel Cretton")
        val tatamiMovie = Movie(1077038, "Tatami", R.drawable.tatami, "The Iranian female judoka Leila is at the World Judo Championships, intent on bringing home Iran's first gold medal.", "action", "02h 43m", "Destin Daniel Cretton")
        val songOfEarth = Movie(1077579, "Songs of earth", R.drawable.song , "Being connected to nature, what does it mean? Father knows and father shows. The director's father is 84. We follow in his footsteps into the mountain home. Into nature's smallest life and out to grand panoramas, where he grew up." , "action"  , "02h 43m", "Destin Daniel Cretton")

        return listOf(
            Program(jamesBondMovie, "2024-11-14", "18:00", "Mechelen"),
            Program(tatamiMovie, "2024-11-21", "21:00", "Cinema Cartoons"),
            Program(jamesBondMovie, "2024-11-14", "19:30", "Brugge"),
            Program(jamesBondMovie, "2024-11-22", "21:30", "Brugge"),
            Program(tatamiMovie, "2024-11-23", "16:00", "Mechelen"),
            Program(songOfEarth, "2024-11-21", "21:00", "Cinema Cartoons"),
            Program(jamesBondMovie, "2024-11-24", "20:00", "Antwerpen"),
            Program(tatamiMovie, "2024-11-25", "17:30", "Brugge"),
            Program(songOfEarth, "2024-11-21", "21:00", "Mechelen"),
            Program(jamesBondMovie, "2024-11-26", "18:45", "Cinema Cartoons"),
            Program(tatamiMovie, "2024-11-27", "21:15", "Antwerpen"),
            Program(songOfEarth, "2024-11-21", "20:00", "Brugge"),
            Program(jamesBondMovie, "2024-11-28", "19:00", "Mechelen"),
            Program(tatamiMovie, "2024-11-29", "16:30", "Cinema Cartoons"),
            Program(jamesBondMovie, "2024-11-30", "20:30", "Brugge"),
            Program(tatamiMovie, "2024-11-31", "18:00", "Brugge"),
            Program(songOfEarth, "2024-11-21", "21:00", "Brugge")

        )
    }

    fun LoadAllMoviesMock(): List<MovieModel> {
        return listOf(MovieModel(
            id = 1,
            title = "FakeMovie1",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
            genre = "",
            duration = "",
            director = "",
            description = "",
            video = "",
            videoPlaceholderUrl = "",
            eventId = 1
        ),
            MovieModel(
                id = 2,
                title = "FakeMovie2",
                cinemas = emptyList(),
                cast = emptyList(),
                coverImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
                genre = "",
                duration = "",
                director = "",
                description = "",
                video = "",
                videoPlaceholderUrl = "",
                eventId = 2
            ))

    }
}