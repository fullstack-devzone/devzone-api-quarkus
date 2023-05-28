package com.sivalabs.devzone.posts;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.posts.services.PostsImportService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class PostsDataInitializer {
    private final PostsImportService postsImportService;
    private final ApplicationProperties properties;

    void onStart(@Observes StartupEvent ev) throws Exception {
        log.info("The application is starting...");
        if (properties.postDataInitEnabled()) {
            log.info("Initializing posts data");
            postsImportService.importPosts(properties.postsInitDataFile());
        } else {
            log.info("Skipping posts data initialization");
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("The application is stopping...");
    }
}
