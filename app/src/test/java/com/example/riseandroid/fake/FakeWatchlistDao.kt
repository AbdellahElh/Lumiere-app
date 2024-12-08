package com.example.riseandroid.fake

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.watchlist.MovieWatchlistEntity
import com.example.riseandroid.data.entitys.watchlist.WatchlistDao
import com.example.riseandroid.data.entitys.watchlist.WatchlistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeWatchlistDao : WatchlistDao {

    private val watchlists = mutableListOf<WatchlistEntity>()
    private val movies = mutableListOf<MovieEntity>()
    private val movieWatchlistRelations = mutableListOf<MovieWatchlistEntity>()

    private val watchlistFlows = mutableMapOf<Int, MutableStateFlow<List<MovieEntity>>>()

    private fun updateWatchlistFlowForUser(userId: Int) {
        val watchlistId = watchlists.firstOrNull { it.userId == userId }?.id
        if (watchlistId != null) {
            val movieIds = movieWatchlistRelations.filter { it.watchlistId == watchlistId }.map { it.movieId }
            val userMovies = movies.filter { movieIds.contains(it.id) }
            val flow = watchlistFlows[userId] ?: MutableStateFlow(emptyList())
            flow.value = userMovies
            watchlistFlows[userId] = flow
        } else {
            val flow = watchlistFlows[userId] ?: MutableStateFlow(emptyList())
            flow.value = emptyList()
            watchlistFlows[userId] = flow
        }
    }

    override fun getMoviesInWatchlist(userId: Int): Flow<List<MovieEntity>> {
        if (!watchlistFlows.containsKey(userId)) {
            watchlistFlows[userId] = MutableStateFlow(emptyList())
        }
        return watchlistFlows[userId]!!
    }

    override suspend fun addToWatchlist(movieWatchlistEntity: MovieWatchlistEntity) {
        val alreadyExists = movieWatchlistRelations.any { it.watchlistId == movieWatchlistEntity.watchlistId && it.movieId == movieWatchlistEntity.movieId }
        if (!alreadyExists) {
            movieWatchlistRelations.add(movieWatchlistEntity)
        }
        updateWatchlistFlowForUser(getUserIdFromWatchlistId(movieWatchlistEntity.watchlistId))
    }

    override suspend fun removeFromWatchlist(movieId: Int, userId: Int) {
        val watchlistId = getWatchlistIdForUser(userId)
        if (watchlistId != null) {
            movieWatchlistRelations.removeAll { it.watchlistId == watchlistId && it.movieId == movieId }
            updateWatchlistFlowForUser(userId)
        }
    }

    override suspend fun addMovieToWatchlist(movieEntity: MovieEntity) {
        val alreadyExists = movies.any { it.id == movieEntity.id }
        if (!alreadyExists) {
            movies.add(movieEntity)
        }
    }

    override suspend fun getWatchlistIdForUser(userId: Int): Int? {
        return watchlists.firstOrNull { it.userId == userId }?.id
    }

    override suspend fun insertWatchlist(watchlistEntity: WatchlistEntity) {
        if (!watchlists.any { it.userId == watchlistEntity.userId }) {
            val newId = (watchlists.maxOfOrNull { it.id } ?: 0) + 1
            watchlists.add(watchlistEntity.copy(id = newId))
            updateWatchlistFlowForUser(watchlistEntity.userId)
        }
    }

    override suspend fun clearWatchlistForUser(userId: Int) {
        val watchlistId = getWatchlistIdForUser(userId)
        if (watchlistId != null) {
            movieWatchlistRelations.removeAll { it.watchlistId == watchlistId }
            updateWatchlistFlowForUser(userId)
        }
    }

    override suspend fun clearAllMovieWatchlistEntries() {
        movieWatchlistRelations.clear()
        watchlists.forEach { updateWatchlistFlowForUser(it.userId) }
    }

    override suspend fun clearAllWatchlists() {
        watchlists.clear()
        movieWatchlistRelations.clear()
        watchlistFlows.forEach { (_, flow) -> flow.value = emptyList() }
    }

    override suspend fun clearAllWatchlistData() {
        clearAllMovieWatchlistEntries()
        clearAllWatchlists()
    }

    override suspend fun insertMovies(movies: List<MovieEntity>) {
        this.movies.addAll(movies.filter { newMovie -> this.movies.none { it.id == newMovie.id } })
        watchlists.forEach { updateWatchlistFlowForUser(it.userId) }
    }

    override suspend fun insertMovieWatchlistRelations(relations: List<MovieWatchlistEntity>) {
        relations.forEach { relation ->
            val alreadyExists = movieWatchlistRelations.any { it.watchlistId == relation.watchlistId && it.movieId == relation.movieId }
            if (!alreadyExists) {
                movieWatchlistRelations.add(relation)
            }
        }
        relations.forEach { updateWatchlistFlowForUser(getUserIdFromWatchlistId(it.watchlistId)) }
    }

    private fun getUserIdFromWatchlistId(watchlistId: Int): Int {
        return watchlists.firstOrNull { it.id == watchlistId }?.userId ?: -1
    }
}
