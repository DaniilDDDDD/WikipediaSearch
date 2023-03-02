package com.wikipedia.wikipediasearchservice.repository.article;

import com.wikipedia.wikipediasearchservice.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, String>, ArticleRepositoryCustom{

    List<Article> findAllByTitle(String title);

}
