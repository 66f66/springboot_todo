package me.springboot_todo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class EnvUtil {

    private final Environment environment;

    public boolean isProd() {

        return Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }
}
