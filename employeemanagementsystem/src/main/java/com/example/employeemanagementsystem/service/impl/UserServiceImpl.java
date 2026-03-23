package com.example.employeemanagementsystem.service.impl;

import com.example.employeemanagementsystem.component.MessageHelper;
import com.example.employeemanagementsystem.config.JwtService;
import com.example.employeemanagementsystem.dto.UserDTO;
import com.example.employeemanagementsystem.exception.*;
import com.example.employeemanagementsystem.model.User;
import com.example.employeemanagementsystem.repository.UserRepository;
import com.example.employeemanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.example.employeemanagementsystem.service.impl.UserServiceImpl.ValidationRule.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    // check validations
    enum ValidationRule {
        CHECK_ID,
        CHECK_USERNAME,
        CHECK_PASSWORD,
        CHECK_ROLE,
        CHECK_EXISTS,
        CHECK_DUPLICATE_NAME
    }

    public User getByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }


    public Page<UserDTO> findAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user ->
                new UserDTO(user.getUserId(),  user.getUsername(), "", user.getRole()));
    }

    public Page<UserDTO> findByUsernameContainingIgnoreCase(String username, Pageable pageable){
        Page<User> users = userRepository.findByUsernameContainingIgnoreCase(username, pageable);
        return users.map(user ->
                new UserDTO(user.getUserId(), user.getUsername(), "", user.getRole()));
    }

    public UserDTO registerUser(UserDTO userDTO) {

        validateUser(userDTO, CHECK_USERNAME, CHECK_PASSWORD, CHECK_ROLE, CHECK_DUPLICATE_NAME);

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // hash password
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        } else {
            user.setRole("ROLE_USER");
        }
        User newUser = userRepository.save(user);
        return new UserDTO(newUser.getUserId(), newUser.getUsername(), "", newUser.getRole());
    }

    public UserDTO updateUser(UserDTO userDTO) {
        validateUser(userDTO, CHECK_ID, CHECK_USERNAME, CHECK_ROLE, CHECK_EXISTS, CHECK_DUPLICATE_NAME);
        User user = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(userDTO.getUserId()));
        User updateUser = new User();
        updateUser.setUserId(userDTO.getUserId());
        updateUser.setUsername(userDTO.getUsername());
        updateUser.setPassword(user.getPassword());
        updateUser.setRole(userDTO.getRole());
        userRepository.save(updateUser);
        return new UserDTO(userDTO.getUserId(), userDTO.getUsername(), "", userDTO.getRole());
    }

    public UserDTO deleteUser(UserDTO userDTO) {
        validateUser(userDTO, CHECK_ID, CHECK_EXISTS);
        User deletedUser = new User();
        deletedUser.setUserId(userDTO.getUserId());
        userRepository.delete(deletedUser);
        return new UserDTO(userDTO.getUserId(), deletedUser.getUsername(), "", deletedUser.getRole());
    }

    public String login(UserDTO userDTO) {
        User dbUser = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDTO.getUsername()));
        if (passwordEncoder.matches(userDTO.getPassword(), dbUser.getPassword())) {
            return jwtService.generateToken(dbUser.getUsername(), dbUser.getRole());
        }
        throw new BadCredentialsException(MessageHelper.get("error.auth.bad.credentials"));
    }

    private void validateUser(UserDTO userDTO, ValidationRule... rules) {
        Set<ValidationRule> ruleSet = Set.of(rules);

        if (ruleSet.contains(CHECK_USERNAME) &&
                (userDTO.getUsername() == null || userDTO.getUsername().isBlank())) {
            throw new MissingFieldsException(MissingFieldsException.USER_USERNAME);
        }

        if (ruleSet.contains(CHECK_ID) && userDTO.getUserId() == null) {
            throw new MissingFieldsException(MissingFieldsException.USER_ID);
        }

        if (ruleSet.contains(CHECK_PASSWORD) &&
                (userDTO.getPassword() == null || userDTO.getPassword().isBlank())) {
            throw new MissingFieldsException(MissingFieldsException.USER_PASSWORD);
        }

        if (ruleSet.contains(CHECK_ROLE) &&
                (userDTO.getRole() == null || userDTO.getRole().isBlank())) {
            throw new MissingFieldsException(MissingFieldsException.USER_ROLE);
        }

        if (ruleSet.contains(CHECK_EXISTS)) {
            userRepository.findById(userDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(userDTO.getUserId()));
        }

        if (ruleSet.contains(CHECK_DUPLICATE_NAME)) {
            User existingUser = userRepository.findByUsername(userDTO.getUsername()).orElse(null);
            if (existingUser != null && !existingUser.getUserId().equals(userDTO.getUserId())) {
                throw new UserAlreadyExistsException(userDTO.getUsername());
            }
        }
    }
}
