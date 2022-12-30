package com.example.springbootproject.validations;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResponseErrorValidation {

    public HttpEntity<?> mapValidationResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            for (FieldError fieldError : fieldErrors) {
//                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
//            }

            List<ObjectError> allErrors = bindingResult.getAllErrors();
            if (!CollectionUtils.isEmpty(allErrors)) {
                for (ObjectError error : allErrors) {
                    errors.put(error.getCode(), error.getDefaultMessage());
                }
            }
            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }
}
