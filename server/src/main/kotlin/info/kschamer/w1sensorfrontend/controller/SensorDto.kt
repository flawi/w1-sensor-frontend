package info.kschamer.w1sensorfrontend.controller

data class SensorDto(
        val id: String? = null
) {
    var name: String? = null
    var description: String? = null
    var currentTemperature: Double? = null
    var averageTemperature: Double? = null
    var minTemperature: Double? = null
    var maxTemperature: Double? = null
}