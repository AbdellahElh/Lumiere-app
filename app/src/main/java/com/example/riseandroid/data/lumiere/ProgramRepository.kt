package com.example.riseandroid.data.lumiere


import com.example.riseandroid.data.Datasource
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.Program
import com.example.riseandroid.network.LumiereApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

interface ProgramRepository {
    suspend fun getPrograms() : Flow<List<Program>>
    suspend fun getProgramsLocation(location: String) : Flow<List<Program>>
    suspend fun getMoviesLocation(location: String) : Flow<List<Program>>
    suspend fun getProgramsForMovie(movieId: Long): Flow<List<Program>>



}

class NetworkProgramRepository(private val lumiereApiService: LumiereApiService) : ProgramRepository {
    override suspend fun getPrograms(): Flow<List<Program>> {
        val movies = Datasource().LoadPrograms()
        return listOf(movies).asFlow()
    }
    override suspend fun getProgramsLocation(location: String): Flow<List<Program>> {
        val programs = Datasource().LoadPrograms()
        return flow {
            emit(programs.filter { it.location.equals(location, ignoreCase = true) })
        }
    }
    override suspend fun getMoviesLocation(location: String): Flow<List<Program>> {
        val programs = Datasource().LoadPrograms()
        return flow {
            emit(
                programs
                    .filter { it.location.equals(location, ignoreCase = true) }
                    .distinctBy { it.movie.movieId }
            )
        }
    }
    override suspend fun getProgramsForMovie(movieId: Long): Flow<List<Program>> {
        val programs = Datasource().LoadPrograms()
        return flow {
            emit(programs.filter { it.movie.movieId == movieId })
        }
    }


}


