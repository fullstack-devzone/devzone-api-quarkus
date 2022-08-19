package com.sivalabs.devzone.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.links.services.LinksImportService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class AppLifecycleBean {
    private final LinksImportService linksImportService;

    void onStart(@Observes StartupEvent ev) throws Exception {
        log.info("The application is starting...");
        linksImportService.importLinks("/data/links.csv");
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("The application is stopping...");
    }

}
