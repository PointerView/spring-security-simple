package com.danihc.cursos.api.spring_security.persistence.repositories;

import com.danihc.cursos.api.spring_security.persistence.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
