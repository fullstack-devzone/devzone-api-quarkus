package com.sivalabs.devzone.links.repositories;

import com.sivalabs.devzone.links.entities.Link;
import com.sivalabs.devzone.links.mappers.LinkMapper;
import com.sivalabs.devzone.links.models.LinkDTO;
import com.sivalabs.devzone.links.models.LinksDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class LinkRepository implements PanacheRepository<Link> {
    private static final int PAGE_SIZE = 10;
    private final LinkMapper linkMapper;
    private final EntityManager em;

    public LinksDTO getAllLinks(int page) {
        TypedQuery<Long> queryForIds = em.createQuery("select l.id from Link l order by l.createdAt desc", Long.class);
        queryForIds.setFirstResult((page-1) * PAGE_SIZE);
        queryForIds.setMaxResults(PAGE_SIZE);
        List<Long> ids = queryForIds.getResultList();

        TypedQuery<Long> queryTotal = em.createQuery("Select count(l.id) from Link l", Long.class);
        return getLinksDTO(page, ids, queryTotal);
    }

    public LinksDTO searchByTitle(String query, int page) {
        TypedQuery<Long> queryForIds = em.createQuery("select l.id from Link l where lower(l.title) like lower(concat('%', :query,'%')) order by l.createdAt desc", Long.class);
        queryForIds.setParameter("query", query);
        queryForIds.setFirstResult((page-1) * PAGE_SIZE);
        queryForIds.setMaxResults(PAGE_SIZE);
        List<Long> ids = queryForIds.getResultList();

        TypedQuery<Long> queryTotal = em.createQuery("Select count(l.id) from Link l where lower(l.title) like lower(concat('%', :query,'%'))", Long.class);
        queryTotal.setParameter("query", query);
        return getLinksDTO(page, ids, queryTotal);
    }

    public LinksDTO getLinksByTag(String tag, int page) {
        TypedQuery<Long> queryForIds = em.createQuery("select l.id from Link l LEFT JOIN l.tags t where t.name=:tag order by l.createdAt desc ", Long.class);
        queryForIds.setParameter("tag", tag);
        queryForIds.setFirstResult((page-1) * PAGE_SIZE);
        queryForIds.setMaxResults(PAGE_SIZE);
        List<Long> ids = queryForIds.getResultList();

        TypedQuery<Long> queryTotal = em.createQuery("Select count(l.id) from Link l LEFT JOIN l.tags t where t.name=:tag", Long.class);
        queryTotal.setParameter("tag", tag);
        return getLinksDTO(page, ids, queryTotal);
    }

    public Optional<LinkDTO> getLinkById(Long id) {
        return findByIdOptional(id).map(linkMapper::toDTO);
    }

    private LinksDTO getLinksDTO(int page, List<Long> ids, TypedQuery<Long> queryTotal) {
        long totalElements = queryTotal.getSingleResult();
        int totalPages = (int) ((totalElements / PAGE_SIZE) + 1);

        TypedQuery<Link> dataQuery = em.createQuery("select l from Link l where l.id in :ids order by l.createdAt desc", Link.class);
        dataQuery.setParameter("ids", ids);
        var linkDTOS = dataQuery.getResultList().stream().map(linkMapper::toDTO).toList();

        return new LinksDTO(linkDTOS, totalElements, totalPages, page);
    }



}
