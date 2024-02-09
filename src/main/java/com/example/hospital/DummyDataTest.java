package com.example.hospital;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DummyDataTest {

    @Test
    void createDummyPatients_shouldCreatePatients() {
        List<ZiekenhuisApp.Patient> patients = DummyData.createDummyPatients();
        assertNotNull(patients);
        assertEquals(3, patients.size()); // Aanpassen op aantal
    }

    @Test
    void createDummyZorgverleners_shouldCreateZorgverleners() {
        List<ZiekenhuisApp.Zorgverlener> zorgverleners = DummyData.createDummyZorgverleners();
        assertNotNull(zorgverleners);
        assertEquals(3, zorgverleners.size()); // Aanpassen op aantal
    }

    @Test
    void createDummyAfspraken_shouldCreateAfspraken() {
        List<ZiekenhuisApp.Patient> patients = DummyData.createDummyPatients();
        List<ZiekenhuisApp.Zorgverlener> zorgverleners = DummyData.createDummyZorgverleners();
        List<ZiekenhuisApp.Afspraak> afspraken = DummyData.createDummyAfspraken(patients, zorgverleners);
        assertNotNull(afspraken);
        assertEquals(3, afspraken.size()); // Aanpassen op aantal
    }
}