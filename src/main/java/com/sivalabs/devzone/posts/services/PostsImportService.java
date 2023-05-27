package com.sivalabs.devzone.posts.services;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.users.services.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostsImportService {
    public static final String SYSTEM_USER_EMAIL = "admin@gmail.com";
    private final PostService postService;
    private final UserService userService;

    public void importPosts(String fileName) throws IOException, CsvValidationException {
        log.info("Importing posts from file: {}", fileName);
        long count = 0L;
        InputStream inputStream = this.getClass().getResourceAsStream(fileName);

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                CSVReader csvReader = new CSVReader(inputStreamReader)) {
            csvReader.skip(1);
            CSVIterator iterator = new CSVIterator(csvReader);

            while (iterator.hasNext()) {
                String[] nextLine = iterator.next();
                PostDTO postDTO = new PostDTO();
                postDTO.setUrl(nextLine[0]);
                postDTO.setTitle(nextLine[1]);
                postDTO.setContent(nextLine[1]);
                postDTO.setCreatedUserId(userService
                        .getUserByEmail(SYSTEM_USER_EMAIL)
                        .orElseThrow()
                        .getId());
                postDTO.setCreatedAt(Instant.now());

                postService.createPost(postDTO);
                count++;
            }
        }
        log.info("Imported {} posts from file {}", count, fileName);
    }
}
