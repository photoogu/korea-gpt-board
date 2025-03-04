package com.korit.board.boardback.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCategory {
    private int boardCategoryId;
    private String boardCategoryName;
}
