package com.korit.board.boardback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "사용자 로그인 정보 DTO")
public class ReqLoginDto {
    @Schema(description = "사용자이름", example = "test12345")
    private String username;
    @Schema(description = "비밀번호", example = "qwer1234")
    private String password;
}
