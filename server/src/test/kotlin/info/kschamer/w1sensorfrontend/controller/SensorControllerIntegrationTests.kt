package info.kschamer.w1sensorfrontend.controller

import info.kschamer.w1sensorfrontend.model.SensorDetail
import info.kschamer.w1sensorfrontend.model.SensorValue
import info.kschamer.w1sensorfrontend.repository.SensorDetailRepository
import info.kschamer.w1sensorfrontend.repository.SensorValueRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:integration-tests.properties")
class SensorControllerIntegrationTests {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var detailRepository: SensorDetailRepository

    @Autowired
    private lateinit var valueRepository: SensorValueRepository

    companion object {

        private fun existingSensorDetails(): List<SensorDetail> {
            val detailOne = SensorDetail(sensorId = "041700e609fe",
                    name = "sensor 1",
                    description = "description 1")
            val detailTwo = SensorDetail(sensorId = "041700e609ff",
                    name = "sensor 2",
                    description = "description 2"
            )
            return listOf(detailOne, detailTwo)
        }

        private fun getValidSensorDto(): SensorDto {
            val existingDetail = existingSensorDetails().first()
            val dto = SensorDto(existingDetail.sensorId)
            with(dto) {
                name = "request sensor"
                description = "request description"
            }
            return dto
        }
    }

    @Before
    fun setUp() {
        val existingSensorDetails = existingSensorDetails()
        detailRepository.saveAll(existingSensorDetails)

        val valuesToCreate = mutableListOf<SensorValue>()
        for ((index, detail: SensorDetail) in existingSensorDetails.withIndex()) {
            valuesToCreate.add(SensorValue(
                    id = index.toLong(),
                    sensorId = detail.sensorId,
                    value = 12.3
            ))
        }
        valueRepository.saveAll(valuesToCreate)
    }

    @Test
    fun update_sensor_whichDoesNotExist_404expected() {
        val validRequestDto = getValidSensorDto()
        val response = restTemplate.exchange("/sensors/notExistingSensorId", HttpMethod.PUT, HttpEntity(validRequestDto), String::class.java)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun update_sensor_withInvalidRequestBody_400expected() {
        val existingSensor = getValidSensorDto()
        val invalidRequestBody = object { }
        val response = restTemplate.exchange("/sensors/${existingSensor.id}", HttpMethod.PUT, HttpEntity(invalidRequestBody), String::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun update_sensor_withValidRequestBody_200expected() {
        val validDto = getValidSensorDto()
        validDto.name = "updated name"
        validDto.description = "updated description"
        val response = restTemplate.exchange("/sensors/${validDto.id}", HttpMethod.PUT, HttpEntity(validDto), String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        val dbDetails: SensorDetail? = detailRepository.findBySensorId(validDto.id!!)
        assertEquals(validDto.name, dbDetails?.name)
        assertEquals(validDto.description, dbDetails?.description)
    }
}