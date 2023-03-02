package com.wikipedia.wikipediasearchservice.dto.article;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleData {

    private List<String> category;
    private String title;
    private String wiki;
    private Date timestamp;
    private Date create_timestamp;
    private List<String> auxiliary_text;
    private String language;

}
