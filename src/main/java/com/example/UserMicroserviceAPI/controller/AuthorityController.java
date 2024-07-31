
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityController {
    @Autowired
    private AuthorityService authorityService;

    @GetMapping
    public List<Authority> getAllAuthorities() {
        return authorityService.findAll();
    }

    @PostMapping
    public ResponseEntity<List<Authority>> createRoles(@RequestBody List<Authority> roles) {
        List<Authority> createdRoles = new ArrayList<>();
        for (Authority role : roles) {
            Authority createdRole = authorityService.createAuthorityWithPermissions(role);
            createdRoles.add(createdRole);
        }
        return ResponseEntity.ok(createdRoles);
    }
}
