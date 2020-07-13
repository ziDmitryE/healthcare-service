package ru.netology.patient.service;

import java.math.BigDecimal;

import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;

public class MedicalServiceImpl implements MedicalService {

    private final PatientInfoRepository patientInfoRepository;

    public MedicalServiceImpl(PatientInfoRepository patientInfoRepository) {
        this.patientInfoRepository = patientInfoRepository;
    }

    @Override
    public void checkBloodPressure(String patientId, BloodPressure bloodPressure) {
        PatientInfo patientInfo = patientInfoRepository.getById(patientId);
        if (patientInfo == null) {
            throw new RuntimeException("Patient not found");
        }
        if (!patientInfo.getHealthInfo().getBloodPressure().equals(bloodPressure)) {
            System.out.printf("Warning, patient with id: %s, need help", patientInfo.getId());
        }
    }

    @Override
    public void checkTemperature(String patientId, BigDecimal temperature) {
        PatientInfo patientInfo = patientInfoRepository.getById(patientId);
        if (patientInfo == null) {
            throw new RuntimeException("Patient not found");
        }
        if (patientInfo.getHealthInfo().getNormalTemperature().subtract(new BigDecimal("1.5")).compareTo(temperature) > 0) {
            System.out.printf("Warning, patient with id: %s, need help", patientInfo.getId());
        }
    }
}
