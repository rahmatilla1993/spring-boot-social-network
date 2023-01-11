package com.example.springbootproject.service;

import com.example.springbootproject.entity.ImageModel;
import com.example.springbootproject.entity.Post;
import com.example.springbootproject.entity.User;
import com.example.springbootproject.exceptions.ImageNotFoundException;
import com.example.springbootproject.exceptions.PostNotFoundException;
import com.example.springbootproject.repository.ImageRepository;
import com.example.springbootproject.repository.PostRepository;
import com.example.springbootproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService {

    public static final Logger LOG = LoggerFactory.getLogger(ImageUploadService.class);
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public ImageUploadService(ImageRepository imageRepository,
                              UserRepository userRepository,
                              PostRepository postRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        LOG.info("Uploading image profile to User {}", user.getUsername());
        ImageModel userProfileImage = imageRepository
                .findByUserId(user.getId())
                .orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }
        var imageModel = new ImageModel();
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        imageModel.setUserId(user.getId());
        return imageRepository.save(imageModel);
    }

    public ImageModel uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post userPost = user.getPosts()
                .stream()
                .filter(post -> post.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        var imageModel = new ImageModel();
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        imageModel.setPostId(userPost.getId());
        LOG.info("Uploading image to Post {}", userPost.getId());
        return imageRepository.save(imageModel);
    }

    public ImageModel getImageToUser(Principal principal) throws DataFormatException {
        User user = getUserByPrincipal(principal);
        ImageModel imageModel = imageRepository
                .findByUserId(user.getId())
                .orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(deCompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    public ImageModel getImageToPost(Long postId) throws DataFormatException {
        ImageModel imageModel = imageRepository
                .findByPostId(postId)
                .orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(deCompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    private byte[] compressBytes(byte[] data) {
        var deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        try (var byteArrayOutputStream = new ByteArrayOutputStream(data.length)) {
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                byteArrayOutputStream.write(buffer, 0, count);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            LOG.error("Cannot compress bytes");
            throw new RuntimeException(e.getMessage());
        }
    }

    private byte[] deCompressBytes(byte[] data) throws DataFormatException {
        var inflater = new Inflater();
        inflater.setInput(data);
        byte[] buffer = new byte[1024];
        try(var outputStream = new ByteArrayOutputStream(data.length)) {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer,0,count);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            LOG.error("Cannot decompress bytes");
            throw new RuntimeException(e);
        }
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }
}
