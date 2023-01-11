package com.example.springbootproject.controllers;

import com.example.springbootproject.dto.PostDto;
import com.example.springbootproject.entity.Post;
import com.example.springbootproject.exceptions.PostNotFoundException;
import com.example.springbootproject.payload.ApiResponse;
import com.example.springbootproject.service.PostService;
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
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final ModelMapper modelMapper;
    private final PostService postService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public PostController(ModelMapper modelMapper, PostService postService,
                          ResponseErrorValidation responseErrorValidation) {
        this.modelMapper = modelMapper;
        this.postService = postService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public HttpEntity<?> createPost(@RequestBody @Valid PostDto postDto,
                                    BindingResult bindingResult, Principal principal) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        Post post = postService.createPost(postDto, principal);
        PostDto createdPost = modelMapper.map(post, PostDto.class);
        createdPost.setUsername(post.getUser().getUsername());
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAllPosts() {
        List<PostDto> allPosts = postService
                .getAllPosts()
                .stream()
                .map(post -> {
                    PostDto postDto = modelMapper.map(post, PostDto.class);
                    postDto.setUsername(post.getUser().getUsername());
                    return postDto;
                })
                .toList();
        return ResponseEntity.ok(allPosts);
    }

    @GetMapping("/user/posts")
    public HttpEntity<?> getAllPostsForUser(Principal principal) {
        List<PostDto> allPostByUser = postService
                .getAllPostForUser(principal)
                .stream()
                .map(post -> {
                    PostDto postDto = modelMapper.map(post, PostDto.class);
                    postDto.setUsername(post.getUser().getUsername());
                    return postDto;
                })
                .toList();
        return ResponseEntity.ok(allPostByUser);
    }

    @PostMapping("/{postId}/{username}/like")
    public HttpEntity<?> likePost(@PathVariable("postId") String postId,
                                  @PathVariable("username") String username) {
        Post post = postService.likePost(Long.parseLong(postId), username);
        PostDto postDto = modelMapper.map(post, PostDto.class);
        postDto.setUsername(post.getUser().getUsername());
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping("/{postId}/delete")
    public HttpEntity<?> deletePost(@PathVariable("postId") String postId, Principal principal) {
        postService.deletePost(Long.parseLong(postId),principal);
        return ResponseEntity.ok(new ApiResponse("Post deleted", true));
    }

    @ExceptionHandler
    public HttpEntity<?> handleException(PostNotFoundException e) {
        var apiResponse = new ApiResponse(e.getMessage(), false);
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
