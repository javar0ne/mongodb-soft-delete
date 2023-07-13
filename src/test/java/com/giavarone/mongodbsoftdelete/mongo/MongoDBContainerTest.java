package com.giavarone.mongodbsoftdelete.mongo;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class MongoDBContainerTest {

    private static final MongoDBContainer mongoContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0.6"));

    static {
        mongoContainer.start();
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoContainer.getReplicaSetUrl());
    }
}
