package com.sivalabs.devzone.links.web.controller;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.entities.Link;
import com.sivalabs.devzone.links.models.LinkDTO;
import com.sivalabs.devzone.links.models.LinksDTO;
import com.sivalabs.devzone.links.services.LinkService;
import com.sivalabs.devzone.users.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/links")
@RequiredArgsConstructor
@Slf4j
public class LinkController {
    private final LinkService linkService;

    @GET
    public LinksDTO findAllLinks(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("query") @DefaultValue("") String query,
            @QueryParam("tag") @DefaultValue("") String tag
    ) {
        if (StringUtils.isNotEmpty(query)) {
            log.info("Searching links for {} with page: {}", query, page);
            return linkService.searchLinks(query, page);
        } else if (StringUtils.isNotEmpty(tag)) {
            log.info("Fetching links for tag {} with page: {}", tag, page);
            return linkService.getLinksByTag(tag, page);
        } else {
            log.info("Fetching links with page: {}", page);
            return linkService.getAllLinks(page);
        }
    }

    @GET
    @Path("/{id}")
    public LinkDTO getLink(@PathParam("id") Long id) {
        return linkService
                .getLinkById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Link with id: " + id + " not found"));
    }

    @POST
    public Response createLink(@Valid LinkDTO link, @Context UriInfo uriInfo) {
        link.setCreatedUserId(1L);
        LinkDTO linkDTO = linkService.createLink(link);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(linkDTO.getId())).build();
        return Response.created(uri).entity(linkDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLink(@PathParam("id") Long id) {
        LinkDTO link = linkService.getLinkById(id).orElseThrow();
        //this.checkPrivilege(id, link, loginUser);
        linkService.deleteLink(link.getId());
        return Response.noContent().build();
    }
}
