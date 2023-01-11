package com.example.springboard.controller;

import com.example.springboard.proc.UserMapper;
import com.example.springboard.security.jwt.JwtProvider;
import com.example.springboard.security.jwt.JwtResponseDTO;
import com.example.springboard.security.service.CustomUserService;
import com.example.springboard.security.userInfo.CustomUserDetails;
import com.example.springboard.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    final CustomUserService userService;
    UserMapper userMapper;

    @RequestMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok().body("test");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "잘못된 형식")
    })
    @Operation(method = "GET", summary = "로그인")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserInfoVO userInfoVO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("message: invalid email input");
        }else {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            userInfoVO.getUserId(), userInfoVO.getPw()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final CustomUserDetails user = userService.loadUserByUsername(userInfoVO.getUserId());
            if(user!=null) {

                Map<String, String> tokenMap = jwtProvider.createTokens("", userInfoVO.getUserId());
                JwtResponseDTO responseDTO = JwtResponseDTO.builder()
                        .accessToken(tokenMap.get("accessToken"))
                        .username(userInfoVO.getUserId())
                        .build();
                ResponseCookie cookie = ResponseCookie.from("refresh_token",tokenMap.get("refreshToken"))
                        .path("/")
                        .httpOnly(true)
                        .secure(true)
                        .build();
//              HttpHeaders headers = new HttpHeaders();
//              headers.add("Set-Cookie","platform=mobile; Max-Age=604800; Path=/; Secure; HttpOnly");
                return ResponseEntity.ok()
                        .header("set-cookie",cookie.toString())
                        .body(responseDTO);
            }

            return ResponseEntity.ok("ee");
        }
    }
    @GetMapping("/{id}/check")
    public ResponseEntity<?> checkId(@PathVariable("id") String id){
        log.info("checking Id: {}", id);
        String message = userService.
                checkUsername(id)?"Available id":"Unavailable id";
        return ResponseEntity.ok().body(message);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> singup(@Valid @RequestBody UserInfoVO userInfoVO){
        String message = userService.createUser(userInfoVO)>0?"done":"incomplete";
        return ResponseEntity.ok().body(message);
    }

}