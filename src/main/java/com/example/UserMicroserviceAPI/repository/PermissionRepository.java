
package com.example.UserMicroserviceAPI.repository;

import com.example.UserMicroserviceAPI.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
