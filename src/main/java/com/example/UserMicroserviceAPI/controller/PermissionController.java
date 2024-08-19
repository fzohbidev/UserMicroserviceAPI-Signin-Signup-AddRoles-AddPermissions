
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.Permission;
import com.example.UserMicroserviceAPI.model.PermissionDto;
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
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }

    @PostMapping
    public ResponseEntity<String> createPermissions(@RequestBody List<Permission> permissions) {
        List<Permission> savedPermissions = permissionService.saveAll(permissions);
        return ResponseEntity.ok(savedPermissions.size() + " permissions added successfully");
    }
}
