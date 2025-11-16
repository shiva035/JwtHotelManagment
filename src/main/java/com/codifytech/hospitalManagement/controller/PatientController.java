package com.codifytech.hospitalManagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @GetMapping()
    public ResponseEntity<String> pageName(){
        return ResponseEntity.ok("Welcome to patient service");
    }
}
