package com.sesac.common.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

//@RequiresApi(Build.VERSION_CODES.O)
fun parseDate(
    dateString: String,
    locale: Locale = Locale.KOREA,
    timeZone: TimeZone = TimeZone.getTimeZone("UTC"),
): Date {
    val formats = arrayOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", // ISO 8601 with timezone offset
        "yyyy-MM-dd'T'HH:mm:ssXXX"
    )
    for (format in formats) {
        try {
            val parser = SimpleDateFormat(format, locale).apply {
                timeZone
            }
            return parser.parse(dateString) ?: continue
//            val parser = DateTimeFormatter.ofPattern(format).apply {
//                dateString.format(this)
//            }
//            return parser
        } catch (e: ParseException) {
            Log.d("TAG-ParseDate", "parse-date error: $e")
        }
    }
    // Return current date as a fallback if parsing fails
    return Date()
}