package com.giavarone.mongodbsoftdelete.mongo;


import com.giavarone.mongodbsoftdelete.client.CustomMongoTemplate;
import com.giavarone.mongodbsoftdelete.model.Example;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SoftDeleteIntegrationTest extends MongoDBContainerTest {
    @Autowired
    private CustomMongoTemplate customMongoTemplate;

    @AfterEach
    void dropCollection() {
        customMongoTemplate.dropCollection(Example.class);
    }

    @Test
    void testFindWhenRetrievingObjectsReturnsObjectsNotDeleted() {
        Example example1 = Example.builder()
            .id(1L)
            .aString("Test 1")
            .build();

        Example example2 = Example.builder()
            .id(2L)
            .aString("Test 2")
            .build();

        Example persistedExample1 = customMongoTemplate.save(example1);
        Example persistedExample2 = customMongoTemplate.save(example2);

        persistedExample1.setDeletedAt(LocalDateTime.now());
        customMongoTemplate.save(persistedExample1);


        List<Example> examples = customMongoTemplate.find(new Query(), Example.class, "examples");

        assertFalse(examples.isEmpty(), "Examples should not be empty");
        assertEquals(1, examples.size(), "Examples size should be 1");
        assertTrue(examples.contains(persistedExample2), "Examples should contain persistedExample2");
        assertFalse(examples.contains(persistedExample1), "Examples should not contain persistedExample1");
    }

    @Test
    void testFindByIdWhenValidObjectIdProvidedReturnsObject() {
        Example example = Example.builder()
            .id(1L)
            .aString("Test 1")
            .build();

        customMongoTemplate.save(example);

        Example exampleFound = customMongoTemplate.findById(1L, Example.class, "examples");

        assertNotNull(exampleFound, "Example found should not be null");
        assertEquals(example.getId(), exampleFound.getId(), "Example found id should be equal to example id");
    }

    @Test
    void testFindByIdWhenDeletedObjectIdProvidedReturnsNullObject() {
        Example example = Example.builder()
            .id(1L)
            .aString("Test 1")
            .build();

        Example persistedExample = customMongoTemplate.save(example);

        persistedExample.setDeletedAt(LocalDateTime.now());
        customMongoTemplate.save(persistedExample);

        Example deletedExample = customMongoTemplate.findById(1L, Example.class, "examples");

        assertNull(deletedExample, "Deleted example should be null");
    }

    @Test
    void testFindOneWhenValidObjectIdProvidedReturnsObject() {
        Example example = Example.builder()
            .id(1L)
            .aString("Test 1")
            .build();

        customMongoTemplate.save(example);

        Example exampleFound = customMongoTemplate.findOne(new Query(), Example.class, "examples");

        assertNotNull(exampleFound, "Example found should not be null");
        assertEquals(example.getId(), exampleFound.getId(), "Example found id should be equal to example id");
    }

    @Test
    void testFindOneWhenDeletedObjectIdProvidedReturnsNullObject() {
        Example example = Example.builder()
            .id(1L)
            .aString("Test 1")
            .build();

        Example persistedExample = customMongoTemplate.save(example);

        persistedExample.setDeletedAt(LocalDateTime.now());
        customMongoTemplate.save(persistedExample);

        Example deletedExample = customMongoTemplate.findOne(new Query(), Example.class, "examples");

        assertNull(deletedExample, "Deleted example should be null");
    }

    @Test
    void testExistsWhenValidObjectIdProvidedReturnsTrue() {
        Example example = Example.builder()
            .id(1L)
            .aString("Test 1")
            .build();

        customMongoTemplate.save(example);

        Boolean exampleExists = customMongoTemplate.exists(new Query(), Example.class, "examples");

        assertTrue(exampleExists, "Example should exist");
    }

    @Test
    void testExistsWhenDeletedObjectIdProvidedReturnsFalse() {
        Example example = Example.builder()
            .id(1L)
            .aString("Test 1")
            .build();

        Example persistedExample = customMongoTemplate.save(example);

        persistedExample.setDeletedAt(LocalDateTime.now());
        customMongoTemplate.save(persistedExample);

        Boolean exampleExists = customMongoTemplate.exists(new Query(), Example.class, "examples");

        assertFalse(exampleExists, "Example should not exist");
    }

    @Test
    void testCountWhenNotAllObjectsAreDeletedReturnsExamples() {
        Example example1 = Example.builder()
            .id(1L)
            .aString("Test 1")
            .build();

        Example example2 = Example.builder()
            .id(2L)
            .aString("Test 2")
            .build();

        Example persistedExample1 = customMongoTemplate.save(example1);
        customMongoTemplate.save(example2);


        persistedExample1.setDeletedAt(LocalDateTime.now());
        customMongoTemplate.save(persistedExample1);

        Long examplesCount = customMongoTemplate.count(new Query(), "examples");

        assertEquals(1, examplesCount, "Examples count should be 1");
    }

    @Test
    void testCountWhenAllObjectsAreDeletedReturnsZero() {
        Example example = Example.builder()
            .id(1L)
            .aString("Test 1")
            .build();

        Example persistedExample = customMongoTemplate.save(example);

        persistedExample.setDeletedAt(LocalDateTime.now());
        customMongoTemplate.save(persistedExample);

        Long examplesCount = customMongoTemplate.count(new Query(), "examples");

        assertEquals(0, examplesCount, "examples count should be 0");
    }
}