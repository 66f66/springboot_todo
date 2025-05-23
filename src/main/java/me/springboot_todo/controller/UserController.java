package me.springboot_todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.springboot_todo.dto.*;
import me.springboot_todo.enums.Role;
import me.springboot_todo.security.CustomUserDetails;
import me.springboot_todo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid UserDTO userDTO) {

        userService.saveUser(userDTO, Role.ROLE_USER);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<UsernameExistsResponse> existsUsername(@RequestParam(name = "q") String username) {

        return ResponseEntity.ok(userService.existsUsername(username));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request,
                                                 HttpServletResponse response) {

        return ResponseEntity.ok(userService.signIn(request, response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authentication")
    public ResponseEntity<UserDTO> getAuthentication(@AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(userService.getUser(user.getId()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(HttpServletRequest request,
                                                             HttpServletResponse response) {

        return ResponseEntity.ok(userService.refreshToken(request, response));
    }

    @DeleteMapping("/sign-out")
    public ResponseEntity<Void> signOut(HttpServletResponse response) {

        userService.signOut(response);

        return ResponseEntity.ok().build();
    }
}
