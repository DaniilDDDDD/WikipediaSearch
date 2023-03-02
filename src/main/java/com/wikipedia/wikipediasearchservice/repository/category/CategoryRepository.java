package com.wikipedia.wikipediasearchservice.repository.category;

import com.wikipedia.wikipediasearchservice.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends MongoRepository<Category, String>, CategoryRepositoryCustom {

    List<Category> findAllByNameIn(Set<String> names);

}
