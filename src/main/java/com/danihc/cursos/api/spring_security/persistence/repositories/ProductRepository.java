package com.danihc.cursos.api.spring_security.persistence.repositories;

import com.danihc.cursos.api.spring_security.persistence.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
