package com.korit.board.boardback.service;

import com.korit.board.boardback.dto.request.ReqJoinDto;
import com.korit.board.boardback.dto.request.ReqLoginDto;
import com.korit.board.boardback.entity.User;
import com.korit.board.boardback.entity.UserRole;
import com.korit.board.boardback.exception.DuplicatedValueException;
import com.korit.board.boardback.exception.FieldError;
import com.korit.board.boardback.repository.UserRepository;
import com.korit.board.boardback.repository.UserRoleRepository;
import com.korit.board.boardback.security.jwt.JwtUtil;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private EmailService emailService;

    public User getUserByUsername(String username) throws Exception {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("사용자를 찾지 못했습니다."));
    }

    public boolean duplicatedByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional(rollbackFor = Exception.class)
    public User join(ReqJoinDto reqJoinDto) {
        if(duplicatedByUsername(reqJoinDto.getUsername())) {
            throw new DuplicatedValueException(List.of(FieldError.builder()
                            .field("username")
                            .message("이미 존재하는 사용자이름입니다.")
                            .build()));
        }
        User user = User.builder()
                .username(reqJoinDto.getUsername())
                .password(passwordEncoder.encode(reqJoinDto.getPassword()))
                .email(reqJoinDto.getEmail())
                .nickname(reqJoinDto.getUsername())
                .accountExpired(1)
                .accountLocked(1)
                .credentialsExpired(1)
                .accountEnabled(0)
                .build();
        userRepository.save(user);

        UserRole userRole = UserRole.builder()
                .userId(user.getUserId())
                .roleId(1)      // 원래는 role_tb 에서 select 로 찾아서 넣어야함
                .build();
        userRoleRepository.save(userRole);

        try { // 계정 생성하면 인증 메일 보내주기! 근데 메일이 전송 안됐다고 회원가입 실패하면 안되니까, 메서드에 throws 로 예외 처리하지 않고 try-catch 사용!
            emailService.sentAuthMail(reqJoinDto.getEmail(), reqJoinDto.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public String login(ReqLoginDto reqLoginDto) {
        User user = userRepository
                .findByUsername(reqLoginDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 다시 확인하세요."));

        if(!passwordEncoder.matches(reqLoginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 확인하세요.");
        }

        // 이메일 인증 여부 확인
        if(user.getAccountEnabled() == 0) {
            throw new DisabledException("이메일 인증이 필요합니다.");
        }

        Date expires = new Date(new Date().getTime() + (1000l * 60 * 60 * 24 * 7));

        return jwtUtil.generateToken(
                user.getUsername(),
                Integer.toString(user.getUserId()),
                expires);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProfileImg(User user, MultipartFile file) {
        final String PROFILE_IMG_FILE_PATH = "/upload/user/profile";
        String saveFileName = fileService.saveFile(PROFILE_IMG_FILE_PATH, file);
        userRepository.updateProfileImg(user.getUserId(), saveFileName);
        if(user.getProfileImg() == null) {
            return;
        }
        fileService.deleteFile(PROFILE_IMG_FILE_PATH + "/" + user.getProfileImg());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateNickname(User user, String nickname) {
        userRepository.updateNickname(user.getUserId(), nickname);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(User user, String password) {
        String encodedPassword = passwordEncoder.encode(password);  // password 는 encoding 이 필요함
        userRepository.updatePassword(user.getUserId(), encodedPassword);
    }
}
