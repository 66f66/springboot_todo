package me.springboot_todo.exception;

public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException() {
        super("이미 사용중인 아이디입니다");
    }
}
