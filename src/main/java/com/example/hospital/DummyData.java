package com.example.hospital;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public static List<ZiekenhuisApp.Patient> createDummyPatients() {
        List<ZiekenhuisApp.Patient> patients = new ArrayList<>();
        patients.add(new ZiekenhuisApp.Patient(1, "John Doe", LocalDate.of(1990, 5, 15), "123-456-7890"));
        patients.add(new ZiekenhuisApp.Patient(2, "Jane Doe", LocalDate.of(1985, 8, 22), "987-654-3210"));
        patients.add(new ZiekenhuisApp.Patient(3, "Alice Johnson", LocalDate.of(1970, 3, 10), "555-123-4567"));
        return patients;
    }

    public static List<ZiekenhuisApp.Zorgverlener> createDummyZorgverleners() {
        List<ZiekenhuisApp.Zorgverlener> zorgverleners = new ArrayList<>();
        zorgverleners.add(new ZiekenhuisApp.Zorgverlener(1, "Dr. Smith", "Cardiologist", "555-789-0123"));
        zorgverleners.add(new ZiekenhuisApp.Zorgverlener(2, "Dr. Johnson", "Orthopedic Surgeon", "777-888-9999"));
        zorgverleners.add(new ZiekenhuisApp.Zorgverlener(3, "Dr. Brown", "Pediatrician", "111-222-3333"));
        return zorgverleners;
    }

    public static List<ZiekenhuisApp.Afspraak> createDummyAfspraken(List<ZiekenhuisApp.Patient> patients, List<ZiekenhuisApp.Zorgverlener> zorgverleners) {
        List<ZiekenhuisApp.Afspraak> afspraken = new ArrayList<>();
        afspraken.add(new ZiekenhuisApp.Afspraak(1, "Scheduled", LocalDate.now().plusDays(2), LocalTime.of(10, 30), patients.get(0), zorgverleners.get(0)));
        afspraken.add(new ZiekenhuisApp.Afspraak(2, "Scheduled", LocalDate.now().plusDays(5), LocalTime.of(14, 0), patients.get(1), zorgverleners.get(1)));
        afspraken.add(new ZiekenhuisApp.Afspraak(3, "Completed", LocalDate.now().minusDays(1), LocalTime.of(11, 45), patients.get(2), zorgverleners.get(2)));
        return afspraken;
    }
}