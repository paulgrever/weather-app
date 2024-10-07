package com.paul.weatherapp.business

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId


class DateTimeHelper {
    companion object {
        const val WEATHER_APP_DATE_FORMAT = "EEEE, MMM d, yyyy"
    }

    private val formatter = DateTimeFormatter.ofPattern(WEATHER_APP_DATE_FORMAT)
        .withZone(ZoneId.systemDefault())

    fun getFriendlyDate(unixTimestamp: Long): String {
        val currentDateTime = LocalDateTime.now()
        val eventDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneId.systemDefault())

        val isToday = eventDateTime.toLocalDate().isEqual(currentDateTime.toLocalDate())
        val isTomorrow =
            eventDateTime.toLocalDate().isEqual(currentDateTime.plusDays(1).toLocalDate())

        val timeDescription = getTimeDescription(eventDateTime)

        return when {
            isToday -> "Today $timeDescription"
            isTomorrow -> "Tomorrow"
            else -> eventDateTime.format(formatter) // Falls back to a formatted date if not today or tomorrow
        }
    }

    private fun getTimeDescription(dateTime: LocalDateTime): String {
        return when (dateTime.hour) {
            in 5..11 -> "morning"
            in 12..16 -> "afternoon"
            in 17..20 -> "evening"
            else -> "night"
        }
    }
}
