package me.springboot_todo.controller;

import lombok.RequiredArgsConstructor;
import me.springboot_todo.dto.*;
import me.springboot_todo.security.CustomUserDetails;
import me.springboot_todo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Validated(ValidationGroups.Create.class) UserDTO userDTO) {

        userService.saveUser(userDTO, "ROLE_USER");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<UsernameExistsResponse> existsUsername(@RequestParam(name = "q") String username) {

        return ResponseEntity.ok(userService.existsUsername(username));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request) {

        return ResponseEntity.ok(userService.signIn(request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/session")
    public ResponseEntity<UserDTO> getSession(@AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(userService.getUser(user.getId()));
    }
}
