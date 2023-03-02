package com.wikipedia.wikipediasearchservice.dto.article;

import com.wikipedia.wikipediasearchservice.model.Article;
import com.wikipedia.wikipediasearchservice.model.Category;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ArticleRetrieve {

    private String id;
    private List<String> categories;
    private String title;
    private String wiki;
    private Date timestamp;
    private Date createTimestamp;
    private List<String> auxiliaryText;
    private String language;


    public static ArticleRetrieve parseArticle(Article article) {

        return ArticleRetrieve.builder()
                .id(article.getId())
                .title(article.getTitle())
                .wiki(article.getWiki())
                .timestamp(article.getTimestamp())
                .createTimestamp(article.getCreateTimestamp())
                .auxiliaryText(article.getAuxiliaryText())
                .categories(article.getCategories().stream().map(Category::getName).toList())
                .build();

    }
}
