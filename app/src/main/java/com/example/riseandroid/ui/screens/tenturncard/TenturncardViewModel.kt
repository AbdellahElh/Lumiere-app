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
import com.example.riseandroid.repository.ApiResource
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
    data class Success(val allTenturncards: StateFlow<List<Tenturncard>>,

    ) : TenturncardUiState
    data class Error(val message: String?) : TenturncardUiState
    object Loading : TenturncardUiState
}

class TenturncardViewModel(
    private val tenturncardRepository: ITenturncardRepository

) : ViewModel() {
    var tenturncardUiState: TenturncardUiState by mutableStateOf(TenturncardUiState.Loading)
        private set

    private val _tenturncards = MutableStateFlow<List<Tenturncard>>(emptyList())
    val tenturncards = _tenturncards.asStateFlow()

    init{
        fetchTenturncards()
    }

    fun fetchTenturncards() {
        viewModelScope.launch {
            try {
                val cards = tenturncardRepository.getTenturncards()
                    .collect { cards ->
                        _tenturncards.value = cards
                        tenturncardUiState  = TenturncardUiState.Success(
                            allTenturncards = tenturncards
                        )

                    }


            } catch (e: Exception) {

                _tenturncards.value = emptyList()
            }
        }
    }

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    fun updateInputText(newText: String) {
        _inputText.value = newText
    }

    fun submitActivationCode(
        activationCode: String) {
        var activationCodeTrimmed = activationCode.trim()
        viewModelScope.launch {
            tenturncardUiState = TenturncardUiState.Loading
            try {
                tenturncardRepository.addTenturncard(activationCodeTrimmed)
                    .collect { resource ->
                        when (resource) {
                            is ApiResource.Loading -> {
                                tenturncardUiState = TenturncardUiState.Loading
                            }
                            is ApiResource.Success -> {
                                tenturncardUiState = TenturncardUiState.Success(
                                    allTenturncards = tenturncards
                                )
                                updateInputText("Tienbeurtenkaart succesvol toegevoegd")
                            }
                            is ApiResource.Error -> {
                                tenturncardUiState = TenturncardUiState.Error(resource.message)
                                if(resource.message?.contains("404") == true) {
                                    updateInputText("Deze kaart bestaat niet")
                                }
                                else {
                                    updateInputText("Deze kaart behoort al tot iemand")
                                }
                            }

                            is ApiResource.Initial -> {
                                updateInputText("Je request is in behandeling")
                            }
                        }
                    }
            } catch (e: Exception) {
                tenturncardUiState = TenturncardUiState.Error(e.message)
                updateInputText("Er was een onverwachte fout in de viewmodel")
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