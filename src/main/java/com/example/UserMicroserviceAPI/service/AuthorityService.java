
package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.model.Permission;
import com.example.UserMicroserviceAPI.repository.AuthorityRepository;
import com.example.UserMicroserviceAPI.repository.PermissionRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Transactional
    public Authority createAuthorityWithPermissions(Authority authority) {
        Set<Permission> savedPermissions = new HashSet<>();
        for (Permission permission : authority.getPermissions()) {
            Permission savedPermission = permissionRepository.save(permission);
            savedPermissions.add(savedPermission);
        }
        authority.setPermissions(savedPermissions);
        return authorityRepository.save(authority);
    }
}
