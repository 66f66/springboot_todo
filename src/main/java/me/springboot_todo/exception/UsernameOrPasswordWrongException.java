package me.springboot_todo.exception;

public class UsernameOrPasswordWrongException extends RuntimeException {

    public UsernameOrPasswordWrongException() {
        super("아이디 또는 비밀번호가 일치하지 않습니다");
    }
}
