package com.korit.board.boardback.repository;

import com.korit.board.boardback.entity.Board;
import com.korit.board.boardback.entity.BoardSearch;
import com.korit.board.boardback.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.OptionalInt;

@Repository
public class BoardRepository {

    @Autowired
    private BoardMapper boardMapper;

    public Board save(Board board) {
        boardMapper.insertBoard(board);
        return board;
    }

    public List<BoardSearch> findBoardListAllBySearchOption(
            int startIndex,
            int limitCount,
            String order,
            String searchText) {
        return boardMapper.selectBoardListAllBySearchOption(startIndex, limitCount, order, searchText);
    }

    public int findBoardCountAllBySearchText(String searchText) {
        return boardMapper.selectBoardCountAllBySearchText(searchText);
    }
}
