package com.wikipedia.wikipediasearchservice.repository.category;

import com.wikipedia.wikipediasearchservice.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CategoryRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Map<String, Category> findAddCategoriesNames() {
        Query query = new Query();
        List<Category> results = mongoTemplate.find(query, Category.class);
        return results.stream().collect(Collectors.toMap(Category::getName, category -> category));
    }
}
