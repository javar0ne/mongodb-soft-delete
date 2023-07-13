package com.giavarone.mongodbsoftdelete.service;

import com.giavarone.mongodbsoftdelete.model.Example;
import com.giavarone.mongodbsoftdelete.mongo.MongoDBContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ExampleServiceIntegrationTest extends MongoDBContainerTest {

    @Autowired
    private ExampleService exampleService;

    @Test
    void testFindByIdWhenDeletedExampleIdProvidedThrowsException() {
        Example example = Example.builder()
            .id(1L)
            .aString("a string")
            .build();

        Example storedExample = exampleService.save(example);

        exampleService.delete(storedExample.getId());

        assertThrows(IllegalArgumentException.class, () -> exampleService.findById(storedExample.getId()));
    }
}
