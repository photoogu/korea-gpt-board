package com.korit.board.boardback.mapper;

import com.korit.board.boardback.entity.Board;
import com.korit.board.boardback.entity.BoardSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insertBoard(Board board);
    List<BoardSearch> selectBoardListAllBySearchOption(
            @Param("startIndex") int startIndex,
            @Param("limitCount") int limitCount,
            @Param("order") String order,
            @Param("searchText") String searchText
    );
    int selectBoardCountAllBySearchText(@Param("searchText") String searchText);
}
