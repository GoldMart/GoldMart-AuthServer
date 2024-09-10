package com.goldmarket.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(title = "AUTH_RES_02 : 토큰 검증 응답 DTO")
public class JwtTokenVerityRes {
    String isValid;
    String username;
}
