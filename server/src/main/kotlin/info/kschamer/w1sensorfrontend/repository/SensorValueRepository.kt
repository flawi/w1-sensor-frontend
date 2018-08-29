package info.kschamer.w1sensorfrontend.repository

import info.kschamer.w1sensorfrontend.model.SensorValue
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface SensorValueRepository : PagingAndSortingRepository<SensorValue, Long>{

    @Query("""
        select distinct v.sensorId
        from SensorValue as v
        order by v.sensorId
    """)
    fun getAvailableSensors() : List<String>

    @Query("""
        select v
        from SensorValue as v
        where v.sensorId = ?1 and v.timestamp >= ?2 and v.timestamp <= ?3
    """)
    @Nullable
    fun findSensorValuesInPeriod(sensorId: String, from : Instant, to: Instant) : List<SensorValue>

    @Query("""
        select v
        from SensorValue as v
        where v.timestamp = (
            select max(v.timestamp)
            from SensorValue as v
            where v.sensorId = ?1
        ) and v.sensorId = ?1
    """)
    @Nullable
    fun getLastSensorValue(sensorId: String) : SensorValue

    @Query("""
        select max(v.value)
        from SensorValue as v
        where       v.sensorId = ?1
                and v.timestamp >= ?2
                and v.timestamp < ?3
    """)
    @Nullable
    fun getMaxSensorValue(sensorId: String, from: Instant, to: Instant) : Double

    @Query("""
        select min(v.value)
        from SensorValue as v
        where       v.sensorId = ?1
                and v.timestamp >= ?2
                and v.timestamp < ?3
    """)
    @Nullable
    fun getMinSensorValue(sensorId: String, from: Instant, to: Instant) : Double

    @Query("""
        select avg(v.value)
        from SensorValue as v
        where       v.sensorId = ?1
                and v.timestamp >= ?2
                and v.timestamp < ?3
    """)
    @Nullable
    fun getAvgSensorValue(sensorId: String, from: Instant, to: Instant) : Double
}