package com.sivalabs.devzone.posts.repositories;

import com.sivalabs.devzone.posts.entities.Post;
import com.sivalabs.devzone.posts.mappers.PostMapper;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.models.PostsDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PostRepository implements PanacheRepository<Post> {
    private static final int PAGE_SIZE = 10;
    private final PostMapper postMapper;
    private final EntityManager em;

    public PostsDTO getAllPosts(int page) {
        TypedQuery<Long> queryForIds = em.createQuery("select l.id from Post l order by l.createdAt desc", Long.class);
        queryForIds.setFirstResult((page - 1) * PAGE_SIZE);
        queryForIds.setMaxResults(PAGE_SIZE);
        List<Long> ids = queryForIds.getResultList();

        TypedQuery<Long> queryTotal = em.createQuery("Select count(l.id) from Post l", Long.class);
        return getPostsDTO(page, ids, queryTotal);
    }

    public PostsDTO searchByTitle(String query, int page) {
        TypedQuery<Long> queryForIds = em.createQuery(
                "select l.id from Post l where lower(l.title) like lower(concat('%', :query,'%')) order by l.createdAt desc",
                Long.class);
        queryForIds.setParameter("query", query);
        queryForIds.setFirstResult((page - 1) * PAGE_SIZE);
        queryForIds.setMaxResults(PAGE_SIZE);
        List<Long> ids = queryForIds.getResultList();

        TypedQuery<Long> queryTotal = em.createQuery(
                "Select count(l.id) from Post l where lower(l.title) like lower(concat('%', :query,'%'))", Long.class);
        queryTotal.setParameter("query", query);
        return getPostsDTO(page, ids, queryTotal);
    }

    public Optional<PostDTO> getPostById(Long id) {
        return findByIdOptional(id).map(postMapper::toDTO);
    }

    private PostsDTO getPostsDTO(int page, List<Long> ids, TypedQuery<Long> queryTotal) {
        long totalElements = queryTotal.getSingleResult();
        int totalPages = (int) ((totalElements / PAGE_SIZE) + 1);

        TypedQuery<Post> dataQuery =
                em.createQuery("select l from Post l where l.id in :ids order by l.createdAt desc", Post.class);
        dataQuery.setParameter("ids", ids);
        var postDTOS = dataQuery.getResultList().stream().map(postMapper::toDTO).toList();

        return new PostsDTO(postDTOS, totalElements, totalPages, page);
    }
}
