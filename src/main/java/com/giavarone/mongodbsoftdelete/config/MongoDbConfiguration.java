package com.giavarone.mongodbsoftdelete.config;

import com.giavarone.mongodbsoftdelete.client.CustomMongoTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoDbConfiguration {

    @Bean(name = "mongoTemplate")
    public CustomMongoTemplate customMongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
        return new CustomMongoTemplate(databaseFactory, converter);
    }
}