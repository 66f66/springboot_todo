package me.springboot_todo.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id,
                             String username,
                             String password,
                             String nickname,
                             Collection<? extends GrantedAuthority> authorities
    ) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.authorities = authorities;
    }
}
