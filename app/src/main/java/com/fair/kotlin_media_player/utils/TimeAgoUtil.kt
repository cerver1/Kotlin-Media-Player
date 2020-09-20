package com.fair.kotlin_media_player.utils

import java.util.*
import java.util.concurrent.TimeUnit

fun timeAgo(duration: Long?): String {

    val now = Date()

    return if (duration != null) {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - duration)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - duration)
        val days = TimeUnit.MILLISECONDS.toDays(now.time - duration)

        when {
            seconds < 60 -> "Just now"
            minutes.toInt() == 1 -> "a minute ago"
            minutes in 2..59 -> "$minutes minutes ago"
            hours.toInt() == 1 -> "an hour ago"
            hours in 2..24 -> "$hours hours ago"
            days.toInt() == 1 -> "a day ago"
            else -> "$days days ago"
        }
    } else {
        return "..."
    }

}