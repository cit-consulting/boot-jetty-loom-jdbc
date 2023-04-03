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
            body(jsonEquals<String>(dailyDefault))
        }
    }

    @Test
    fun `get default weekly horoscope`() {
        When {
            get("/weekly")
        } Then {
            statusCode(200)
            contentType("application/json")
            body(jsonEquals<String>(weeklyDefault))
        }
    }
}

private val now = LocalDate.now()

// language=JSON
private val dailyDefault = """[
  {
    "zodiacSign": "Aries",
    "date": "$now",
    "text": "Turn your attention to finances and try to come up with income-generating ideas like a lucrative side hustle or ideal clients you could pitch. See if you can think of ways to enhance your brand’s “value proposition” during these uncertain economic times. Then crunch the numbers for potential business ventures and new projects so you can assess which to prioritize."
  },
  {
    "zodiacSign": "Gemini",
    "date": "$now",
    "text": "As the sign most associated with communication, you know there are many forms of expression, from active to passive, verbal to non, and so on."
  }
]"""

// language=JSON
private const val weeklyDefault = """[
  {
    "zodiacSign": "Aries",
    "weekOfYear": "2023-14",
    "text": "As abundant, lucky Jupiter finishes its last full month in Aries, it’s time to double down on your personal goals. You won’t want to squander one second of this clarifying month because after May 16, Jupiter won’t be back in your sign for another decade!"
  },
  {
    "zodiacSign": "Gemini",
    "weekOfYear": "2023-14",
    "text": "Sort through your squad and the people you spend the most time with: Do they elevate or irritate you? Do they share your vision for life? It might be time to give a few seats to members who are aligned with your mission. As for the ones that irk you? Well, maybe a few of them are mirroring some of your own not-so-savory traits—or maybe they’re messengers who are there to help you grow."
  }
]"""
