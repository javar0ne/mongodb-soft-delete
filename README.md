### MongoDB Soft Delete
This project offers a set of classes and annotations to implement soft delete in MongoDB

### Usage
In order to introduce soft delete in your Spring Boot application with MongoDB, you have to:
1. Instantiate in a configuration class the bean _CustomMongoTemplate_ and give to the bean the name _mongoTemplate_:

```java
import com.giavarone.mongodbsoftdelete.client.CustomMongoTemplate;

@Configuration
public class MyProjectConfiguration {
    @Bean("mongoTemplate")
    public CustomMongoTemplate customMongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
        return new CustomMongoTemplate(databaseFactory, converter);
    }
}
```
2. Add the following key-value to your _application.properties_
```properties
spring.main.allow-bean-definition-overriding=true
```
This way Spring Boot won't throw any exception on startup as we're going to override the default _mongoTemplate_ bean instance
3. Make your model classes extend _SoftDelete_
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "examples")
public class Example extends SoftDelete {
    private Long id;
    private String aString;
    private Integer aNumber;
}
```
4. In repositories, you can annotate methods with _@IncludeDeletedRecords_ to retrieve soft deleted records too
```java
@Repository
public interface ExampleRepository extends MongoRepository<Example, Long> {

    List<Example> findByaString(String aString);

    @IncludeDeletedRecords
    List<Example> findByaNumber(Integer aNumber);
}
```
5. In your services class, implement _delete_ method like this:
```java
@Service
public class ExampleService {
    @Autowired
    private ExampleRepository exampleRepository;

    public Example findById(Long id) {
        return exampleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("id not valid"));
    }

    public Example save(Example example) {
        return exampleRepository.save(example);
    }

    public Example update(Long id, Example example) {
        Example _example = findById(id);
        _example.setAString(example.getAString());
        _example.setANumber(example.getANumber());
        _example.setDeletedAt(example.getDeletedAt());

        return exampleRepository.save(_example);
    }

    public void delete(Long id) {
        Example example = findById(id);
        example.setDeletedAt(LocalDateTime.now());
        update(id, example);
    }
}
```

### Acknowledgments
- The development of the project was inspired by [this](https://stackoverflow.com/a/71811055/21914683) answer on StackOverflow. 