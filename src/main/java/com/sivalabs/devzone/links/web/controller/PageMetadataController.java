package com.sivalabs.devzone.links.web.controller;

import com.sivalabs.devzone.links.entities.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api")
@RequiredArgsConstructor
@Slf4j
public class PageMetadataController {

    @GET
    @Path("/page-metadata")
    public Map<String, String> getPageMetadata(@QueryParam("url") String url) {
        Map<String, String> metadata = new ConcurrentHashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            metadata.put("title", doc.title());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            metadata.put("title", url);
        }
        return metadata;
    }
}
