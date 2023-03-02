package com.wikipedia.wikipediasearchservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikipedia.wikipediasearchservice.dto.EntityCount;
import com.wikipedia.wikipediasearchservice.dto.article.ArticleRetrieve;
import com.wikipedia.wikipediasearchservice.dto.article.ArticleUpdate;
import com.wikipedia.wikipediasearchservice.model.Article;
import com.wikipedia.wikipediasearchservice.service.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/wiki")
public class WikiController {

    private final WikiService wikiService;
    private final ObjectMapper objectMapper;

    @Autowired
    public WikiController(WikiService wikiService, ObjectMapper objectMapper) {
        this.wikiService = wikiService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{title}")
    public ResponseEntity<String> list(
            @PathParam(value = "title")
                    String title,
            @RequestParam(value = "pretty", required = false, defaultValue = "false")
                    Boolean pretty
    ) throws IOException {
        List<Article> articles = wikiService.findAllByTitle(title);

        String response = pretty ? objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(articles) :
                articles.stream().map(ArticleRetrieve::parseArticle).toList().toString();

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/stats")
    public ResponseEntity<List<EntityCount>> count() {
        return ResponseEntity.ok(wikiService.countArticlesInAllCategories());
    }


    @PutMapping("/{id}")
    public ResponseEntity<ArticleRetrieve> update(
            @PathParam(value = "id")
            String id,
            @RequestBody
            @Valid
            ArticleUpdate articleUpdate
    ) throws EntityNotFoundException {
        return ResponseEntity.ok(
                ArticleRetrieve.parseArticle(wikiService.update(id, articleUpdate))
        );
    }


    @GetMapping("/refresh")
    public ResponseEntity<String> refresh() {
        wikiService.refresh(false);
        return ResponseEntity.ok("Database refreshed with new data!");
    }

}
