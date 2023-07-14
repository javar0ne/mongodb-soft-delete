package com.giavarone.mongodbsoftdelete.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public abstract class SoftDelete {
    private LocalDateTime deletedAt;
}
