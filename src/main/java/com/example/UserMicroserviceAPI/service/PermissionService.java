
package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.model.Permission;
import com.example.UserMicroserviceAPI.model.PermissionDto;
import com.example.UserMicroserviceAPI.repository.AuthorityRepository;
import com.example.UserMicroserviceAPI.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public List<PermissionDto> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PermissionDto toDTO(Permission permission) {
        PermissionDto dto = new PermissionDto();
        dto.setId(permission.getId());
        dto.setPermission(permission.getPermission());
        dto.setPermissionDescription(permission.getPermissionDescription());

        Set<Long> authorityIds = permission.getAuthorities().stream()
                .map(Authority::getId)
                .collect(Collectors.toSet());
        dto.setAuthorityIds(authorityIds);

        return dto;
    }

    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    public List<Permission> saveAll(List<Permission> permissions) {
        return permissionRepository.saveAll(permissions);
    }
}
