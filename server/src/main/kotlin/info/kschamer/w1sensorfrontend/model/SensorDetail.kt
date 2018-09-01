package info.kschamer.w1sensorfrontend.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "\"sensor-details\"")
data class SensorDetail(
        @Column(name = "\"sensor-id\"") @Id val sensorId: String? = null,
        @Column(name = "\"name\"") val name: String? = null,
        @Column(name = "\"description\"") val description: String? = null
)