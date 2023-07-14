package com.giavarone.mongodbsoftdelete.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class SoftDelete {
    private LocalDateTime deletedAt;
}
