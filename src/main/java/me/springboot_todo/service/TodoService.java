package me.springboot_todo.service;

import lombok.RequiredArgsConstructor;
import me.springboot_todo.dto.TodoDTO;
import me.springboot_todo.dto.TodoPageRequest;
import me.springboot_todo.entity.Todo;
import me.springboot_todo.entity.User;
import me.springboot_todo.repository.TodoMyBatisRepository;
import me.springboot_todo.repository.TodoRepository;
import me.springboot_todo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final ModelMapper modelMapper;
    private final TodoRepository todoRepository;
    private final TodoMyBatisRepository todoMyBatisRepository;
    private final UserRepository userRepository;

    @Transactional
    public TodoDTO createTodo(Long userId, TodoDTO todoDTO) {

        Todo todo = modelMapper.map(todoDTO, Todo.class);

        User user = userRepository.getReferenceById(userId);
        todo.setUser(user);

        int orderNumber = todoRepository
                .findMaxOrderNumberByUserId(userId)
                .orElse(0)
                + 1;
        todo.setOrderNumber(orderNumber);

        todoRepository.save(todo);

        return modelMapper.map(todo, TodoDTO.class);
    }

    @Transactional
    public TodoDTO updateTodo(Long id, Long userId, TodoDTO todoDTO) {

        Todo todo = todoRepository.findByIdAndUserIdForUpdate(id, userId)
                .orElseThrow();

        Optional.ofNullable(todoDTO.getTitle())
                .ifPresent(todo::setTitle);
        Optional.ofNullable(todoDTO.getDescription())
                .ifPresent(todo::setDescription);
        Optional.ofNullable(todoDTO.getCompleted())
                .ifPresent(todo::setCompleted);

        todoRepository.save(todo);

        return modelMapper.map(todo, TodoDTO.class);
    }

    @Transactional
    public void updateOrderNumber(Long id, Long userId, int newOrderNumber) {

        Todo movedTodo = todoRepository
                .findByIdAndUserIdForUpdate(id, userId)
                .orElseThrow();

        List<Todo> todos = todoRepository.findByUserIdOrderByOrderNumber(userId);

        int oldOrderNumber = movedTodo.getOrderNumber();

        // 같은 위치에 놓인 경우
        if (newOrderNumber == oldOrderNumber) return;

        if (newOrderNumber > oldOrderNumber) {
            // 이동할 아이템이 리스트에서 뒤로 갈 경우
            todos.stream()
                    .filter(todo -> todo.getOrderNumber() > oldOrderNumber
                            && todo.getOrderNumber() <= newOrderNumber)
                    .forEach(todo -> todo.setOrderNumber(todo.getOrderNumber() - 1));
        } else {
            // 이동할 아이템이 리스트에서 앞으로 갈 경우
            todos.stream()
                    .filter(todo -> todo.getOrderNumber() >= newOrderNumber
                            && todo.getOrderNumber() < oldOrderNumber)
                    .forEach(todo -> todo.setOrderNumber(todo.getOrderNumber() + 1));
        }

        // 이동할 아이템의 순서 업데이트
        movedTodo.setOrderNumber(newOrderNumber);

        todoRepository.saveAll(todos);

        todoRepository.save(movedTodo);
    }

    @Transactional
    public void deleteTodo(Long id, Long userId) {

        Todo todoToDelete = todoRepository
                .findByIdAndUserIdForUpdate(id, userId)
                .orElseThrow();

        int deletedOrderNumber = todoToDelete.getOrderNumber();

        todoRepository.delete(todoToDelete);

        // 이후 항목들의 orderNumber 를 조정
        List<Todo> todos = todoRepository.findByUserIdOrderByOrderNumber(userId);

        todos.stream()
                .filter(todo -> todo.getOrderNumber() > deletedOrderNumber)
                .forEach(todo -> todo.setOrderNumber(todo.getOrderNumber() - 1));

        todoRepository.saveAll(todos);
    }

    @Transactional(readOnly = true)
    public Page<TodoDTO> getTodos(Long userId, String search) {

        TodoPageRequest request = new TodoPageRequest();
        request.setUserId(userId);
        request.setSearch(search);

        return todoMyBatisRepository.getPage(request);
    }
}
