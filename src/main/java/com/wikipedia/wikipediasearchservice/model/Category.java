package com.wikipedia.wikipediasearchservice.model;


import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("category")
public class Category {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    @NotNull
    private String name;

    @DocumentReference
    private List<Article> users;

}
