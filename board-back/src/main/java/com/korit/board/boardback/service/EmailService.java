package com.korit.board.boardback.service;

import com.korit.board.boardback.entity.User;
import com.korit.board.boardback.repository.UserRepository;
import com.korit.board.boardback.security.jwt.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailService {

    private final String FROM_EMAIL = "hanigirl0@gmail.com";

    @Autowired(required = false)
    private JavaMailSender javaMailSender;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @Async
    public void sentAuthMail(String to, String username) throws MessagingException {
        String emailToken = jwtUtil.generateToken(null, null, new Date(new Date().getTime() + 1000l * 60 * 5));
        String href = "http://localhost:8080/api/auth/email?username=" + username + "&token=" + emailToken;

        final String SUBJECT = "[board_project] 계정 활성화 인증 메일입니다.";
        String content = String.format("""        
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
            </head>
            <body>
                <div style="display: flex; flex-direction: column; align-items: center;">
                    <h1>계정 활성화</h1>
                    <p>계정 활성화를 하시려면 아래의 인증 버튼을 클릭하세요.</p>
                    <a style="box-sizing: border-box; border: none; border-radius: 8px; padding: 7px 15px; background-color: #2383e2; color: #ffffff; text-decoration: none;" target="_blank" href="%s">인증하기</a>
                </div>
            </body>
            </html>
        """, href); // href="%s" >> %s 에 href 넣을 수 있음

        sendMail(to, SUBJECT, content);
    }

    public void sendMail(String to, String subject, String content) throws MessagingException { // 메일이 전송 되지 않은 경우 등의 메일 전송 관련 예외 처리
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setFrom(FROM_EMAIL);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessage.setText(content, StandardCharsets.UTF_8.name(), "html");

        javaMailSender.send(mimeMessage);
    }

    @Transactional(rollbackFor = Exception.class)
    public String auth(String username, String token) {
        String responseMessage = "";
        try {
            jwtUtil.parseToken(token);
            Optional<User> userOptional = userRepository.findByUsername(username);
            if(userOptional.isEmpty()) {
                responseMessage = "[인증실패] 존재하지 않는 사용자입니다.";
            } else {
                User user = userOptional.get();
                if(user.getAccountEnabled() == 1) {
                    responseMessage = "[인증실패] 이미 인증된 사용자입니다.";
                } else {
                    userRepository.updateAccountEnabled(username);
                    responseMessage = "[인증성공] 인증에 성공하였습니다.";
                }
            }

        } catch (Exception e) {
            responseMessage = "[인증실패] 토큰이 유효하지 않거나 인증 시간을 초과하였습니다.";
        }

        return responseMessage;
    }

    public String generateEmailCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(1000000));
    }

    @Async
    public void sendChangeEmailVerification(String to, String code) throws MessagingException {
        final String SUBJECT = "[board_project] 계정 활성화 인증 메일입니다.";
        String content = String.format("""        
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
            </head>
            <body>
                <div style="display: flex; flex-direction: column; align-items: center;">
                    <h1>계정 활성화</h1>
                    <p>계정 활성화를 하시려면 아래의 인증 코드 번호를 확인하세요.</p>
                    <h3 style="background-color: #2383e2; color:#ffffff; margin: 20px 0">%s</h3>
                </div>
            </body>
            </html>
        """, code);

        sendMail(to, SUBJECT, content);
    }
}
