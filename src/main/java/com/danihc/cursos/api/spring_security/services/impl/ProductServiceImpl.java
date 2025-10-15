package com.danihc.cursos.api.spring_security.services.impl;

import com.danihc.cursos.api.spring_security.dto.SaveProduct;
import com.danihc.cursos.api.spring_security.exceptions.ObjectNotFoundException;
import com.danihc.cursos.api.spring_security.persistence.entities.Category;
import com.danihc.cursos.api.spring_security.persistence.entities.Product;
import com.danihc.cursos.api.spring_security.persistence.repositories.ProductRepository;
import com.danihc.cursos.api.spring_security.services.CategoryService;
import com.danihc.cursos.api.spring_security.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findOneById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    @Transactional
    public Product createOne(SaveProduct saveProduct) {
        Product product = new Product();
        product.setName(saveProduct.getName());
        product.setPrice(saveProduct.getPrice());
        product.setStatus(Product.ProductStatus.ENABLED);

        Category category = new Category();
        category.setId(saveProduct.getCategoryId());

        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateOneById(Long productId, SaveProduct saveProduct) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + productId));
        productFromDB.setName(saveProduct.getName());
        productFromDB.setPrice(saveProduct.getPrice());
        productFromDB.setStatus(Product.ProductStatus.ENABLED);

        Category category = new Category();
        category.setId(saveProduct.getCategoryId());

        productFromDB.setCategory(category);

        return productRepository.save(productFromDB);
    }

    @Override
    @Transactional
    public Product disableObeById(Long productId) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + productId));
        productFromDB.setStatus(Product.ProductStatus.DISABLED);
        return productRepository.save(productFromDB);
    }
}
