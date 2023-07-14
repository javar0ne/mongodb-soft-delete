package com.giavarone.mongodbsoftdelete.client;

import com.giavarone.mongodbsoftdelete.model.SoftDelete;
import com.mongodb.client.MongoClient;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class CustomMongoTemplate extends MongoTemplate {

    public static final String DELETED_AT_KEY = "deletedAt";

    public CustomMongoTemplate(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
    }

    public CustomMongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    public CustomMongoTemplate(MongoDatabaseFactory mongoDbFactory, MongoConverter mongoConverter) {
        super(mongoDbFactory, mongoConverter);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> entityClass, String collectionName) {
        Assert.notNull(query, "Query must not be null");
        Assert.notNull(entityClass, "EntityClass must not be null");
        Assert.notNull(collectionName, "CollectionName must not be null");

        addDeletedAtExistFalseCriteriaToQuery(query);

        return super.find(query, entityClass, collectionName);
    }

    @Nullable
    @Override
    public <T> T findById(Object id, Class<T> entityClass, String collectionName) {
        T t = super.findById(id, entityClass, collectionName);

        if(t instanceof SoftDelete) {
            try {
                Field field = SoftDelete.class.getDeclaredField(DELETED_AT_KEY);
                field.setAccessible(Boolean.TRUE);
                if (Objects.nonNull(field.get(t))) {
                    return null;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }

        return t;
    }

    @Nullable
    @Override
    public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
        Assert.notNull(query, "Query must not be null");
        Assert.notNull(entityClass, "EntityClass must not be null");
        Assert.notNull(collectionName, "CollectionName must not be null");

        addDeletedAtExistFalseCriteriaToQuery(query);

        return super.findOne(query, entityClass, collectionName);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean exists(Query query, @Nullable Class<?> entityClass, String collectionName) {
        if (query == null) {
            throw new InvalidDataAccessApiUsageException("Query passed in to exist can't be null");
        }

        addDeletedAtExistFalseCriteriaToQuery(query);

        return super.exists(query, entityClass, collectionName);
    }

    @Override
    public long count(Query query, String collectionName) {
        addDeletedAtExistFalseCriteriaToQuery(query);

        return this.count(query, (Class)null, collectionName);
    }

    private void addDeletedAtExistFalseCriteriaToQuery(Query query) {
        Assert.notNull(query, "Query must not be null");
        query.addCriteria(Criteria.where(DELETED_AT_KEY).exists(Boolean.FALSE));
    }
}
