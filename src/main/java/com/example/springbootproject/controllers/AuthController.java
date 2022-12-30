package com.example.springbootproject.controllers;

import com.example.springbootproject.payload.ApiResponse;
import com.example.springbootproject.payload.request.LoginRequest;
import com.example.springbootproject.payload.request.SignUpRequest;
import com.example.springbootproject.security.JwtTokenProvider;
import com.example.springbootproject.security.SecurityConstants;
import com.example.springbootproject.service.UserService;
import com.example.springbootproject.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final ResponseErrorValidation responseErrorValidation;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(UserService userService,
                          ResponseErrorValidation responseErrorValidation,
                          AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.responseErrorValidation = responseErrorValidation;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signup")
    public HttpEntity<?> registerUser(@RequestBody @Valid SignUpRequest signUpRequest,
                                      BindingResult bindingResult) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        userService.createUser(signUpRequest);
        return ResponseEntity.ok(new ApiResponse("User registered successfully!",true));
    }

    @PostMapping("/signin")
    public HttpEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest,
                                          BindingResult bindingResult) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication authenticate =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.createToken(authenticate);
        return ResponseEntity.ok(new ApiResponse(token, true));
    }
}
