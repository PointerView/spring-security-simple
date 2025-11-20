package com.pointerview.api.resource_server.persistence.repository.security;

import com.pointerview.api.resource_server.persistence.entity.security.GrantedPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<GrantedPermission, Long> {
}
