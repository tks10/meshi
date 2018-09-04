package com.takashi.meshi.util

import java.text.SimpleDateFormat
import java.util.*

fun getDateTime(unixTime: Long): String? {
    try {
        val sdf = SimpleDateFormat("MM/dd H:mm")
        val netDate = Date(unixTime * 1000)
        return sdf.format(netDate)
    } catch (e: Throwable) {
        return e.toString()
    }
}
