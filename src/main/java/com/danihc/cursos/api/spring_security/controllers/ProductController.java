package com.danihc.cursos.api.spring_security.controllers;

import com.danihc.cursos.api.spring_security.dto.SaveProduct;
import com.danihc.cursos.api.spring_security.persistence.entities.Product;
import com.danihc.cursos.api.spring_security.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/products")
//@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    public ResponseEntity<Page<Product>> findAll(Pageable pageable){
        Page<Product> productsPage = productService.findAll(pageable);

        if(productsPage.hasContent()){
            return ResponseEntity.ok(productsPage);
        }
        Arrays.asList(1, 2, 3).stream().reduce(0, Integer::sum);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAuthority('READ_ONE_PRODUCT')")
    // @CrossOrigin(origins = "https://www.google.com")
    public ResponseEntity<Product> findOneById(@PathVariable Long productId){
        Optional<Product> product = productService.findOneById(productId);

        if(product.isPresent()){
            return ResponseEntity.ok(product.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ONE_PRODUCT')")
    public ResponseEntity<Product> createOne(@RequestBody @Valid SaveProduct saveProduct){
        Product product = productService.createOne(saveProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT')")
    public ResponseEntity<Product> updateOneById(@PathVariable Long productId,
                                                 @RequestBody @Valid SaveProduct saveProduct){
        Product product = productService.updateOneById(productId, saveProduct);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}/disabled")
    @PreAuthorize("hasAuthority('DISABLE_ONE_PRODUCT')")
    public ResponseEntity<Product> disableOneById(@PathVariable Long productId){
        Product product = productService.disableObeById(productId);
        return ResponseEntity.ok(product);
    }
}
