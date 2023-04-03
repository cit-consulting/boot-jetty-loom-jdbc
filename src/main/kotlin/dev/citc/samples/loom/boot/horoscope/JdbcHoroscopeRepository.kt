package dev.citc.samples.loom.boot.horoscope

import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.SqlParameterValue
import org.springframework.jdbc.core.support.AbstractSqlTypeValue
import java.sql.Connection
import java.sql.Types
import java.time.LocalDate
import java.time.temporal.WeekFields

internal class JdbcHoroscopeRepository(
    private val jdbcOps: JdbcOperations,
) : HoroscopeRepository {

    override fun findDailyForSignsAt(signs: Set<ZodiacSign>, date: LocalDate): Set<DailyHoroscope> =
        jdbcOps.query(
            """SELECT zodiac_sign, "date", "text"
               FROM daily_horoscope 
               WHERE zodiac_sign = ANY(?) AND "date" = ?
            """,
            DataClassRowMapper(DailyHoroscope::class.java),
            signs.toSqlArrayParam(),
            date,
        ).toSet()

    override fun findWeeklyForSignsAt(signs: Set<ZodiacSign>, date: LocalDate): Set<WeeklyHoroscope> {
        val year = date.get(WeekFields.ISO.weekBasedYear())
        val week = date.get(WeekFields.ISO.weekOfYear())
        return jdbcOps.query(
            """SELECT zodiac_sign, week_of_year, "text"
                   FROM weekly_horoscope 
                   WHERE zodiac_sign = ANY(?) AND week_of_year = ?
                """,
            DataClassRowMapper(WeeklyHoroscope::class.java),
            signs.toSqlArrayParam(),
            "$year-$week",
        ).toSet()
    }

    private fun Iterable<ZodiacSign>.toSqlArrayParam() =
        SqlParameterValue(Types.ARRAY, ArrayValue(map { it.name }.toTypedArray()))

    private class ArrayValue(private val array: Array<String>) : AbstractSqlTypeValue() {
        override fun createTypeValue(con: Connection, sqlType: Int, typeName: String?): Any =
            con.createArrayOf("varchar", array)
    }
}
