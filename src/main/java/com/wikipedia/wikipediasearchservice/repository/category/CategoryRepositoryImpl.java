package com.wikipedia.wikipediasearchservice.repository.category;

import com.wikipedia.wikipediasearchservice.model.Article;
import com.wikipedia.wikipediasearchservice.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
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

    @Override
    public List<Category> getOrCreateCategory(Set<String> categoryName) {

        Query query = new Query();
        Criteria categoryNameInList = Criteria.where("name").in(categoryName);
        query.addCriteria(categoryNameInList);

        List<Category> result = new LinkedList<>();

        Set<String> categoryNamesPersisted = mongoTemplate.find(query, Category.class)
                .stream()
                .peek(result::add)
                .map(Category::getName)
                .collect(Collectors.toSet());

        List<Category> categoryOnPersist = categoryName.stream()
                .filter(name -> !categoryNamesPersisted.contains(name))
                .map(name -> Category.builder().name(name).build()
                ).toList();

        result.addAll(mongoTemplate.insertAll(categoryOnPersist));

        return result;
    }
}
