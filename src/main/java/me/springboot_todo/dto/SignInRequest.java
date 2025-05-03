package me.springboot_todo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {

    private String username;

    private String password;
}
