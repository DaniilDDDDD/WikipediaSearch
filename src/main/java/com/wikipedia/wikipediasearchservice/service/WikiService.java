package com.wikipedia.wikipediasearchservice.service;

import com.wikipedia.wikipediasearchservice.dto.EntityCount;
import com.wikipedia.wikipediasearchservice.dto.article.ArticleUpdate;
import com.wikipedia.wikipediasearchservice.model.Article;
import com.wikipedia.wikipediasearchservice.repository.CategoryRepository;
import com.wikipedia.wikipediasearchservice.repository.article.ArticleRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WikiService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public WikiService(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }


    public List<Article> findAllByTitle(String title) {
        return articleRepository.findAllByTitle(title);
    }


    public List<EntityCount> countArticlesInAllCategories() {
        return articleRepository.countArticlesInAllCategories();
    }


    public Article update(String id, ArticleUpdate articleUpdate) throws EntityNotFoundException {

        Optional<Article> articleData = articleRepository.findById(id);
        if (articleData.isEmpty())
            throw new EntityNotFoundException("No article with provided id not found!");

        Article article = articleData.get();

        article.setTitle(articleUpdate.getTitle());
        article.setAuxiliaryText(articleUpdate.getAuxiliaryText());
        article.setCategories(categoryRepository.findAllByNameIn(articleUpdate.getCategories()));
        article.setWiki(articleUpdate.getWiki());
        article.setTimestamp(new Date());

        return articleRepository.save(article);

    }


    public void refresh(Boolean local) throws IOException {
        if (local) {

            Reader reader = Files.newBufferedReader(
                    Paths.get("src/main/resources/initialData.json"));



        }
    }
}
