
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.Permission;
import com.example.UserMicroserviceAPI.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.findAll();
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission savedPermission = permissionService.save(permission);
        return ResponseEntity.ok(savedPermission);
    }
}
