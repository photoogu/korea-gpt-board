package com.korit.board.boardback.mapper;

import com.korit.board.boardback.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectById(int userId);
    User selectByUsername(String username);
    int insert(User user);
}
