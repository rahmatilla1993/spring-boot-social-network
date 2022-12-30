package com.example.springbootproject.service;

import com.example.springbootproject.dto.CommentDto;
import com.example.springbootproject.entity.Comment;
import com.example.springbootproject.entity.Post;
import com.example.springbootproject.entity.User;
import com.example.springbootproject.exceptions.PostNotFoundException;
import com.example.springbootproject.repository.CommentRepository;
import com.example.springbootproject.repository.PostRepository;
import com.example.springbootproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class CommentService {

    public static final Logger LOG = LoggerFactory.getLogger( CommentService.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Long postId, CommentDto commentDto, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username " + user.getUsername()));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDto.getMessage());

        LOG.info("Saving comment for Post : {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long commentId) {
        commentRepository
                .findById(commentId)
                .ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }
}
