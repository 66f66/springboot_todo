package me.springboot_todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RefreshTokenResponse {

    private String newAccessToken;
}
