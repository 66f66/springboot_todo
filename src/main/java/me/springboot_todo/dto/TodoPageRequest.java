package me.springboot_todo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TodoPageRequest extends BasePageRequest {

    private Long userId;

    private String search;

    private boolean completed;

    @Override
    protected Set<Integer> allowedSize() {

        return Set.of(5);
    }

    @Override
    protected int defaultSize() {

        return 5;
    }
}
