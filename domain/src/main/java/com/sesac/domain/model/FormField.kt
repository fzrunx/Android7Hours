package com.sesac.domain.model

/**
 * 폼 필드의 유효성 검사 결과
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

/**
 * 폼 전체의 유효성 검사 결과
 */
data class FormValidationResult(
    val isValid: Boolean,
    val invalidFields: List<String> = emptyList()
)

/**
 * 폼 필드 정의
 */
sealed class FormField<T> {
    abstract val value: T
    abstract val fieldName: String

    data class Required<T>(
        override val value: T,
        override val fieldName: String,
        val validator: (T) -> ValidationResult
    ) : FormField<T>()

    data class Optional<T>(
        override val value: T,
        override val fieldName: String,
        val validator: ((T) -> ValidationResult)? = null
    ) : FormField<T>()
}
