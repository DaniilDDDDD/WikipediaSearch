package com.wikipedia.wikipediasearchservice.repository.category;


import com.wikipedia.wikipediasearchservice.model.Category;

import java.util.Map;
import java.util.Set;

public interface CategoryRepositoryCustom {

    Map<String, Category> findAddCategoriesNames();

}
