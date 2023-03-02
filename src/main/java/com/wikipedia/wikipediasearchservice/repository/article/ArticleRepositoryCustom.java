package com.wikipedia.wikipediasearchservice.repository.article;

import com.wikipedia.wikipediasearchservice.dto.EntityCount;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<EntityCount> countArticlesInAllCategories();

}
