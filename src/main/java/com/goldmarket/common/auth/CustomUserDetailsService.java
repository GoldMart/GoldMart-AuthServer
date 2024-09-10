package com.goldmarket.common.auth;

import com.goldmarket.member.domain.Member;
import com.goldmarket.member.dto.CustomUserInfo;
import com.goldmarket.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(id))
            .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다"));

        CustomUserInfo info = mapper.map(member, CustomUserInfo.class);

        return new CustomUserDetails(info);
    }

}
