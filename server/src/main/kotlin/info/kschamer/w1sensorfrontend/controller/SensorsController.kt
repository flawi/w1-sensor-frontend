package info.kschamer.w1sensorfrontend.controller

import info.kschamer.w1sensorfrontend.model.SensorDetail
import info.kschamer.w1sensorfrontend.model.SensorValue
import info.kschamer.w1sensorfrontend.repository.SensorDetailRepository
import info.kschamer.w1sensorfrontend.repository.SensorValueRepository
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.http.ResponseEntity
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant
import javax.validation.Valid


@Suppress("unused")
@RestController
@RequestMapping("/sensors")
class SensorsController(
        private val valueRepository: SensorValueRepository,
        private val detailRepository: SensorDetailRepository) {

    private val log = LoggerFactory.getLogger(SensorsController::class.java)
    private val decimalFormat = DecimalFormat("#.##")

    @GetMapping
    fun getSensors(): List<String> {
        return valueRepository.getAvailableSensors()
    }

    @GetMapping("{id}")
    fun getSensor(
            @PathVariable("id") sensorId: String): ResponseEntity<SensorDto?> {

        val watch = StopWatch()
        watch.start("existsBySensorId")
        val sensorIdFound = valueRepository.existsBySensorId(sensorId)
        watch.stop()
        if(!sensorIdFound){
            return ResponseEntity.notFound().build()
        }

        val now = Instant.now()
        val before = Instant.now().minus(Duration.ofHours(24))

        watch.start("getMaxSensorValue")
        val max: Double = valueRepository.getMaxSensorValue(sensorId, before, now)
        watch.stop()

        watch.start("getMinSensorValue")
        val min: Double = valueRepository.getMinSensorValue(sensorId, before, now)
        watch.stop()

        watch.start("getAvgSensorValue")
        val avg: Double = valueRepository.getAvgSensorValue(sensorId, before, now)
        watch.stop()

        watch.start("getLastSensorValue")
        val lastValue: SensorValue = valueRepository.getLastSensorValue(sensorId)
        watch.stop()

        watch.start("detailRepository.findBySensorId")
        val details: SensorDetail? = detailRepository.findBySensorId(sensorId)
        watch.stop()

        val dto = SensorDto(sensorId)
        with(dto) {
            currentTemperature = lastValue.value
            maxTemperature = max
            minTemperature = min
            averageTemperature = avg
            name = details?.name
            description = details?.description
        }
        if (log.isInfoEnabled) {
            log.info(watch.prettyPrint())
        }

        return ResponseEntity.ok(dto)
    }

    @PutMapping("{id}")
    fun updateSensor(
            @PathVariable("id") sensorId: String, @Valid @RequestBody dto: SensorDto, uriBuilder: UriComponentsBuilder): ResponseEntity<SensorDto?> {

        if(!valueRepository.existsBySensorId(sensorId)){
            return ResponseEntity.notFound().build()
        }

        detailRepository.save(SensorDetail(
                sensorId = sensorId,
                name = dto.name,
                description = dto.description
        ))

        return ResponseEntity.ok()
                .location(uriBuilder.build().toUri())
                .build()
    }

    @GetMapping("{id}/values", params = ["from", "to"])
    fun getValues(
            @PathVariable("id") sensorId: String,
            @RequestParam("from") @DateTimeFormat(iso = DATE_TIME) from: Instant,
            @RequestParam("to") @DateTimeFormat(iso = DATE_TIME) to: Instant): List<SensorValue> {
        val watch = StopWatch()
        watch.start("findSensorValuesInPeriod")
        val values: List<SensorValue> = valueRepository.findSensorValuesInPeriod(sensorId, from, to)
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
        return valueRepository.findSensorValuesInPeriod(sensorId, from, to)
    }
}