package com.wikipedia.wikipediasearchservice.repository.article;

import com.wikipedia.wikipediasearchservice.dto.EntityCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ArticleRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<EntityCount> countArticlesInAllCategories() {
        GroupOperation groupByCategories = Aggregation
                .group("name")
                .sum(ArrayOperators.Size.lengthOfArray("articles"))
                .as("amount");

        Aggregation aggregation = Aggregation.newAggregation(
                groupByCategories
        );

        AggregationResults<EntityCount> results = mongoTemplate
                .aggregate(aggregation, "category", EntityCount.class);

        return results.getMappedResults();
    }


}
