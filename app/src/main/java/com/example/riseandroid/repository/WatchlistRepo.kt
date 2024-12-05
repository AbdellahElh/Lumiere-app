package com.example.riseandroid.repository

import android.annotation.SuppressLint
import android.net.http.HttpException
import com.example.riseandroid.data.entitys.watchlist.MovieWatchlistEntity
import com.example.riseandroid.data.entitys.watchlist.WatchlistDao
import com.example.riseandroid.data.entitys.watchlist.WatchlistEntity
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MovieWatchlistModel
import com.example.riseandroid.network.WatchlistApi
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.asExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface IWatchlistRepo {
    fun getMoviesInWatchlist(userId: Int): Flow<List<MovieModel>>
    suspend fun addToWatchlist(movie: MovieModel, userId: Int)
    suspend fun removeFromWatchlist(movieId: Int, userId: Int)
    suspend fun getWatchlistId(userId: Int): Int
    suspend fun syncWatchlistWithBackend(userId: Int)
}

class WatchlistRepo(
    private val watchlistDao: WatchlistDao,
    private val watchlistApi: WatchlistApi
) : IWatchlistRepo {

    override fun getMoviesInWatchlist(userId: Int): Flow<List<MovieModel>> {
        return watchlistDao.getMoviesInWatchlist(userId).map { entities ->
            entities.map { entity -> entity.asExternalModel() }
        }
    }

    override suspend fun addToWatchlist(movie: MovieModel, userId: Int) {
        withContext(Dispatchers.IO) {
            val watchlistId = getWatchlistId(userId)

            val movieWatchlist = MovieWatchlistModel(
                watchlistId = watchlistId,
                movieId = movie.id
            )

            try {
                watchlistDao.addToWatchlist(movieWatchlist.asEntity())
                watchlistApi.addToWatchlist(movie)
            } catch (@SuppressLint("NewApi") e: HttpException) {
                throw e
            }
        }
    }



    override suspend fun removeFromWatchlist(movieId: Int, userId: Int) {
        withContext(Dispatchers.IO) {
            val watchlistId = getWatchlistId(userId)
            watchlistDao.removeFromWatchlist(movieId, userId)
            try {
                watchlistApi.removeFromWatchlist(movieId)
            } catch (@SuppressLint("NewApi") e: HttpException) {
                throw e // Afhandeling van de fout gebeurt in de ViewModel
            }
        }
    }



    override suspend fun syncWatchlistWithBackend(userId: Int) {
        withContext(Dispatchers.IO) {
            watchlistDao.clearAllWatchlistData()

            val backendMovies = watchlistApi.getWatchlist()

            val movieEntities = backendMovies.map { it.asEntity() }
            watchlistDao.insertMovies(movieEntities)

            val watchlistId = getWatchlistId(userId)

            val movieWatchlistEntities = backendMovies.map { movie ->
                MovieWatchlistEntity(
                    watchlistId = watchlistId,
                    movieId = movie.id
                )
            }
            watchlistDao.insertMovieWatchlistRelations(movieWatchlistEntities)
        }
    }


    override suspend fun getWatchlistId(userId: Int): Int {
        return watchlistDao.getWatchlistIdForUser(userId) ?: run {
            val newWatchlist = WatchlistEntity(userId = userId)
            watchlistDao.insertWatchlist(newWatchlist)
            watchlistDao.getWatchlistIdForUser(userId)
                ?: throw IllegalStateException("Kon geen watchlist aanmaken voor user met ID $userId")
        }
    }
}


