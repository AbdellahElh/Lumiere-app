package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.watchlist.MovieWatchlistEntity
import com.example.riseandroid.data.entitys.watchlist.WatchlistDao
import com.example.riseandroid.data.entitys.watchlist.WatchlistEntity
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.network.WatchlistApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface IWatchlistRepo {
    fun getMoviesInWatchlist(userId: Int): Flow<List<MovieModel>>
    suspend fun addToWatchlist(movie: MovieModel, userId: Int)
    suspend fun removeFromWatchlist(movieId: Int, userId: Int)
}

class WatchlistRepo(
    private val watchlistDao: WatchlistDao,
    private val watchlistApi: WatchlistApi
) : IWatchlistRepo {

    override fun getMoviesInWatchlist(userId: Int): Flow<List<MovieModel>> {
        return watchlistDao.getMoviesInWatchlist(userId).map { entities ->
            entities.map { entity ->
                MovieModel(
                    id = entity.id,
                    title = entity.title,
                    coverImageUrl = entity.coverImageUrl,
                    genre = entity.genre,
                    duration = entity.duration,
                    director = entity.director,
                    description = entity.description,
                    video = null, // Optional, backend doesn't include it
                    videoPlaceholderUrl = entity.videoPlaceholderUrl,
                    cast = emptyList(), // Optional, backend doesn't include it
                    cinemas = emptyList() // Optional, backend doesn't include it
                )
            }
        }
    }

    override suspend fun addToWatchlist(movie: MovieModel, userId: Int) {
        withContext(Dispatchers.IO) {
            Log.d("WatchlistRepo", "Adding movie to watchlist: $movie for userId: $userId")
            val watchlistId = watchlistDao.getWatchlistIdForUser(userId)
                ?: createWatchlistForUser(userId)

            val movieWatchlistEntity = MovieWatchlistEntity(
                movieId = movie.id,
                watchlistId = watchlistId
            )

            Log.d("WatchlistRepo", "Adding movie to local database: $movieWatchlistEntity")
            watchlistDao.addToWatchlist(movieWatchlistEntity)

            Log.d("WatchlistRepo", "Syncing with backend API")
            watchlistApi.addToWatchlist(movie)
        }
    }


    override suspend fun removeFromWatchlist(movieId: Int, userId: Int) {
        withContext(Dispatchers.IO) {
            watchlistDao.removeFromWatchlist(movieId, userId)

            watchlistApi.removeFromWatchlist(movieId)
        }
    }

    private suspend fun createWatchlistForUser(userId: Int): Int {
        val newWatchlist = WatchlistEntity(userId = userId)
        watchlistDao.insertWatchlist(newWatchlist)
        return watchlistDao.getWatchlistIdForUser(userId)!!
    }
}
