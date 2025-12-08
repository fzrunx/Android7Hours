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
        val mimeType = contentResolver.getType(uri)
        val extension = when {
            mimeType?.contains("webp") == true -> "webp"
            mimeType?.contains("png") == true -> "png"
            else -> "jpeg"
        }

        // [핵심] 파일명에 현재 시간을 붙여서 유니크하게 만듦
        val uniqueFileName = "profile_${System.currentTimeMillis()}.$extension"
        val tempFile = File(context.cacheDir, uniqueFileName)

        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            val requestFile = tempFile.asRequestBody(mimeType?.toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, uniqueFileName, requestFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}