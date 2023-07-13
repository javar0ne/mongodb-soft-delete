package com.giavarone.mongodbsoftdelete.repository;

import com.giavarone.mongodbsoftdelete.model.Example;
import com.giavarone.mongodbsoftdelete.mongo.MongoDBContainerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class ExampleRepositoryIntegrationTest extends MongoDBContainerTest {

    @Autowired
    private ExampleRepository exampleRepository;

    @AfterEach
    void dropCollection() {
        exampleRepository.deleteAll();
    }

    @Test
    void testFindByAStringWhenValidStringProvidedReturnsExamplesNotDeleted() {
        Example example1 = Example.builder()
            .id(1L)
            .aString("aString")
            .aNumber(5)
            .build();

        Example example2 = Example.builder()
            .id(2L)
            .aString("aString")
            .aNumber(10)
            .build();

        exampleRepository.saveAll(List.of(example1, example2));

        example1.setDeletedAt(LocalDateTime.now());
        example1 = exampleRepository.save(example1);

        List<Example> examples = exampleRepository.findByaString("aString");

        assertFalse(examples.isEmpty(), "Examples should not be empty");
        assertEquals(1, examples.size(), "Examples size should be 1");
        assertTrue(examples.contains(example2), "Examples should contain example2");
        assertFalse(examples.contains(example1), "Examples should not contain example1");
    }
    @Test
    void testFindByANumberWhenValidNumberProvidedReturnsExamples() {
        Example example1 = Example.builder()
            .id(1L)
            .aString("a string 1")
            .aNumber(5)
            .build();

        Example example2 = Example.builder()
            .id(2L)
            .aString("a string 2")
            .aNumber(5)
            .build();

        exampleRepository.saveAll(List.of(example1, example2));

        example1.setDeletedAt(LocalDateTime.now());
        example1 = exampleRepository.save(example1);

        List<Example> examples = exampleRepository.findByaNumber(5);

        assertFalse(examples.isEmpty(), "Examples should not be empty");
        assertEquals(2, examples.size(), "Examples size should be 2");
        assertTrue(examples.contains(example1), "Examples should contain example1");
        assertTrue(examples.contains(example2), "Examples should contain example2");
    }
}
