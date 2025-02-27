package com.korit.board.boardback.controller;

import com.korit.board.boardback.security.principal.PrincipalUser;
import com.korit.board.boardback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/me")
    public ResponseEntity<?> getLoginUser(@AuthenticationPrincipal PrincipalUser principalUser) {
//        PrincipalUser principalUser1 =
//                (PrincipalUser) SecurityContextHolder
//                        .getContext()
//                        .getAuthentication()
//                        .getPrincipal();              // 이 코드를 @AuthenticationPrincipal 하나로 해결 가능

//        int userId = principalUser.getUser().getUserId();     // login 시 인증 토큰을 받기 때문에 이렇게 할 필요 X
        if(principalUser.getUser().getProfileImg() == null) {
            principalUser.getUser().setProfileImg("default.jpg");
        }
        return ResponseEntity.ok().body(principalUser.getUser());
    }

    @PostMapping("/user/profile/img")
    public ResponseEntity<?> changeProfileImg(@AuthenticationPrincipal PrincipalUser principalUser, @RequestPart MultipartFile file) {
        userService.updateProfileImg(principalUser.getUser(), file);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/profile/nickname")
    public ResponseEntity<?> changeNickname(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody Map<String, String> requestBody
            ) {
        String nickname = requestBody.get("nickname");
        userService.updateNickname(principalUser.getUser(), nickname);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/profile/password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody Map<String, String> requestBody
    ) {
        String password = requestBody.get("password");
        userService.updatePassword(principalUser.getUser(), password);
        return ResponseEntity.ok().build();
    }
}
