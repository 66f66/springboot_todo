package me.springboot_todo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public abstract class BasePageRequest {

    protected int page = 0;

    protected int size = defaultSize();

    protected long offset;

    protected abstract Set<Integer> allowedSize();

    protected abstract int defaultSize();

    public void calculateOffset() {

        this.offset = (long) page * size;
    }

    public void setSize(int size) {

        if (!allowedSize().contains(size)) {

            this.size = defaultSize();
        } else {

            this.size = size;
        }
    }
}
