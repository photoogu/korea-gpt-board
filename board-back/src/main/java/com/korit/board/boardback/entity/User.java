package com.korit.board.boardback.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String oAuth2Name;
    private String oAuth2Provider;
    private int accountExpired;
    private int accountLocked;
    private int credentialsExpired;
    private int accountEnabled;
    private LocalDateTime createdAt;
}
