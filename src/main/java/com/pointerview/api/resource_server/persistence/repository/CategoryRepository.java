package com.pointerview.api.resource_server.persistence.repository;

import com.pointerview.api.resource_server.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
