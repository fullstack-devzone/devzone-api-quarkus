package com.sivalabs.devzone.links.repositories;

import com.sivalabs.devzone.links.entities.Tag;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TagRepository implements PanacheRepository<Tag> {

    public Optional<Tag> findByName(String tag) {
        return find("name", tag).firstResultOptional();
    }
}
