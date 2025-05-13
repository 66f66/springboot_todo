package me.springboot_todo.repository;

import me.springboot_todo.constants.RoleType;
import me.springboot_todo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleType(RoleType roleType);
}
