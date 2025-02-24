package com.korit.board.boardback.service;

import com.korit.board.boardback.dto.request.ReqJoinDto;
import com.korit.board.boardback.entity.User;
import com.korit.board.boardback.exception.DuplicatedValueException;
import com.korit.board.boardback.exception.FieldError;
import com.korit.board.boardback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
                .password(reqJoinDto.getPassword())
                .email(reqJoinDto.getEmail())
                .nickname(reqJoinDto.getUsername())
                .accountExpired(1)
                .accountLocked(1)
                .credentialsExpired(1)
                .accountEnabled(1)
                .build();
        return userRepository.save(user);
    }
}
