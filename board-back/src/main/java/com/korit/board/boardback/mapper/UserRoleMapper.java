package com.korit.board.boardback.mapper;

import com.korit.board.boardback.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int insert(UserRole userRole);
}
