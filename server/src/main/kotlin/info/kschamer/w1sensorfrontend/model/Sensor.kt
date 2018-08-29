package info.kschamer.w1sensorfrontend.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Sensor(
        @Id val id: String? = null,
        val name: String? = null,
        val description: String? = null
)