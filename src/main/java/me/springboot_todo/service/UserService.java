package me.springboot_todo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.springboot_todo.dto.*;
import me.springboot_todo.entity.User;
import me.springboot_todo.enums.Role;
import me.springboot_todo.enums.ValidateTokenResult;
import me.springboot_todo.exception.UsernameOrPasswordWrongException;
import me.springboot_todo.exception.UsernameTakenException;
import me.springboot_todo.repository.UserRepository;
import me.springboot_todo.security.JwtTokenProvider;
import me.springboot_todo.util.CookieUtil;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final String COOKIE_NAME = "springboot_todo_refresh_token";
    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30일

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final UserRepository userRepository;

    @Transactional
    public void saveUser(UserDTO userDTO, Role role) {

        User user = modelMapper.map(userDTO, User.class);

        boolean exists = userRepository.existsByUsername(user.getUsername());
        if (exists) throw new UsernameTakenException();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(role);

        try {

            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {

            String message = e.getMessage();

            if (message.contains("중복된 키") && message.contains("username"))
                throw new UsernameTakenException();
        }
    }

    @Transactional(readOnly = true)
    public UsernameExistsResponse existsUsername(String username) {

        return new UsernameExistsResponse(userRepository.existsByUsername(username));
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request,
                                 HttpServletResponse response) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(UsernameOrPasswordWrongException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new UsernameOrPasswordWrongException();

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        cookieUtil.setCookie(COOKIE_NAME, refreshToken, COOKIE_PATH, COOKIE_MAX_AGE, response);

        return new SignInResponse(accessToken);
    }

    @Transactional(readOnly = true)
    public UserDTO getUser(Long id) {

        User user = userRepository.findById(id).orElseThrow();

        return modelMapper.map(user, UserDTO.class);
    }

    public RefreshTokenResponse refreshToken(HttpServletRequest request,
                                             HttpServletResponse response) {

        String refreshToken = cookieUtil.getCookie(request.getCookies(), COOKIE_NAME);

        if (refreshToken == null) throw new AccessDeniedException("리프레시 토큰이 없습니다");

        if (jwtTokenProvider.validateToken(refreshToken) != ValidateTokenResult.VALID)
            throw new AccessDeniedException("유효한 토큰이 아닙니다");

        String username = jwtTokenProvider.extractSubjectFromToken(refreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        cookieUtil.setCookie(COOKIE_NAME, newRefreshToken, COOKIE_PATH, COOKIE_MAX_AGE, response);

        return new RefreshTokenResponse(newAccessToken);
    }

    public void signOut(
            HttpServletResponse response) {

        cookieUtil.removeCookie(COOKIE_NAME, COOKIE_PATH, response);
    }
}
