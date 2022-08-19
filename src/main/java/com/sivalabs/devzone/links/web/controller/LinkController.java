package com.sivalabs.devzone.links.web.controller;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.models.LinkDTO;
import com.sivalabs.devzone.links.models.LinksDTO;
import com.sivalabs.devzone.links.services.LinkService;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Objects;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/links")
@RequiredArgsConstructor
@Slf4j
public class LinkController {
    private final LinkService linkService;
    private final UserService userService;
    private final JsonWebToken jwt;

    @GET
    @PermitAll
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
    @PermitAll
    public LinkDTO getLink(@PathParam("id") Long id) {
        return linkService
                .getLinkById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Link with id: " + id + " not found"));
    }

    @POST
    @RolesAllowed({ "ROLE_USER", "ROLE_ADMIN" })
    public Response createLink(@Valid LinkDTO link, @Context UriInfo uriInfo,
                               @Context SecurityContext ctx) {
        String email = jwt.getClaim("email");
        User user = userService.getUserByEmail(email).orElseThrow();
        link.setCreatedUserId(user.getId());
        LinkDTO linkDTO = linkService.createLink(link);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(linkDTO.getId())).build();
        return Response.created(uri).entity(linkDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ "ROLE_USER", "ROLE_ADMIN" })
    public Response deleteLink(@PathParam("id") Long id,
                               @Context SecurityContext ctx) {
        LinkDTO link = linkService.getLinkById(id).orElseThrow();
        String email = jwt.getClaim("email");
        User user = userService.getUserByEmail(email).orElseThrow();
        if(ctx.isUserInRole("ROLE_ADMIN") || Objects.equals(link.getCreatedUserId(), user.getId())) {
            linkService.deleteLink(link.getId());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
