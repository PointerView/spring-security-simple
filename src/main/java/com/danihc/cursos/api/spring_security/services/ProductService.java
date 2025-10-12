package com.danihc.cursos.api.spring_security.services;

import com.danihc.cursos.api.spring_security.dto.SaveProduct;
import com.danihc.cursos.api.spring_security.persistence.entities.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    public Page<Product> findAll(Pageable pageable);

    Optional<Product> findOneById(Long productId);

    Product createOne(@Valid SaveProduct saveProduct);

    Product updateOneById(Long productId, @Valid SaveProduct saveProduct);

    Product disableObeById(Long productId);
}
