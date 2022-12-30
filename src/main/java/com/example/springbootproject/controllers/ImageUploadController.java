package com.example.springbootproject.controllers;

import com.example.springbootproject.payload.ApiResponse;
import com.example.springbootproject.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

    @Autowired
    public ImageUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/upload")
    public HttpEntity<?> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                           Principal principal) throws IOException {
        imageUploadService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new ApiResponse("Image for user uploaded", true));
    }

    @PostMapping("/{postId}/upload")
    public HttpEntity<?> uploadImageToPost(@RequestParam("file") MultipartFile file,
                                           @PathVariable("postId") String postId,
                                           Principal principal) throws IOException {
        imageUploadService.uploadImageToPost(file,principal,Long.parseLong(postId));
        return ResponseEntity.ok(new ApiResponse("Image for post uploaded",true));
    }

    @GetMapping("/profileImage")
    public HttpEntity<?> getImageToUser(Principal principal) throws DataFormatException {
        var userImage = imageUploadService.getImageToUser(principal);
        return ResponseEntity.ok(userImage);
    }

    @GetMapping("/{postId}/image")
    public HttpEntity<?> getImageToPost(@PathVariable("postId") String postId) throws DataFormatException {
        var imagePost = imageUploadService.getImageToPost(Long.parseLong(postId));
        return ResponseEntity.ok(imagePost);
    }

    @ExceptionHandler
    public HttpEntity<?> handleException(UsernameNotFoundException e) {
        var apiResponse = new ApiResponse(e.getMessage(), false);
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
