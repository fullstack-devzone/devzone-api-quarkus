package com.sivalabs.devzone.posts.services;

import com.sivalabs.devzone.posts.entities.Post;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.models.PostsDTO;
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

    public PostsDTO getAllPosts(int page) {
        return postRepository.getAllPosts(page);
    }

    public PostsDTO searchPosts(String query, int page) {
        return postRepository.searchByTitle(query, page);
    }

    public Optional<PostDTO> getPostById(Long id) {
        return postRepository.getPostById(id);
    }

    public PostDTO createPost(PostDTO post) {
        Post entity = new Post();
        entity.setTitle(post.getTitle());
        entity.setUrl(post.getUrl());
        entity.setContent(post.getContent());
        entity.setCreatedBy(userRepository.findById(post.getCreatedUserId()));

        postRepository.persist(entity);
        post.setId(entity.getId());
        return post;
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
