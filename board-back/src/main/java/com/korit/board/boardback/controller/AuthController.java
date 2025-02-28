package com.korit.board.boardback.controller;

import com.korit.board.boardback.dto.request.ReqAuthEmailDto;
import com.korit.board.boardback.dto.request.ReqJoinDto;
import com.korit.board.boardback.dto.request.ReqLoginDto;
import com.korit.board.boardback.dto.response.RespTokenDto;
import com.korit.board.boardback.entity.User;
import com.korit.board.boardback.service.EmailService;
import com.korit.board.boardback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Operation(summary = "회원가입", description = "회원가입 설명")
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody ReqJoinDto dto) {
        return ResponseEntity.ok().body(userService.join(dto));
    }

    @Operation(summary = "로그인" ,description = "로그인 설명")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ReqLoginDto dto) {
        /*
         * UserService -> login()
         * User 객체 findByUsername
         * user 가 있으면 비밀번호 일치하는지 확인
         * 비밀번호가 일치하면 JWT 응답
         * JwtUtil -> secret 세팅
         * */
        RespTokenDto respTokenDto = RespTokenDto.builder()
                .type("JWT")
                .name("AccessToken")
                .token(userService.login(dto))
                .build();

        return ResponseEntity.ok().body(respTokenDto);
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendAuthEmail(@RequestBody ReqAuthEmailDto dto) throws Exception {
        User user = userService.getUserByUsername(dto.getUsername())       ;
        emailService.sentAuthMail(user.getEmail(), dto.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email")
    public ResponseEntity<?> setAuthEmail(
            @RequestParam String username,
            @RequestParam String token
    ) {
        // window.close() : 인증 절차 모두 마치고 나면(전송된 메일의 인증하기 버튼 누르면) 창이 뜸 >> 이 창을 닫아야함
        String script = String.format("""
            <script>
                alert("%s");
                window.close();
            </script>
        """, emailService.auth(username, token));

        return ResponseEntity.ok().header("Content-Type", "text/html;charset=utf-8").body(script);
    }
}
