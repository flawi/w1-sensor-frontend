package info.kschamer.w1sensorfrontend.controller

import info.kschamer.w1sensorfrontend.model.SensorValue
import info.kschamer.w1sensorfrontend.repository.SensorValueRepository
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.*
import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant


@Suppress("unused")
@RestController
@RequestMapping("/sensors")
class Sensors(
        private val repository: SensorValueRepository) {

    private val log = LoggerFactory.getLogger(Sensors::class.java)
    private val decimalFormat = DecimalFormat("#.##")

    @GetMapping
    fun getSensors(): List<String> {
        return repository.getAvailableSensors()
    }

    @GetMapping("{id}")
    fun getSensor(
            @PathVariable("id") sensorId: String): SensorDto? {
        val watch = StopWatch()
        val now = Instant.now()
        val before = Instant.now().minus(Duration.ofHours(24))

        watch.start("getMaxSensorValue")
        val max: Double = repository.getMaxSensorValue(sensorId, before, now)
        watch.stop()

        watch.start("getMinSensorValue")
        val min: Double = repository.getMinSensorValue(sensorId, before, now)
        watch.stop()

        watch.start("getAvgSensorValue")
        val avg: Double = repository.getAvgSensorValue(sensorId, before, now)
        watch.stop()

        watch.start("getLastSensorValue")
        val lastValue: SensorValue = repository.getLastSensorValue(sensorId)
        watch.stop()

        val dto = SensorDto(sensorId)
        with(dto) {
            currentTemperature = lastValue.value
            maxTemperature = max
            minTemperature = min
            averageTemperature = avg

        }
        if (log.isInfoEnabled) {
            log.info(watch.prettyPrint())
        }

        return dto
    }

    @GetMapping("{id}/values", params = ["from", "to"])
    fun getValues(
            @PathVariable("id") sensorId: String,
            @RequestParam("from") @DateTimeFormat(iso = DATE_TIME) from: Instant,
            @RequestParam("to") @DateTimeFormat(iso = DATE_TIME) to: Instant): List<SensorValue> {
        val watch = StopWatch()
        watch.start("findSensorValuesInPeriod")
        val values: List<SensorValue> = repository.findSensorValuesInPeriod(sensorId, from, to)
        watch.stop()

        if (log.isInfoEnabled) {
            log.info(watch.prettyPrint())
        }
        return values
    }

    @GetMapping("{id}/values", params = [])
    fun getValuesOfLastHour(@PathVariable("id") sensorId: String): List<SensorValue> {
        val to = Instant.now()
        val from = to.minus(Duration.ofHours(1))
        return repository.findSensorValuesInPeriod(sensorId, from, to)
    }


    enum class ValueType {
        LAST,
        AVG,
        MIN,
        MAX
    }


}