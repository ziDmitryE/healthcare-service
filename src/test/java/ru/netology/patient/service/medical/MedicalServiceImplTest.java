package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

class MedicalServiceImplTest {

    @Test
    void checkBloodPressure() {
        PatientInfoRepository pir = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(pir.getById("007")).
                thenReturn(new PatientInfo("007", "James", "Bond", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(140, 100))));

        SendAlertService sas = Mockito.mock(SendAlertService.class);

        MedicalService ms = new MedicalServiceImpl(pir, sas);
        ms.checkBloodPressure("007", new BloodPressure(120, 80));

        String expected = "Warning, patient with id: 007, need help";

        Mockito.verify(sas, Mockito.times(1)).send(expected);
        // или
        Mockito.verify(sas, Mockito.only()).send(Mockito.anyString());
    }

    @Test
    void checkBloodPressureWithCorrectParameters() {
        PatientInfoRepository pir = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(pir.getById("007")).
                thenReturn(new PatientInfo("007", "James", "Bond", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        SendAlertService sas = Mockito.mock(SendAlertService.class);

        MedicalService ms = new MedicalServiceImpl(pir, sas);
        ms.checkBloodPressure("007", new BloodPressure(120, 80));

        Mockito.verify(sas, Mockito.never()).send(Mockito.anyString());
    }

    @Test
    void checkTemperature() {
        PatientInfoRepository pir = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(pir.getById("007")).
                thenReturn(new PatientInfo("007", "James", "Bond", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("40"), new BloodPressure(120, 80))));

        SendAlertService sas = Mockito.mock(SendAlertService.class);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService ms = new MedicalServiceImpl(pir, sas);
        ms.checkTemperature("007", new BigDecimal("36.6"));

        String expected = "Warning, patient with id: 007, need help";

        Mockito.verify(sas, Mockito.atLeast(1)).send(expected);
        // или
        Mockito.verify(sas, Mockito.only()).send(Mockito.anyString());

        Mockito.verify(pir).getById(argumentCaptor.capture());
        Assertions.assertEquals("007", argumentCaptor.getValue());
    }

    @Test
    void testSend() {
        SendAlertService sas = Mockito.mock(SendAlertService.class);
        sas.send("message");
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sas).send(argumentCaptor.capture());
        Mockito.verify(sas, Mockito.times(1)).send("message");
        Assertions.assertEquals("message", argumentCaptor.getValue());
    }
}