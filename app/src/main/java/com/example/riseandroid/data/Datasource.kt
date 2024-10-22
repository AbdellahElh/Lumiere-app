package com.example.riseandroid.data

import com.example.riseandroid.R
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.Ticket

class Datasource() {
    fun LoadRecentMovies() : List<Movie> {
        return listOf<Movie>(
            Movie(1, "A new kind of wilderness",R.drawable.screenshot_2024_10_09_224909, "James Bond has left active service. His peace is short-lived when Felix Leiter, an old friend from the CIA, turns up asking for help, leading Bond onto the trail of a mysterious villain armed with dangerous new technology." , "action", "02h 43m", "Destin Daniel Cretton"),
            Movie(2, "Ezra",R.drawable.screenshot_2024_10_08_105150 , "The Iranian female judoka Leila is at the World Judo Championships, intent on bringing home Iran's first gold medal." , "action"  , "02h 43m", "Destin Daniel Cretton"),
            Movie(3, "Firebrand", R.drawable.screenshot_2024_10_10_192651 , "Being connected to nature, what does it mean? Father knows and father shows. The director's father is 84. We follow in his footsteps into the mountain home. Into nature's smallest life and out to grand panoramas, where he grew up." , "action"  , "02h 43m", "Destin Daniel Cretton"),
        )
    }
    fun LoadNonRecentMovies() : List<Movie> {
        return listOf<Movie>(
            Movie(1, "James bond", R.drawable.screenshot_2024_10_10_102504 , "James Bond has left active service. His peace is short-lived when Felix Leiter, an old friend from the CIA, turns up asking for help, leading Bond onto the trail of a mysterious villain armed with dangerous new technology.", "action"  , "02h 43m", "Destin Daniel Cretton"),
            Movie(2, "Tatami", R.drawable.tatami , "The Iranian female judoka Leila is at the World Judo Championships, intent on bringing home Iran's first gold medal." , "action"  , "02h 43m", "Destin Daniel Cretton"),
            Movie(3, "Songs of earth", R.drawable.song , "Being connected to nature, what does it mean? Father knows and father shows. The director's father is 84. We follow in his footsteps into the mountain home. Into nature's smallest life and out to grand panoramas, where he grew up." , "action"  , "02h 43m", "Destin Daniel Cretton")
        )
    }
    fun ticketTypes() : List<Ticket>{
        return listOf<Ticket>(
            Ticket("Standaard", 12.00),
            Ticket("65+", 11.50),
            Ticket("Student (-26j)", 10.00)
        )
    }


}