package com.example.springboard.vo;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserInfoVO {
    private int userNo;
    @Email
    private String userId;
    //
    private String firstName;
    private String lastName;
    private String userBirthDate;
    private String userSex;
    private String joinDate;
    private String ipAddress;
    private String lastLogin;
    //
    private String pw;
    private String pwLastChange;
    private String pwEncryptYn;
    private String bannedYn;
    //
    private String bannedDate;
    private String bannedCode;
}

