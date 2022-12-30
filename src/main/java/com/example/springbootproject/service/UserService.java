package com.example.springbootproject.service;

import com.example.springbootproject.dto.UserDto;
import com.example.springbootproject.entity.User;
import com.example.springbootproject.enums.Role;
import com.example.springbootproject.exceptions.UserExistException;
import com.example.springbootproject.payload.request.SignUpRequest;
import com.example.springbootproject.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.UUID;

@Service
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public User createUser(SignUpRequest userIn) {
        User user = modelMapper.map(userIn, User.class);
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        try {
            LOG.info("Saving user {}", user.getEmail());
            return userRepository.save(user);
        }
        catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exists");
        }
    }

    public User getUserById(UUID userId) {
       return userRepository
                .findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    public User updateUser(UserDto userDto, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBio(userDto.getBio());
        return userRepository.save(user);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }
}
