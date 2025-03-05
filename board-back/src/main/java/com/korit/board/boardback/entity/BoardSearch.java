package com.korit.board.boardback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSearch {
    private int boardId;
    private String title;
    private String boardCategoryName;
    private String profileImg;
    private String nickname;
    private int viewCount;
    private int likeCount;
    private LocalDate createdAt;
}
