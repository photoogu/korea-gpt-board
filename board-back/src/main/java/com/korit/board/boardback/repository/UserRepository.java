package com.korit.board.boardback.repository;

import com.korit.board.boardback.entity.User;
import com.korit.board.boardback.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private UserMapper userMapper;

    public Optional<User> findById(int userId) {
        return Optional.ofNullable(userMapper.selectById(userId));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userMapper.selectByUsername(username));
    }

    public User save(User user) { // 회원가입 시 중복체크를 이미 했기 때문에 Optional 로 받을 필요가 없음(무조건 회원가입이 되기 때문)
        userMapper.insert(user);
        return user;
    }
}
