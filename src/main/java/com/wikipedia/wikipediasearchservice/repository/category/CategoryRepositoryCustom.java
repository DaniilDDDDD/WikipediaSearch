package com.wikipedia.wikipediasearchservice.repository.category;


import com.wikipedia.wikipediasearchservice.model.Article;
import com.wikipedia.wikipediasearchservice.model.Category;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CategoryRepositoryCustom {

    Map<String, Category> findAddCategoriesNames();

    List<Category> getOrCreateCategory(Set<String> categoryName);

}
