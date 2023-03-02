package com.wikipedia.wikipediasearchservice.dto.article;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleData {

    private List<String> category = List.of();
    private String title;
    private String wiki;
    private Date timestamp;
    private Date create_timestamp;
    private List<String> auxiliary_text = List.of();
    private String language;

}
