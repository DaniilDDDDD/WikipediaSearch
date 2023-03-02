package com.wikipedia.wikipediasearchservice.dto.article;

import com.wikipedia.wikipediasearchservice.model.Article;
import com.wikipedia.wikipediasearchservice.model.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArticleRetrieve {

    private String id;
    private List<String> categories;
    private String title;
    private String wiki;
    private Long timestamp;
    private Long create_timestamp;
    private List<String> auxiliary_text;
    private String language;


    public static ArticleRetrieve parseArticle(Article article) {

        return ArticleRetrieve.builder()
                .id(article.getId())
                .title(article.getTitle())
                .wiki(article.getWiki())
                .timestamp(article.getTimestamp() != null ? article.getTimestamp().getTime() : null)
                .create_timestamp(article.getCreateTimestamp() != null ? article.getCreateTimestamp().getTime() : null)
                .auxiliary_text(article.getAuxiliaryText())
                .categories(article.getCategories().stream().map(Category::getName).toList())
                .build();

    }
}
