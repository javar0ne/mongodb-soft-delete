package com.giavarone.mongodbsoftdelete;

import com.giavarone.mongodbsoftdelete.config.factory.SoftDeleteMongoRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(repositoryFactoryBeanClass = SoftDeleteMongoRepositoryFactoryBean.class)
public class MongoDbSoftDeleteApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoDbSoftDeleteApplication.class, args);
    }
}
