package com.wikipedia.wikipediasearchservice.dto.article;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ArticleUpdate {

    private String title;
    private List<String> auxiliaryText;
    private String wiki;
    private Set<String> categories;

}
