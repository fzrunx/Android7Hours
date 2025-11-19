package com.sesac.common.utils

import com.sesac.domain.model.FormField
import com.sesac.domain.model.FormValidationResult
import com.sesac.domain.model.ValidationResult

/**
 * 폼 유효성 검사 유틸리티
 */
object FormValidationUtils {

    /**
     * 여러 필드의 유효성을 한번에 검사
     */
    fun validateFields(vararg fields: FormField<*>): FormValidationResult {
        val invalidFields = mutableListOf<String>()

        fields.forEach { field ->
            val isValid = validateField(field)
            if (!isValid) {
                invalidFields.add(field.fieldName)
            }
        }

        return FormValidationResult(
            isValid = invalidFields.isEmpty(),
            invalidFields = invalidFields
        )
    }

    /**
     * 단일 필드 검증 (타입 안정성 확보)
     */
    private fun <T> validateField(field: FormField<T>): Boolean {
        return when (field) {
            is FormField.Required -> {
                val result = field.validator(field.value)
                result.isValid
            }
            is FormField.Optional -> {
                // 옵셔널 필드는 값이 있을 때만 검증
                field.validator?.let { validator ->
                    if (isFieldNotEmpty(field.value)) {
                        val result = validator(field.value)
                        result.isValid
                    } else {
                        true
                    }
                } ?: true
            }
        }
    }

    /**
     * 필드가 비어있지 않은지 확인
     */
    private fun isFieldNotEmpty(value: Any?): Boolean {
        return when (value) {
            is String -> value.isNotBlank()
            is Boolean -> true
            null -> false
            else -> true
        }
    }

    // === 일반적인 검증 함수들 ===

    /**
     * 이메일 유효성 검사
     */
    fun validateEmail(email: String, isValid: Boolean): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "이메일을 입력해주세요")
            !isValid -> ValidationResult(false, "올바른 이메일 형식이 아닙니다")
            else -> ValidationResult(true)
        }
    }

    /**
     * 비밀번호 유효성 검사
     */
    fun validatePassword(password: String, isValid: Boolean): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "비밀번호를 입력해주세요")
            !isValid -> ValidationResult(false, "비밀번호 조건을 만족하지 않습니다")
            else -> ValidationResult(true)
        }
    }

    /**
     * 비밀번호 확인 검사
     */
    fun validatePasswordConfirm(
        password: String,
        passwordConfirm: String,
        doMatch: Boolean
    ): ValidationResult {
        return when {
            passwordConfirm.isBlank() -> ValidationResult(false, "비밀번호 확인을 입력해주세요")
            !doMatch -> ValidationResult(false, "비밀번호가 일치하지 않습니다")
            else -> ValidationResult(true)
        }
    }

    /**
     * 필수 텍스트 필드 검사
     */
    fun validateRequiredText(text: String, fieldName: String): ValidationResult {
        return if (text.isBlank()) {
            ValidationResult(false, "${fieldName}을(를) 입력해주세요")
        } else {
            ValidationResult(true)
        }
    }

    /**
     * 약관 동의 검사
     */
    fun validateAgreement(isAgreed: Boolean, agreementName: String): ValidationResult {
        return if (!isAgreed) {
            ValidationResult(false, "${agreementName}에 동의해주세요")
        } else {
            ValidationResult(true)
        }
    }

    /**
     * 전화번호 유효성 검사 (선택적)
     */
    fun validatePhone(phone: String): ValidationResult {
        return if (phone.isBlank()) {
            ValidationResult(true) // 비어있어도 OK (선택 필드)
        } else {
            val phonePattern = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$".toRegex()
            if (phonePattern.matches(phone)) {
                ValidationResult(true)
            } else {
                ValidationResult(false, "올바른 전화번호 형식이 아닙니다")
            }
        }
    }
}