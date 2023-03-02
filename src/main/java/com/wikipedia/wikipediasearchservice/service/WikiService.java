package com.wikipedia.wikipediasearchservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikipedia.wikipediasearchservice.dto.EntityCount;
import com.wikipedia.wikipediasearchservice.dto.article.ArticleData;
import com.wikipedia.wikipediasearchservice.dto.article.ArticleUpdate;
import com.wikipedia.wikipediasearchservice.model.Article;
import com.wikipedia.wikipediasearchservice.model.Category;
import com.wikipedia.wikipediasearchservice.repository.article.ArticleRepository;
import com.wikipedia.wikipediasearchservice.repository.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;

@Service
public class WikiService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public WikiService(ArticleRepository articleRepository, CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
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


    public void refresh(String dataLink) throws IOException {
        if (dataLink != null) {
            URL url = new URL(dataLink);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            System.setProperty("http.keepAlive", "false");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
            InputStreamReader reader = new InputStreamReader(gzipInputStream);
            BufferedReader in = new BufferedReader(reader);

            File file = new File("src/main/resources/data.json");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));

            out.write("[");
            String readed;
            while ((readed = in.readLine()) != null)
                out.write(readed + ",\n");

            out.write("]");
            in.close();
            out.close();
        }

        File file = new File("src/main/resources/data.json");
        List<ArticleData> articleData = objectMapper.readValue(file, new TypeReference<>() {
        });

        Map<String, Category> categoriesPersisted = categoryRepository.findAddCategoriesNames();

        List<Category> categoriesOnPersist = new LinkedList<>();

        List<Article> articles = articleData.stream()
                .map(
                        article -> Article.builder()
                                .title(article.getTitle())
                                .wiki(article.getWiki())
                                .timestamp(article.getTimestamp())
                                .createTimestamp(article.getCreate_timestamp())
                                .auxiliaryText(article.getAuxiliary_text())
                                .language(article.getLanguage())
                                .categories(article.getCategory().stream().map(category -> {
                                    Category categoryEntity = Category.builder().name(category).build();
                                    if (!categoriesPersisted.containsKey(category)) {
                                        categoriesOnPersist.add(categoryEntity);
                                        categoriesPersisted.put(category, categoryEntity);
                                    }
                                    return categoryEntity;
                                }).toList())
                                .build()
                ).toList();

        categoryRepository.saveAll(categoriesOnPersist);
        articleRepository.saveAll(articles);
    }
}
