package com.pm.patientservice.service;


import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException(
                    "A patient with this email " + "already exists: "
                            + patientRequestDTO.getEmail()
            );
        }


        Patient patient = patientRepository.save(
                PatientMapper.toPatient(patientRequestDTO)
        );

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID uuid, PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(uuid).orElseThrow(
                () -> new PatientNotFoundException("patient with id: " + uuid + "not found")
        );

        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), uuid)) {
            throw new EmailAlreadyExistsException(
                    "A patient with this email " + "already exists: "
                            + patientRequestDTO.getEmail()
            );
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedP = patientRepository.save(patient);

        return PatientMapper.toDTO(updatedP);
    }

    public void deletePatient(UUID uuid){
        patientRepository.deleteById(uuid);
    }


}
