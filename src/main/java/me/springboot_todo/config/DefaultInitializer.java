package me.springboot_todo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.springboot_todo.entity.Role;
import me.springboot_todo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultInitializer {

    private final static String ROLE_ADMIN = "ROLE_ADMIN";
    private final static String ROLE_USER = "ROLE_USER";

    private final RoleRepository roleRepository;

    @Transactional
    @Bean
    CommandLineRunner init() {

        return args -> {

            if (roleRepository.count() == 0) {

                Role adminRole = new Role();
                adminRole.setName(ROLE_ADMIN);

                Role userRole = new Role();
                userRole.setName(ROLE_USER);

                List<Role> roles = List.of(adminRole, userRole);

                roleRepository.saveAll(roles);
            }
        };
    }
}
