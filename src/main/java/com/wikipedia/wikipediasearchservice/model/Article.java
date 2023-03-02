package com.wikipedia.wikipediasearchservice.model;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("article")
public class Article {

    @Id
    private String id;

    @DocumentReference
    private List<Category> categories;

    private String title;

    private String wiki;

    private Date timestamp;

    private Date createTimestamp;

    private List<String> auxiliaryText;

    private String language;

}
