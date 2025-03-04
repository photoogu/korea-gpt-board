package com.korit.board.boardback.controller;

import com.korit.board.boardback.dto.request.ReqWriteBoardDto;
import com.korit.board.boardback.security.principal.PrincipalUser;
import com.korit.board.boardback.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/{categoryName}")
    public ResponseEntity<?> createBoard(
            @PathVariable String categoryName,
            @RequestBody ReqWriteBoardDto dto,
            @AuthenticationPrincipal PrincipalUser principalUser
            ) {

        return ResponseEntity.ok().body(boardService.createBoard(categoryName, principalUser.getUser(), dto));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(@AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok().body(boardService.getBoardCategoriesByUserId(principalUser.getUser()));
    }
}
