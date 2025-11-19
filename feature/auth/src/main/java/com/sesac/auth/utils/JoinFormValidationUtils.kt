package com.sesac.auth.utils

import com.sesac.common.utils.FormValidationUtils
import com.sesac.domain.model.FormField
import com.sesac.domain.model.FormValidationResult
import com.sesac.domain.model.JoinFormState

/**
 * ViewModel에서 사용할 확장 함수
 */
fun JoinFormState.validate(): FormValidationResult {
    return FormValidationUtils.validateFields(
        // 필수 필드
        FormField.Required(
            value = email to isEmailValid,
            fieldName = "email",
            validator = { (email, isValid) ->
                FormValidationUtils.validateEmail(email, isValid)
            }
        ),
        FormField.Required(
            value = password to isPasswordValid,
            fieldName = "password",
            validator = { (password, isValid) ->
                FormValidationUtils.validatePassword(password, isValid)
            }
        ),
        FormField.Required(
            value = Triple(password, passwordConfirm, doPasswordsMatch),
            fieldName = "passwordConfirm",
            validator = { (pwd, pwdConfirm, doMatch) ->
                FormValidationUtils.validatePasswordConfirm(pwd, pwdConfirm, doMatch)
            }
        ),
        FormField.Required(
            value = name,
            fieldName = "name",
            validator = { FormValidationUtils.validateRequiredText(it, "이름") }
        ),
        FormField.Required(
            value = nickname,
            fieldName = "nickname",
            validator = { FormValidationUtils.validateRequiredText(it, "닉네임") }
        ),
        FormField.Required(
            value = agreeAge,
            fieldName = "agreeAge",
            validator = { FormValidationUtils.validateAgreement(it, "만 14세 이상 동의") }
        ),
        FormField.Required(
            value = agreeTerms,
            fieldName = "agreeTerms",
            validator = { FormValidationUtils.validateAgreement(it, "이용약관 동의") }
        ),
        FormField.Required(
            value = agreePrivacy,
            fieldName = "agreePrivacy",
            validator = { FormValidationUtils.validateAgreement(it, "개인정보 처리방침 동의") }
        ),
        // 선택 필드
        FormField.Optional(
            value = phone,
            fieldName = "phone",
            validator = { FormValidationUtils.validatePhone(it) }
        )
    )
}
