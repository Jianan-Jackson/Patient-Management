package com.pm.patientservice.service;


import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patientList = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOS = patientList.stream()
                .map(PatientMapper::toDTO)
                .toList();

        return patientResponseDTOS;
    }


}
