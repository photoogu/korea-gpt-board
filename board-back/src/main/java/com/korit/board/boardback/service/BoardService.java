package com.korit.board.boardback.service;

import com.korit.board.boardback.dto.request.ReqWriteBoardDto;
import com.korit.board.boardback.entity.Board;
import com.korit.board.boardback.entity.BoardCategory;
import com.korit.board.boardback.entity.BoardCategoryAndBoardCount;
import com.korit.board.boardback.entity.User;
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
}
