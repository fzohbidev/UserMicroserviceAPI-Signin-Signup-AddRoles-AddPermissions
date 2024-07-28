
package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.Permission;
import com.example.UserMicroserviceAPI.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }
}
