package com.sesac.common.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

//@RequiresApi(Build.VERSION_CODES.O)
fun parseDate(
    dateString: String,
    locale: Locale = Locale.KOREA,
    // defaultTimeZone: TimeZone = TimeZone.getTimeZone("Asia/Seoul"), // 기본 타임존은 파싱 로직 내에서 결정
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
        // 1. Django 서버에서 주로 오는 형식 (암시적으로 Asia/Seoul)
        "yyyy-MM-dd HH:mm:ss",

        // 2. ISO 8601 + 타임존 오프셋 (밀리초 포함)
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        // 3. ISO 8601 + 타임존 오프셋 (밀리초 없음)
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        // 4. ISO 8601 + Z (UTC) + 밀리초
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        // 5. ISO 8601 + Z (UTC) (밀리초 없음)
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        // 6. Date.toString() 형식 (암시적으로 시스템 기본 타임존)
        "EEE MMM dd HH:mm:ss zzz yyyy",
        // 7. 기타 EEE 형식 (GMT 포함)
        "EEE MMM dd HH:mm:ss 'GMT'Z yyyy",
        "EEE MMM dd HH:mm:ss 'GMT'XXX yyyy",
        // 8. 날짜만 있는 형식
        "yyyy-MM-dd",
    )
    for (format in formats) {
        try {
            val parser = if (format.startsWith("EEE")) {
                SimpleDateFormat(format, Locale.ENGLISH)
            } else {
                SimpleDateFormat(format, locale)
            }.apply {
                // 날짜 포맷 스트링에 타임존 정보(Z, X, z)가 명시적으로 포함되어 있는지 확인
                val hasExplicitTimeZone = format.contains("Z") || format.contains("X") || format.contains("z")
                
                // 명시적 타임존 정보가 없으면, Django 서버의 TIME_ZONE과 동일하게 Asia/Seoul로 설정
                // 그렇지 않으면, 포맷 스트링의 타임존 정보를 따르거나 (Z, X) UTC를 기본으로 사용
                this.timeZone = if (hasExplicitTimeZone) {
                    TimeZone.getTimeZone("UTC") // 명시적 타임존은 UTC 기준으로 파싱 후 변환
                } else {
                    TimeZone.getTimeZone("Asia/Seoul") // Django 서버 타임존과 일치시킴
                }
            }
            return parser.parse(processedDateString) ?: continue
        } catch (e: ParseException) {
            Log.d("TAG-ParseDate", "parse-date error with format '$format': $e")
        }
    }
    // simpleDataFormat.format(Date()) // 이 라인은 불필요하여 제거
    // Return current date as a fallback if parsing fails
    Log.e("TAG-ParseDate", "Failed to parse date: $dateString with any format. Returning current date.")
    return Date()
}