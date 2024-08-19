
package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.model.AuthorityDto;
import com.example.UserMicroserviceAPI.model.Permission;
import com.example.UserMicroserviceAPI.model.User;
import com.example.UserMicroserviceAPI.repository.AuthorityRepository;
import com.example.UserMicroserviceAPI.repository.PermissionRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Transactional
    public Authority createAuthorityWithPermissions(AuthorityDto authorityDTO) {
        Authority authority = new Authority();
        authority.setAuthority(authorityDTO.getAuthority());
        authority.setAuthorityDescription(authorityDTO.getDescription());

        Set<Permission> permissions = new HashSet<>();
        for (Long permissionId : authorityDTO.getPermissionIds()) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));
            permissions.add(permission);
        }
        authority.setPermissions(permissions);
        return authorityRepository.save(authority);
    }

    public List<AuthorityDto> findAll() {
        List<Authority> authorities = authorityRepository.findAll();
        return authorities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AuthorityDto toDTO(Authority authority) {
        AuthorityDto dto = new AuthorityDto();
        dto.setId(authority.getId());
        dto.setAuthority(authority.getAuthority());
        dto.setDescription(authority.getAuthorityDescription());
        dto.setPermissionIds(authority.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet()));
        dto.setUserIds(authority.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet()));  // Adding user IDs to the DTO
        return dto;
    }
}
