package com.korit.board.boardback.service;

import com.korit.board.boardback.dto.request.ReqBoardListSearchDto;
import com.korit.board.boardback.dto.request.ReqWriteBoardDto;
import com.korit.board.boardback.entity.*;
import com.korit.board.boardback.repository.BoardCategoryRepository;
import com.korit.board.boardback.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardCategoryRepository boardCategoryRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Transactional(rollbackFor = Exception.class)
    public Board createBoard(String categoryName, User user, ReqWriteBoardDto reqWriteBoardDto) {
        BoardCategory boardCategory = boardCategoryRepository
                .findByName(categoryName)
                .orElseGet(() -> {
                    BoardCategory bc = BoardCategory.builder()
                            .boardCategoryName(categoryName)
                            .build();
                    return boardCategoryRepository.save(bc);
                });

        Board board = Board.builder()
                .boardCategoryId(boardCategory.getBoardCategoryId())
                .userId(user.getUserId())
                .title(reqWriteBoardDto.getTitle())
                .content(reqWriteBoardDto.getContent())
                .build();
        return boardRepository.save(board);
    }

    public List<BoardCategoryAndBoardCount> getBoardCategoriesByUserId(User user) {
        return boardCategoryRepository.findAllByUserId(user.getUserId());
    }

    @Transactional(readOnly = true) // 읽기 전용 최적화 >> 데이터의 양이 많을 때
    public List<BoardSearch> getBoardListSearchBySearchOption(ReqBoardListSearchDto reqBoardListSearchDto) {
        int startIndex = (reqBoardListSearchDto.getPage() - 1) * reqBoardListSearchDto.getLimitCount();
        return boardRepository.findBoardListAllBySearchOption(
                startIndex,
                reqBoardListSearchDto.getLimitCount(),
                reqBoardListSearchDto.getOrder(),
                reqBoardListSearchDto.getSearchText()
        );
    }

    @Transactional(readOnly = true)
    public int getBoardListCountBySearchText(String searchText) {
        return boardRepository.findBoardCountAllBySearchText(searchText);
    }
}
