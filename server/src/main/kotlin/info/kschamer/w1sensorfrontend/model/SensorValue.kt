package info.kschamer.w1sensorfrontend.model

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "\"sensor-values\"")
data class SensorValue(
        @Column(name = "\"id\"") @Id val id: Long? = null,
        @Column(name = "\"sensor-id\"") val sensorId: String? = null,
        @Column(name = "\"sensor-value\"") val value: Double? = null,
        @Column(name = "\"timestamp\"") val timestamp: Instant? = null
)