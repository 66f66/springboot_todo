package me.springboot_todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.springboot_todo.dto.TodoDTO;
import me.springboot_todo.dto.UpdateOrderNumberRequest;
import me.springboot_todo.security.CustomUserDetails;
import me.springboot_todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/todos")
@RestController
public class TodoController {

    private final TodoService todoService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TodoDTO> createTodo(@RequestBody @Valid TodoDTO todoDTO,
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
    @PatchMapping("/orders")
    public ResponseEntity<Void> updateTodoOrderNumbers(@RequestBody List<UpdateOrderNumberRequest> requests) {

        todoService.updateOrderNumber(requests);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {

        todoService.deleteTodo(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<TodoDTO>> getTodos(@RequestParam(required = false) String search,
                                                  @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(todoService.getTodos(user.getId(), search));
    }
}
