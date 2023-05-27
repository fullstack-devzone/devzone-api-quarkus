package com.sivalabs.devzone.config;

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
public class AppLifecycleBean {
    private final PostsImportService postsImportService;

    void onStart(@Observes StartupEvent ev) throws Exception {
        log.info("The application is starting...");
        postsImportService.importPosts("/data/posts.csv");
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("The application is stopping...");
    }
}
