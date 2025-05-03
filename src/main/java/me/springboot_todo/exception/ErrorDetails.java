package me.springboot_todo.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class ErrorDetails {

    private LocalDateTime timestamp;

    private String message;

    private String details;
}
