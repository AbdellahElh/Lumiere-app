import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.repository.TenturncardRepository
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



    /**
     * Factory class for creating an instance of TenturncardViewModel
     */
    class Factory(
        private val tenturncardRepository: TenturncardRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TenturncardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TenturncardViewModel(tenturncardRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
