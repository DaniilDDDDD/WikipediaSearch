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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Service
public class WikiService {

    @Value("${initialDataPath}")
    private String initialDataPath;

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public WikiService(
            ArticleRepository articleRepository,
            CategoryRepository categoryRepository,
            ObjectMapper objectMapper
    ) {
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

//        System.out.println(article);

        List<Category> updatedCategories = article.getCategories()
                .stream()
//                .peek(System.out::println)
                .peek(category -> category.getArticles().remove(article))
                .collect(Collectors.toCollection(LinkedList::new));

        List<Category> categories = categoryRepository.findAllByNameIn(articleUpdate.getCategory());
//        System.out.println(categories);
        categories = categories.stream().peek(category -> category.getArticles().add(article)).toList();
        updatedCategories.addAll(categories);

        article.setTitle(articleUpdate.getTitle());
        article.setAuxiliaryText(articleUpdate.getAuxiliary_text());
        article.setCategories(categories);
        article.setWiki(articleUpdate.getWiki());
        article.setTimestamp(new Date());

        categoryRepository.saveAll(updatedCategories);
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

            File file = new File(initialDataPath);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));

            out.write("[");

            String readed = in.readLine();
            if (readed != null)
                out.write(readed);
            while ((readed = in.readLine()) != null)
                out.write(",\n" + readed);

            out.write("\n]");
            in.close();
            out.close();
        }

        File file = new File(initialDataPath);
        List<ArticleData> articleData = objectMapper.readValue(file, new TypeReference<>() {
        });

        Map<String, Category> categoriesPersisted = categoryRepository.findAddCategoriesNames();

        Map<String, Category> categoriesOnPersist = new HashMap<>();

        List<Article> articles = articleData.stream()
                .map(
                        data -> {

                            Article article = Article.builder()
                                    .title(data.getTitle())
                                    .wiki(data.getWiki())
                                    .timestamp(data.getTimestamp())
                                    .createTimestamp(data.getCreate_timestamp())
                                    .auxiliaryText(data.getAuxiliary_text())
                                    .language(data.getLanguage())
                                    .build();

                            List<Category> categories = data.getCategory().stream().map(
                                    categoryName -> {
                                        Category category = categoriesPersisted.get(categoryName);
                                        if (category != null) {
                                            category.getArticles().add(article);
                                            categoriesOnPersist.put(categoryName, category);
                                            return category;
                                        }
                                        List<Article> currentArticles = new LinkedList<>();
                                        currentArticles.add(article);
                                        category = Category.builder().name(categoryName).articles(currentArticles).build();
                                        categoriesOnPersist.put(categoryName, category);
                                        categoriesPersisted.put(categoryName, category);
                                        return category;
                                    }
                            ).toList();

                            article.setCategories(categories);
                            return article;

                        }
                ).toList();

        categoryRepository.saveAll(categoriesOnPersist.values());
        articleRepository.saveAll(articles);
        categoryRepository.saveAll(categoriesOnPersist.values());
    }
}
