package com.sesac.data.mapper

import com.sesac.data.dto.InvitationCodeResponseDTO
import com.sesac.domain.model.InvitationCode

fun InvitationCodeResponseDTO.toDomain(): InvitationCode {
    return InvitationCode(code = this.code)
}
