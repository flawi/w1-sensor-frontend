package info.kschamer.w1sensorfrontend.controller

import javax.validation.constraints.NotNull

data class SensorDto(
        @field:NotNull var id: String? = null,
        var name: String? = null,
        var description: String? = null,
        var currentTemperature: Double? = null,
        var averageTemperature: Double? = null,
        var minTemperature: Double? = null,
        var maxTemperature: Double? = null
)