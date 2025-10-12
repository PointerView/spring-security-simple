package com.danihc.cursos.api.spring_security.controllers;

import com.danihc.cursos.api.spring_security.dto.SaveCategory;
import com.danihc.cursos.api.spring_security.dto.SaveProduct;
import com.danihc.cursos.api.spring_security.persistence.entities.Category;
import com.danihc.cursos.api.spring_security.persistence.entities.Product;
import com.danihc.cursos.api.spring_security.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<Category>> findAll(Pageable pageable){
        Page<Category> categoryPage = this.categoryService.findAll(pageable);

        if(categoryPage.hasContent()){
            return ResponseEntity.ok(categoryPage);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> findOneById(@PathVariable Long categoryId){
        Optional<Category> category = categoryService.findOneById(categoryId);

        if(category.isPresent()){
            return ResponseEntity.ok(category.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Category> createOne(@RequestBody @Valid SaveCategory saveCategory){
        Category category = categoryService.createOne(saveCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateOne(@PathVariable Long categoryId,
                                              @RequestBody @Valid SaveCategory saveCategory){
        Category category = categoryService.updateOne(categoryId, saveCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{categoryId}/disabled")
    public ResponseEntity<Category> disableObeById(@PathVariable Long categoryId){
        Category category = categoryService.disableObeById(categoryId);
        return ResponseEntity.ok(category);
    }
}
