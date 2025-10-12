package com.danihc.cursos.api.spring_security.services;

import com.danihc.cursos.api.spring_security.dto.SaveCategory;
import com.danihc.cursos.api.spring_security.persistence.entities.Category;
import com.danihc.cursos.api.spring_security.persistence.entities.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {

    Page<Category> findAll(Pageable pageable);

    Optional<Category> findOneById(Long categoryId);

    Category createOne(SaveCategory saveCategory);

    Category updateOne(Long categoryId, @Valid SaveCategory saveCategory);

    Category disableObeById(Long categoryId);
}
