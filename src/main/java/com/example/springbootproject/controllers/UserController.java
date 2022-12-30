package com.example.springbootproject.controllers;

import com.example.springbootproject.dto.UserDto;
import com.example.springbootproject.entity.User;
import com.example.springbootproject.service.UserService;
import com.example.springbootproject.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper,
                          ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public HttpEntity<?> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{userId}")
    public HttpEntity<?> getUserProfile(@PathVariable("userId") String userId) {
        User user = userService.getUserById(UUID.fromString(userId));
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/update")
    public HttpEntity<?> updateUserProfile(@RequestBody @Valid UserDto userDto,
                                           BindingResult bindingResult, Principal principal) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        User updateUser = userService.updateUser(userDto, principal);
        UserDto user = modelMapper.map(updateUser, UserDto.class);
        return ResponseEntity.accepted().body(user);
    }
}
