package com.korit.board.boardback.mapper;

import com.korit.board.boardback.entity.Board;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    int insertBoard(Board board);
}
