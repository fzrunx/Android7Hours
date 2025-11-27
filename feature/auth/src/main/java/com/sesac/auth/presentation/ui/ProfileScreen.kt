package com.sesac.auth.presentation.ui


import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    // URI -> MultipartBody.Part 로 변환하는 마법의 함수
    fun createMultipartBody(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
        val contentResolver = context.contentResolver

        // 1. 임시 파일 만들기
        val type = contentResolver.getType(uri) // 예: image/jpeg
        val extension = type?.substringAfter("/") ?: "jpg"
        val tempFile = File(context.cacheDir, "temp_profile.$extension")

        return try {
            // 2. 갤러리 이미지 데이터를 임시 파일로 복사
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // 3. 서버 전송용 객체(Multipart)로 변환
            val requestFile = tempFile.asRequestBody(type?.toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}