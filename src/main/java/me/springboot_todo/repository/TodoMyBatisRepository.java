package me.springboot_todo.repository;

import me.springboot_todo.dto.TodoDTO;
import me.springboot_todo.dto.TodoPageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Mapper
public interface TodoMyBatisRepository {

    default Page<TodoDTO> getPage(TodoPageRequest request) {

        request.calculateOffset();

        return new PageImpl<>(
                selectTodos(request),
                PageRequest.of(request.getPage(), request.getSize()),
                countTodos(request)
        );
    }

    long countTodos(TodoPageRequest request);

    List<TodoDTO> selectTodos(TodoPageRequest request);
}
