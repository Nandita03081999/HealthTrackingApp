import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.healthtrackingapp.data.HealthMetric
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class HealthViewModel(private val repository: HealthRepository) : ViewModel() {

    val allMetrics = repository.allMetrics.asLiveData()
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _filteredMetrics = MutableLiveData<List<HealthMetric>>()
    val filteredMetrics: LiveData<List<HealthMetric>> get() = _filteredMetrics

    fun addMetric(metric: HealthMetric) = viewModelScope.launch {
        repository.insertMetric(metric)
    }

    fun deleteMetric(metric: HealthMetric) = viewModelScope.launch {
        repository.deleteMetric(metric)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun applyFilterForTimeRange(filter: String) {
        val now = LocalDate.now()
        val (start, end) = getTimeRangeTimestamps(now, filter)
        _isLoading.value = true
        viewModelScope.launch {
            delay(300)
            repository.getMetricsBetween(start, end).collect {
                _filteredMetrics.value = it
                _isLoading.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeRangeTimestamps(date: LocalDate, period: String): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        return when (period) {
            "Morning" -> Pair(date.atTime(5, 0).atZone(zone).toEpochSecond() * 1000, date.atTime(11, 59).atZone(zone).toEpochSecond() * 1000)
            "Afternoon" -> Pair(date.atTime(12, 0).atZone(zone).toEpochSecond() * 1000, date.atTime(16, 59).atZone(zone).toEpochSecond() * 1000)
            "Evening" -> Pair(date.atTime(17, 0).atZone(zone).toEpochSecond() * 1000, date.atTime(20, 59).atZone(zone).toEpochSecond() * 1000)
            else -> Pair(0, System.currentTimeMillis())
        }
    }

    private val _chartMetrics = MutableStateFlow<List<HealthMetric>>(emptyList())
    val chartMetrics: StateFlow<List<HealthMetric>> = _chartMetrics

    fun updateChartMetrics(type: String) {
        val now = System.currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000

        _chartMetrics.value = filteredMetrics.value?.filter {
            it.type == type && it.timestamp >= (now - oneDayMillis)
        } ?: emptyList()
    }

    suspend fun getMetricById(id: Int): HealthMetric? {
        return repository.getMetricById(id)
    }
}