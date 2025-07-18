import com.healthtrackingapp.data.HealthMetric
import kotlinx.coroutines.flow.Flow

class HealthRepository(private val dao: HealthMetricDao) {
    val allMetrics = dao.getAll()

    suspend fun insertMetric(metric: HealthMetric) = dao.insert(metric)
    suspend fun deleteMetric(metric: HealthMetric) = dao.delete(metric)
    fun getMetricsBetween(start: Long, end: Long) = dao.getMetricsBetween(start, end)
    suspend fun getMetricById(id: Int): HealthMetric? {
        return dao.getMetricById(id)
    }

}
