package com.giavarone.mongodbsoftdelete.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Document(collection = "examples")
public class Example extends SoftDelete {
    private Long id;
    private String aString;
    private Integer aNumber;
}
