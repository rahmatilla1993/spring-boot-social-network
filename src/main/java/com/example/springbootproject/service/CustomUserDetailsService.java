package com.example.springbootproject.service;

import com.example.springbootproject.entity.User;
import com.example.springbootproject.repository.UserRepository;
import com.example.springbootproject.security.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            return new UserSecurity(optionalUser.get());
        }
        throw new UsernameNotFoundException("User not found");
    }

    public User loadUserById(UUID userId) {
        return userRepository
                .findById(userId)
                .orElse(null);
    }
}
