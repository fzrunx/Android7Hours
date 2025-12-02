package com.sesac.common.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

//@RequiresApi(Build.VERSION_CODES.O)
fun parseDate(
    dateString: String,
    locale: Locale = Locale.KOREA,
    timeZone: TimeZone = TimeZone.getTimeZone("UTC"),
): Date {
    // Pre-process dateString to handle arbitrary fractional seconds (truncate to milliseconds)
    var processedDateString = dateString
    val fractionalSecondsRegex = Regex("(?<=\\.\\d{3})\\d+(?=[Z+-])") // Regex to find more than 3 fractional digits
    fractionalSecondsRegex.find(dateString)?.let { match ->
        // Found more than 3 digits, truncate them.
        processedDateString = dateString.replaceRange(match.range, "")
        Log.d("TAG-ParseDate", "Truncated fractional seconds: $dateString -> $processedDateString")
    }

    val formats = arrayOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", // ISO 8601 with timezone offset and 3 fractional seconds
        "yyyy-MM-dd'T'HH:mm:ssXXX",     // ISO 8601 with timezone offset (no fractional seconds)
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", // ISO 8601 with Z and 3 fractional seconds
        "yyyy-MM-dd'T'HH:mm:ss'Z'",     // ISO 8601 with Z (no fractional seconds)
        "yyyy-MM-dd HH:mm:ss",          // Common format (added for robustness)
        "yyyy-MM-dd"                    // Date only format (added for robustness)
    )
    for (format in formats) {
        try {
            val parser = SimpleDateFormat(format, locale).apply {
                this.timeZone = timeZone // Correctly set the timezone
            }
            return parser.parse(processedDateString) ?: continue
        } catch (e: ParseException) {
            Log.d("TAG-ParseDate", "parse-date error with format '$format': $e")
        }
    }
    // Return current date as a fallback if parsing fails
    Log.e("TAG-ParseDate", "Failed to parse date: $dateString with any format. Returning current date.")
    return Date()
}