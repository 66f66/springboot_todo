package me.springboot_todo.service;

import lombok.RequiredArgsConstructor;
import me.springboot_todo.dto.TodoDTO;
import me.springboot_todo.dto.TodoPageRequest;
import me.springboot_todo.dto.UpdateOrderNumberRequest;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        int nextOrderNumber = todoRepository
                .findMaxOrderNumberByUserId(userId)
                .orElse(0)
                + 1;
        todo.setOrderNumber(nextOrderNumber);

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
    public void updateOrderNumber(List<UpdateOrderNumberRequest> requests) {

        Map<Long, Integer> orderMap = requests.stream()
                .collect(Collectors.toMap(
                        UpdateOrderNumberRequest::getId,
                        UpdateOrderNumberRequest::getOrderNumber
                ));

        List<Todo> sections = todoRepository.findAllById(orderMap.keySet());

        sections.forEach(section ->
                section.setOrderNumber(orderMap.get(section.getId()))
        );

        todoRepository.saveAll(sections);
    }

    @Transactional
    public void deleteTodo(Long id) {

        todoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TodoDTO> getTodos(Long userId, int page, String search) {

        TodoPageRequest request = new TodoPageRequest();
        request.setPage(page);
        request.setUserId(userId);
        request.setSearch(search);

        return todoMyBatisRepository.getPage(request);
    }
}
