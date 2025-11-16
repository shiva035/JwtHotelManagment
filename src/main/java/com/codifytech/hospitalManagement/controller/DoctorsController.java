package com.codifytech.hospitalManagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
public class DoctorsController {
    @GetMapping()
    @PreAuthorize("")
    public ResponseEntity<String> pageName(){
        return ResponseEntity.ok("Welcome to doctor service");
    }
}
