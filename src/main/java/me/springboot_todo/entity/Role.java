package me.springboot_todo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import me.springboot_todo.constants.RoleType;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
