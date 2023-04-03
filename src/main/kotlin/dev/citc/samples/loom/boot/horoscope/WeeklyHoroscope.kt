package dev.citc.samples.loom.boot.horoscope

data class WeeklyHoroscope(
    val zodiacSign: ZodiacSign,
    val weekOfYear: String,
    val text: String,
)
