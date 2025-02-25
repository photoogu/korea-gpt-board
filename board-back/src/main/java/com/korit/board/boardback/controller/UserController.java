package com.korit.board.boardback.controller;

import com.korit.board.boardback.security.principal.PrincipalUser;
import com.korit.board.boardback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return ResponseEntity.ok().body(principalUser.getUser());
    }
}
