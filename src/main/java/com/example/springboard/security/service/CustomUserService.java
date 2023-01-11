package com.example.springboard.security.service;


import com.example.springboard.proc.UserMapper;
import com.example.springboard.security.userInfo.CustomUserDetails;
import com.example.springboard.security.userInfo.Role;
import com.example.springboard.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserService implements UserDetailsService {
    final UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserInfoVO userInfoVO = new UserInfoVO();
        final UserInfoVO userInfoVO = userMapper.selectUser(username);
        if(userInfoVO==null){throw new UsernameNotFoundException("");}
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority(Role.MEMBER.getCode()));
        return CustomUserDetails.builder()
                .userNo(userInfoVO.getUserNo())
                .username(userInfoVO.getUserId())
                .password(userInfoVO.getPw())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .authorities(grantedAuthoritySet)
                .build();
    }
    public boolean checkUsername(String username) {
        return userMapper.countUserById(username)==0;
    }

    public int createUser(UserInfoVO userInfoVO){
//        if(userMapper.countUserById(userInfoVO.getUserId())>0){
//            log.warn("already exist id");
//        }
        userInfoVO.setPw(passwordEncoder.encode(userInfoVO.getPw()));
        userInfoVO.setPwEncryptYn("y");
        return userMapper.createUser(userInfoVO);

    }
}
