package com.codifytech.hospitalManagement.controller;

import com.codifytech.hospitalManagement.models.DoctorResponseDto;
import com.codifytech.hospitalManagement.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class HospitalController {

    private final DoctorService doctorService;
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {
        List<DoctorResponseDto> doctorResponseDtos = new ArrayList<>();
        doctorResponseDtos.add(new DoctorResponseDto(1L, "Dr. Rakesh Mehta", "Cardiology", "rakesh.mehta@example.com"));
        return ResponseEntity.ok(doctorResponseDtos);
    }
}
