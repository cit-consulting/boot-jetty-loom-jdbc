package dev.citc.samples.loom.boot.horoscope

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.servlet.function.router
import java.time.LocalDate

internal fun horoscopeRouter(jsonMapper: ObjectMapper) = router {
    GET("/horoscopes/daily") {
        val resp = jsonMapper.createObjectNode().apply {
            put("date", LocalDate.now().toString())
        }
        ok().body(resp)
    }
}
