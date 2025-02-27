package com.korit.board.boardback.dto.request;

import lombok.Data;

@Data
public class ReqAuthEmailDto {
    private String email; // 인증 메일 보낼 이메일 주소!
    private String username;
}
