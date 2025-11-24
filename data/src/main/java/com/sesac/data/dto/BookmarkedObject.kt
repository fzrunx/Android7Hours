package com.sesac.data.dto

import com.squareup.moshi.JsonClass

/**
 * Sealed interface for polymorphic bookmarked objects (Path or Post).
 */
sealed interface BookmarkedObject {
    val id: Int // Common identifier for bookmarked items
}
