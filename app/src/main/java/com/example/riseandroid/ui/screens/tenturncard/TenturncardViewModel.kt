import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.Program
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.repository.ITenturncardRepository
import com.example.riseandroid.repository.TenturncardRepository
import com.example.riseandroid.ui.screens.homepage.HomepageUiState
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
sealed interface TenturncardUiState {
    data class Succes(val allTenturncards: StateFlow<List<Tenturncard>>,

    ) : TenturncardUiState
    object Error : TenturncardUiState
    object Loading : TenturncardUiState
}

class TenturncardViewModel(
    private val tenturncardRepository: ITenturncardRepository

) : ViewModel() {
    var tenturncardUiState: TenturncardUiState by mutableStateOf(TenturncardUiState.Loading)
        private set

    private val _tenturncards = MutableStateFlow<List<Tenturncard>>(emptyList())
    val tenturncards = _tenturncards.asStateFlow()

    fun fetchTenturncards(authToken: String) {
        viewModelScope.launch {
            try {
                Log.d("viewmodeltenturncard",authToken)
                val cards = tenturncardRepository.getTenturncards(authToken)
                    .collect { cards ->
                        _tenturncards.value = cards
                        tenturncardUiState  = TenturncardUiState.Succes(
                            allTenturncards = tenturncards
                        )

                    }


            } catch (e: Exception) {

                _tenturncards.value = emptyList()
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val tenturncardsRepo=application.container.tenturncardRepository
                TenturncardViewModel(tenturncardRepository = tenturncardsRepo)
            }
        }
    }
}