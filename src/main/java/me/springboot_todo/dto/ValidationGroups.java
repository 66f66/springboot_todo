package me.springboot_todo.dto;

public interface ValidationGroups {

    interface Create {
    }

    interface Update {
    }

    interface Common extends Create, Update {
    }
}
