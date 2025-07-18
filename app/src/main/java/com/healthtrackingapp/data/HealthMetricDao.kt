import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.healthtrackingapp.data.HealthMetric
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthMetricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metric: HealthMetric)

    @Delete
    suspend fun delete(metric: HealthMetric)

    @Query("SELECT * FROM health_metrics ORDER BY timestamp DESC")
    fun getAll(): Flow<List<HealthMetric>>

    @Query("SELECT * FROM health_metrics WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun getMetricsBetween(start: Long, end: Long): Flow<List<HealthMetric>>

    @Query("SELECT * FROM health_metrics WHERE id = :id")
    suspend fun getMetricById(id: Int): HealthMetric?

}
