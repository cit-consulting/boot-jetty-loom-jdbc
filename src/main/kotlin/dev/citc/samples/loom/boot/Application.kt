package dev.citc.samples.loom.boot

import dev.citc.samples.loom.boot.horoscope.HoroscopeSpringConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

@SpringBootApplication
class Application : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        HoroscopeSpringConfig.initialize(applicationContext)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
