package dev.citc.samples.loom.boot.horoscope

import dev.citc.samples.loom.boot.test.FullAppContextTest
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.server.LocalServerPort
import java.time.LocalDate

@FullAppContextTest
class HoroscopeRoutesTests(
    @LocalServerPort
    private val port: Int,
) {

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.basePath = "/horoscopes"
    }

    @Test
    fun `get default daily horoscope`() {
        When {
            get("/daily")
        } Then {
            statusCode(200)
            contentType("application/json")
            body(jsonEquals<String>("""{"date": "${LocalDate.now()}"}"""))
        }
    }
}
