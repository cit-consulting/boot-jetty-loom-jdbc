package dev.citc.samples.loom.boot.horoscope

import org.springframework.http.MediaType
import org.springframework.web.servlet.function.router
import java.time.LocalDate
import java.util.EnumSet

internal fun horoscopeRouter(horoscopeRepository: HoroscopeRepository) = router {
    accept(MediaType.APPLICATION_JSON).nest {
        GET("/horoscopes/daily") {
            ok().body(horoscopeRepository.findDailyForSignsAt(EnumSet.allOf(ZodiacSign::class.java), LocalDate.now()))
        }

        GET("/horoscopes/weekly") {
            ok().body(horoscopeRepository.findWeeklyForSignsAt(EnumSet.allOf(ZodiacSign::class.java), LocalDate.now()))
        }
    }
}
