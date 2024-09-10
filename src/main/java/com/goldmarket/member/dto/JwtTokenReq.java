package com.goldmarket.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "AUTH_REQ_01 : 토큰 발급 요청 DTO")
public class JwtTokenReq {

    @NotNull(message = "사용자 이름 입력은 필수입니다.")
    private String name;

    @NotNull(message = "패스워드 입력은 필수입니다.")
    private String password;
}
