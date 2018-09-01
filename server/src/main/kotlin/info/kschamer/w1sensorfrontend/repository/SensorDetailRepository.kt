package info.kschamer.w1sensorfrontend.repository

import info.kschamer.w1sensorfrontend.model.SensorDetail
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface SensorDetailRepository : PagingAndSortingRepository<SensorDetail, String> {

    // use own function in favor of Spring Data's findById, because findById was way
    // slower (~300ms vs. ~80ms)
    @Query("""
        select d
        from SensorDetail as d
        where d.sensorId = ?1
    """)
    fun findBySensorId(sensorId: String): SensorDetail?
}