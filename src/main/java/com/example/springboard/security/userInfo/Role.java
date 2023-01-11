package com.example.springboard.security.userInfo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {

    MEMBER("ROLE_USER","회원"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String code;
    private final String value;
}