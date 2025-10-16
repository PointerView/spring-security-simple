package com.danihc.cursos.api.spring_security.persistence.repositories.security;

import com.danihc.cursos.api.spring_security.persistence.entities.security.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query("select o from Operation o where o.permitAll = true")
    List<Operation> findByPublicAccess();
}
