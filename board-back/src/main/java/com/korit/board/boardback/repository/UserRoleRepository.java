package com.korit.board.boardback.repository;

import com.korit.board.boardback.entity.UserRole;
import com.korit.board.boardback.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleRepository {

    @Autowired
    private UserRoleMapper userRoleMapper;

    public UserRole save(UserRole userRole) {
        userRoleMapper.insert(userRole);
        return userRole;
    }
}
