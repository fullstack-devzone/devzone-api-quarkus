package com.sivalabs.devzone.posts.services;

import com.sivalabs.devzone.common.models.PagedResult;
import com.sivalabs.devzone.posts.entities.Post;
import com.sivalabs.devzone.posts.models.CreatePostRequest;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.repositories.PostRepository;
import com.sivalabs.devzone.users.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PagedResult<PostDTO> getAllPosts(int page) {
        return postRepository.getAllPosts(page);
    }

    public PagedResult<PostDTO> searchPosts(String query, int page) {
        return postRepository.searchByTitle(query, page);
    }

    public Optional<PostDTO> getPostById(Long id) {
        return postRepository.getPostById(id);
    }

    public PostDTO createPost(CreatePostRequest createPostRequest) {
        Post entity = new Post();
        entity.setTitle(createPostRequest.getTitle());
        entity.setUrl(createPostRequest.getUrl());
        entity.setContent(createPostRequest.getContent());
        entity.setCreatedBy(userRepository.findById(createPostRequest.getUserId()));

        postRepository.persist(entity);

        return PostDTO.from(entity);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public void deleteAllPosts() {
        postRepository.deleteAll();
    }
}
