package com.danihc.cursos.api.spring_security.services.impl;

import com.danihc.cursos.api.spring_security.dto.SaveCategory;
import com.danihc.cursos.api.spring_security.exceptions.ObjectNotFoundException;
import com.danihc.cursos.api.spring_security.persistence.entities.Category;
import com.danihc.cursos.api.spring_security.persistence.repositories.CategoryRepository;
import com.danihc.cursos.api.spring_security.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findOneById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    @Transactional
    public Category createOne(SaveCategory saveCategory) {
        Category category = new Category();
        category.setName(saveCategory.getName());
        category.setStatus(Category.CategoryStatus.ENABLED);

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateOne(Long categoryId, SaveCategory saveCategory) {
        Category categoryFomDB = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id " + categoryId));
        categoryFomDB.setName(saveCategory.getName());

        return categoryRepository.save(categoryFomDB);
    }

    @Override
    @Transactional
    public Category disableObeById(Long categoryId) {
        Category categoryFomDB = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id " + categoryId));
        categoryFomDB.setStatus(Category.CategoryStatus.DISABLED);

        return categoryRepository.save(categoryFomDB);
    }
}
