package com.sivalabs.devzone.posts.repositories;

import com.sivalabs.devzone.common.models.PagedResult;
import com.sivalabs.devzone.posts.entities.Post;
import com.sivalabs.devzone.posts.models.PostDTO;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PostRepository implements PanacheRepository<Post> {
    private static final int PAGE_SIZE = 10;

    public Optional<PostDTO> getPostById(Long id) {
        return findByIdOptional(id).map(PostDTO::from);
    }

    public PagedResult<PostDTO> getAllPosts(int page) {
        PanacheQuery<Post> query = findAll(Sort.descending("createdAt"));
        return getPostDTOPagedResult(page, query);
    }

    public PagedResult<PostDTO> searchByTitle(String keyword, int page) {
        PanacheQuery<Post> query =
                find("lower(title) like lower(concat('%', ?1,'%'))", Sort.descending("createdAt"), keyword);
        return getPostDTOPagedResult(page, query);
    }

    private PagedResult<PostDTO> getPostDTOPagedResult(int page, PanacheQuery<Post> query) {
        List<PostDTO> posts = query.page(Page.of(page, PAGE_SIZE)).list().stream()
                .map(PostDTO::from)
                .toList();
        int pageCount = query.page(Page.of(page, PAGE_SIZE)).pageCount();
        long totalElements = query.count();
        return new PagedResult<>(posts, totalElements, page, pageCount);
    }
}
