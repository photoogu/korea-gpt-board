package com.korit.board.boardback.repository;

import com.korit.board.boardback.entity.Board;
import com.korit.board.boardback.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepository {

    @Autowired
    private BoardMapper boardMapper;

    public Board save(Board board) {
        boardMapper.insertBoard(board);
        return board;
    }
}
