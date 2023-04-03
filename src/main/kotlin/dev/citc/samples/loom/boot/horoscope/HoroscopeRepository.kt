package dev.citc.samples.loom.boot.horoscope

import java.time.LocalDate

interface HoroscopeRepository {
    fun findDailyForSignsAt(signs: Set<ZodiacSign>, date: LocalDate): Set<DailyHoroscope>
    fun findWeeklyForSignsAt(signs: Set<ZodiacSign>, date: LocalDate): Set<WeeklyHoroscope>
}
