import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.repository.TenturncardRepository
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TenturncardViewModel(
    private val tenturncardRepository: TenturncardRepository

) : ViewModel() {

    private val _tenturncards = MutableStateFlow<List<Tenturncard>>(emptyList())
    val tenturncards: StateFlow<List<Tenturncard>> get() = _tenturncards

    fun fetchTenturncards(authToken: String) {
        viewModelScope.launch {
            try {
                val cards = tenturncardRepository.getTenturncards(authToken)
                _tenturncards.value = cards
            } catch (e: Exception) {
                // Handle error appropriately, e.g., log or show a message
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