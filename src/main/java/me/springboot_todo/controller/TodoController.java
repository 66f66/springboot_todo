package me.springboot_todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.springboot_todo.dto.TodoDTO;
import me.springboot_todo.dto.ValidationGroups;
import me.springboot_todo.security.CustomUserDetails;
import me.springboot_todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/todos")
@RestController
public class TodoController {

    private final TodoService todoService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TodoDTO> createTodo(@RequestBody @Validated(ValidationGroups.Create.class) TodoDTO todoDTO,
                                              @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(todoService.createTodo(user.getId(), todoDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public ResponseEntity<TodoDTO> updateTodo(@PathVariable Long id,
                                              @RequestBody @Valid TodoDTO todoDTO,
                                              @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(todoService.updateTodo(id, user.getId(), todoDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/order")
    public ResponseEntity<Void> updateTodoOrder(
            @PathVariable Long id,
            @RequestParam int newOrderNumber,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        todoService.updateOrderNumber(id, user.getId(), newOrderNumber);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails user) {

        todoService.deleteTodo(id, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<TodoDTO>> getTodos(@RequestParam(required = false) String search,
                                                  @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(todoService.getTodos(user.getId(), search));
    }
}
