package com.goldmarket.member.service;

import com.goldmarket.common.util.JwtUtil;
import com.goldmarket.member.domain.Member;
import com.goldmarket.member.dto.CustomUserInfo;
import com.goldmarket.member.dto.JwtTokenReq;
import com.goldmarket.member.dto.JwtTokenRes;
import com.goldmarket.member.dto.JwtTokenVerifyReq;
import com.goldmarket.member.dto.JwtTokenVerityRes;
import com.goldmarket.member.repository.MemberRepository;
import com.goldmarket.refresh_token.domain.RefreshToken;
import com.goldmarket.refresh_token.repository.RefreshTokenRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public JwtTokenRes getToken(JwtTokenReq req) {
        String name = req.getName();
        String password = req.getPassword();
        Member member = memberRepository.findMemberByName(name);
        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }

        if (!encoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        CustomUserInfo info = modelMapper.map(member, CustomUserInfo.class);
        JwtTokenRes jwtTokenRes = jwtUtil.generateAccessAndRefreshToken(info);

        // 발급한 refresh token을 DB에 저장
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMember_Id(member.getId());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(
                refreshToken.get().updateToken(jwtTokenRes.getRefreshToken()));
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                .member(member)
                .token(jwtTokenRes.getRefreshToken())
                .build());
        }
        return jwtTokenRes;
    }

    @Transactional
    public JwtTokenVerityRes verityToken(JwtTokenVerifyReq req) {
        return null;
    }
}
