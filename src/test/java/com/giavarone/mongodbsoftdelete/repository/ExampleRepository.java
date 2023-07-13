package com.giavarone.mongodbsoftdelete.repository;

import com.giavarone.mongodbsoftdelete.annotation.IncludeDeletedRecords;
import com.giavarone.mongodbsoftdelete.model.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExampleRepository extends MongoRepository<Example, Long> {

    List<Example> findByaString(String aString);

    @IncludeDeletedRecords
    List<Example> findByaNumber(Integer aNumber);
}
