package info.kschamer.w1sensorfrontend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class W1SensorFrontendApplication

fun main(args: Array<String>) {
    runApplication<W1SensorFrontendApplication>(*args)
}
