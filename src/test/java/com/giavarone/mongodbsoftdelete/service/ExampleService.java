package com.giavarone.mongodbsoftdelete.service;

import com.giavarone.mongodbsoftdelete.model.Example;
import com.giavarone.mongodbsoftdelete.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
