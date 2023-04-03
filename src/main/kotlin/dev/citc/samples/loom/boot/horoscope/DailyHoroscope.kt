package dev.citc.samples.loom.boot.horoscope

import java.time.LocalDate

data class DailyHoroscope(
    val zodiacSign: ZodiacSign,
    val date: LocalDate,
    val text: String,
)
