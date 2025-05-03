package me.springboot_todo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameExistsResponse {

    private boolean exists;
}
