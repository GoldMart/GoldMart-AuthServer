package com.goldmarket.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(title = "AUTH_RES_01 : 토큰 발급 응답 DTO")
public class JwtTokenRes {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
