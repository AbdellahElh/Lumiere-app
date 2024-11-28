// FakeProgramRepository.kt
package com.example.riseandroid.fake

import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.Program
import com.example.riseandroid.data.lumiere.ProgramRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProgramRepository(
    private val programs: List<Program> = emptyList()
) : ProgramRepository {

    override suspend fun getPrograms(): Flow<List<Program>> {
        return flow {
            emit(programs)
        }
    }

    override suspend fun getProgramsLocation(location: String): Flow<List<Program>> {
        val filteredPrograms = programs.filter { it.location.equals(location, ignoreCase = true) }
        return flow {
            emit(filteredPrograms)
        }
    }

    override suspend fun getMoviesLocation(location: String): Flow<List<Program>> {
        val filteredDistinctPrograms = programs
            .filter { it.location.equals(location, ignoreCase = true) }
            .distinctBy { it.movie.movieId }
        return flow {
            emit(filteredDistinctPrograms)
        }
    }

    override suspend fun getProgramsForMovie(movieId: Long): Flow<List<Program>> {
        val filteredPrograms = programs.filter { it.movie.movieId == movieId }
        return flow {
            emit(filteredPrograms)
        }
    }
}
