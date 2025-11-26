package com.sesac.common.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun createMultipartBody(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
        val contentResolver = context.contentResolver

        // 1. 임시 파일 생성 (캐시 디렉토리)
        val type = contentResolver.getType(uri)
        val extension = type?.substringAfter("/") ?: "jpg"
        val tempFile = File(context.cacheDir, "temp_profile_image.$extension")

        try {
            // 2. URI의 데이터를 임시 파일로 복사
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {3
            e.printStackTrace()
            return null
        }

        // 3. MultipartBody.Part 생성
        val requestFile = tempFile.asRequestBody(type?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
    }
}