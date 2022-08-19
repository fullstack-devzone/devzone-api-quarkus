package com.sivalabs.devzone.links.web.controller;

import com.sivalabs.devzone.links.entities.Tag;
import com.sivalabs.devzone.links.services.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/tags")
@RequiredArgsConstructor
@Slf4j
public class TagController {
    private final LinkService linkService;

    @GET
    @PermitAll
    public List<Tag> allTags() {
        return linkService.findAllTags();
    }
}
