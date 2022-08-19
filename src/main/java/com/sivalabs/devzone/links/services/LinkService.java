package com.sivalabs.devzone.links.services;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.entities.Link;
import com.sivalabs.devzone.links.entities.Tag;
import com.sivalabs.devzone.links.mappers.LinkMapper;
import com.sivalabs.devzone.links.models.LinkDTO;
import com.sivalabs.devzone.links.models.LinksDTO;
import com.sivalabs.devzone.links.repositories.LinkRepository;
import com.sivalabs.devzone.links.repositories.TagRepository;
import com.sivalabs.devzone.users.repositories.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinkService {
    private static final int PAGE_SIZE = 10;

    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final LinkMapper linkMapper;

    public LinksDTO getAllLinks(int page) {
        return linkRepository.getAllLinks(page);
    }

    public LinksDTO searchLinks(String query, int page) {
        return linkRepository.searchByTitle(query, page);
    }

    public LinksDTO getLinksByTag(String tag, int page) {
        return linkRepository.getLinksByTag(tag, page);
    }

    public Optional<LinkDTO> getLinkById(Long id) {
        return linkRepository.getLinkById(id);
    }

    public LinkDTO createLink(LinkDTO link) {
        Link entity = new Link();
        entity.setTitle(link.getTitle());
        entity.setUrl(link.getUrl());
        entity.setCreatedBy(userRepository.findById(link.getCreatedUserId()));
        Set<Tag> tagsList = new HashSet<>();
        link.getTags()
                .forEach(
                        tagName -> {
                            if (!tagName.trim().isEmpty()) {
                                Tag tag = createTagIfNotExist(tagName.trim());
                                tagsList.add(tag);
                            }
                        });
        entity.setTags(tagsList);

        linkRepository.persist(entity);
        link.setId(entity.getId());
        return link;
    }

    public void deleteLink(Long id) {

    }

    public void deleteAllLinks() {
        return;
    }

    public List<Tag> findAllTags() {
        Sort sort = Sort.ascending("name");
        return tagRepository.listAll(sort);
    }

    private Tag createTagIfNotExist(String tagName) {
        Optional<Tag> tagOptional = tagRepository.findByName(tagName);
        if (tagOptional.isPresent()) {
            return tagOptional.get();
        }
        Tag tag = new Tag();
        tag.setName(tagName);
        tagRepository.persist(tag);
        return tag;
    }
}
