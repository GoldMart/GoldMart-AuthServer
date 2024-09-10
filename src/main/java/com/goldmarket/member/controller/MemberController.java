package com.goldmarket.member.controller;

import com.goldmarket.member.dto.JwtTokenReq;
import com.goldmarket.member.dto.JwtTokenRes;
import com.goldmarket.member.dto.JwtTokenVerifyReq;
import com.goldmarket.member.dto.JwtTokenVerityRes;
import com.goldmarket.member.service.MemberAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 및 인가")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/token")
    @Operation(summary = "토큰 발급")
    public ResponseEntity<JwtTokenRes> getToken(
        @Valid @RequestBody JwtTokenReq req
    ) {
        JwtTokenRes jwtTokenRes = this.memberAuthService.getToken(req);
        return ResponseEntity.status(HttpStatus.OK).body(jwtTokenRes);
    }

    @PostMapping("/verifyToken")
    @Operation(summary = "토큰 검증")
    public ResponseEntity<JwtTokenVerityRes> getToken(
        @Valid @RequestBody JwtTokenVerifyReq req
    ) {
        JwtTokenVerityRes jwtTokenVerityRes = this.memberAuthService.verityToken(req);
        return ResponseEntity.status(HttpStatus.OK).body(jwtTokenVerityRes);
    }
}
