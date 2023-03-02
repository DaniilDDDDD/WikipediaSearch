package com.wikipedia.wikipediasearchservice.model;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Document("article")
public class Article {

    public Article() {}

    @Id
    private String id;

    @DocumentReference
    private List<Category> categories;

    @Indexed(unique = true)
    @NotNull
    @NotBlank
    private String title;

    private String wiki;

    private Date timestamp;

    private Date createTimestamp;

    private List<String> auxiliaryText;

    @NotNull
    @NotBlank
    private String language;

}
