package me.springboot_todo.service;

import lombok.RequiredArgsConstructor;
import me.springboot_todo.dto.SignInRequest;
import me.springboot_todo.dto.SignInResponse;
import me.springboot_todo.dto.UserDTO;
import me.springboot_todo.dto.UsernameExistsResponse;
import me.springboot_todo.entity.Role;
import me.springboot_todo.entity.User;
import me.springboot_todo.exception.UsernameOrPasswordWrongException;
import me.springboot_todo.exception.UsernameTakenException;
import me.springboot_todo.repository.RoleRepository;
import me.springboot_todo.repository.UserRepository;
import me.springboot_todo.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void saveUser(UserDTO userDTO, String roleName) {

        User user = modelMapper.map(userDTO, User.class);

        boolean exists = userRepository.existsByUsername(user.getUsername());
        if (exists) throw new UsernameTakenException();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(roleName).orElseThrow());
        user.setRoles(roles);

        try {

            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {

            String message = e.getMessage();

            if (message.contains("중복된 키") && message.contains("username"))
                throw new UsernameTakenException();
        }
    }

    @Transactional(readOnly = true)
    public UsernameExistsResponse existsUsername(String username) {

        boolean exists = userRepository.existsByUsername(username);

        UsernameExistsResponse response = new UsernameExistsResponse();
        response.setExists(exists);

        return response;
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {

        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(UsernameOrPasswordWrongException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new UsernameOrPasswordWrongException();

        String token = jwtTokenProvider.generateAccessToken(user.getUsername());

        SignInResponse response = new SignInResponse();
        response.setToken(token);

        return response;
    }

    @Transactional(readOnly = true)
    public UserDTO getUser(Long id) {

        User user = userRepository.findById(id).orElseThrow();

        return modelMapper.map(user, UserDTO.class);
    }
}
