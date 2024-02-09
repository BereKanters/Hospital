package com.example.hospital;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ZiekenhuisApp extends Application {



    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    private ObservableList<Zorgverlener> zorgverleners = FXCollections.observableArrayList();
    private ObservableList<Afspraak> afspraken = FXCollections.observableArrayList();

    private ListView<Patient> patientListView;
    private ListView<Zorgverlener> zorgverlenerListView;
    private ListView<Afspraak> afspraakListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ziekenhuis App");

//        List<Patient> patients = DummyData.createDummyPatients();
//        List<Zorgverlener> zorgverleners = DummyData.createDummyZorgverleners();
//        List<Afspraak> afspraken = DummyData.createDummyAfspraken(patients, zorgverleners);


        // UI-elementen voor Patient
        Button addPatientButton = new Button("Voeg Patient toe");
        Button editPatientButton = new Button("Bewerk Patient");
        Button deletePatientButton = new Button("Verwijder Patient");

        patientListView = new ListView<>(patients);

        patientListView.setCellFactory(param -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getPatientID() + " | Naam: " + item.getName() +
                            " | Geboortedatum: " + item.getBirthdate() + " | Contact: " + item.getContact());
                }
            }
        });

        addPatientButton.setOnAction(e -> addPatient());
        editPatientButton.setOnAction(e -> editPatient(patientListView.getSelectionModel().getSelectedItem()));
        deletePatientButton.setOnAction(e -> deletePatient(patientListView.getSelectionModel().getSelectedItem()));

        // UI-elementen voor Zorgverlener
        Button addZorgverlenerButton = new Button("Voeg Zorgverlener toe");
        Button editZorgverlenerButton = new Button("Bewerk Zorgverlener");
        Button deleteZorgverlenerButton = new Button("Verwijder Zorgverlener");

        zorgverlenerListView = new ListView<>(zorgverleners);

        zorgverlenerListView.setCellFactory(param -> new ListCell<Zorgverlener>() {
            @Override
            protected void updateItem(Zorgverlener item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getZorgverlenerID() + " | Naam: " + item.getNaam() +
                            " | Specialisme: " + item.getSpecialisme() + " | Contact: " + item.getContactgegevens());
                }
            }
        });

        addZorgverlenerButton.setOnAction(e -> addZorgverlener());
        editZorgverlenerButton.setOnAction(e -> editZorgverlener(zorgverlenerListView.getSelectionModel().getSelectedItem()));
        deleteZorgverlenerButton.setOnAction(e -> deleteZorgverlener(zorgverlenerListView.getSelectionModel().getSelectedItem()));

        // UI-elementen voor Afspraak
        Button addAfspraakButton = new Button("Maak Afspraak");
        Button editAfspraakButton = new Button("Bewerk Afspraak");
        Button deleteAfspraakButton = new Button("Verwijder Afspraak");

        afspraakListView = new ListView<>(afspraken);

        afspraakListView.setCellFactory(param -> new ListCell<Afspraak>() {
            @Override
            protected void updateItem(Afspraak item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getAfspraakID() + " | Status: " + item.getStatus() +
                            " | Datum: " + item.getDatum() + " | Tijd: " + item.getTijd());
                }
            }
        });

        addAfspraakButton.setOnAction(e -> addAfspraak());
        editAfspraakButton.setOnAction(e -> editAfspraak(afspraakListView.getSelectionModel().getSelectedItem()));
        deleteAfspraakButton.setOnAction(e -> deleteAfspraak(afspraakListView.getSelectionModel().getSelectedItem()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                addPatientButton, editPatientButton, deletePatientButton, patientListView,
                addZorgverlenerButton, editZorgverlenerButton, deleteZorgverlenerButton, zorgverlenerListView,
                addAfspraakButton, editAfspraakButton, deleteAfspraakButton, afspraakListView
        );

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("Stylesheet.css").toString());
        primaryStage.show();
    }

    private void addPatient() {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Voeg Patient toe");

        ButtonType addButton = new ButtonType("Voeg toe", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        TextField nameField = new TextField();
        DatePicker birthdatePicker = new DatePicker();
        TextField contactField = new TextField();

        dialog.getDialogPane().setContent(new VBox(10, new Label("Naam:"), nameField,
                new Label("Geboortedatum:"), birthdatePicker,
                new Label("Contactgegevens:"), contactField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    String name = nameField.getText();
                    LocalDate birthdate = birthdatePicker.getValue();
                    String contact = contactField.getText();

                    if (name.isEmpty() || birthdate == null || contact.isEmpty()) {
                        showAlert("Fout", "Vul alle velden in.");
                        return null;
                    }

                    int generatedID = patients.size() + 1;
                    Patient newPatient = new Patient(generatedID, name, birthdate, contact);
                    patients.add(newPatient);

                    refreshPatientListView();

                    return newPatient;
                } catch (Exception e) {
                    showAlert("Fout", "Er is een fout opgetreden bij het toevoegen van de patient.");
                }
            }
            return null;
        });

        Optional<Patient> result = dialog.showAndWait();
        result.ifPresent(patient -> showAlert("Succes", "Patient toegevoegd: " + patient.getName()));
    }

    private void editPatient(Patient patient) {
        if (patient != null) {
            Dialog<Patient> dialog = new Dialog<>();
            dialog.setTitle("Bewerk Patient");

            ButtonType saveButton = new ButtonType("Opslaan", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

            TextField nameField = new TextField(patient.getName());
            DatePicker birthdatePicker = new DatePicker(patient.getBirthdate());
            TextField contactField = new TextField(patient.getContact());

            dialog.getDialogPane().setContent(new VBox(10, new Label("Naam:"), nameField,
                    new Label("Geboortedatum:"), birthdatePicker,
                    new Label("Contactgegevens:"), contactField));

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButton) {
                    try {
                        String name = nameField.getText();
                        LocalDate birthdate = birthdatePicker.getValue();
                        String contact = contactField.getText();

                        if (name.isEmpty() || birthdate == null || contact.isEmpty()) {
                            showAlert("Fout", "Vul alle velden in.");
                            return null;
                        }

                        patient.setName(name);
                        patient.setBirthdate(birthdate);
                        patient.setContact(contact);

                        refreshPatientListView();

                        return patient;
                    } catch (Exception e) {
                        showAlert("Fout", "Er is een fout opgetreden bij het bewerken van de patient.");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        }
    }

    private void deletePatient(Patient patient) {
        if (patient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bevestig Verwijdering");
            alert.setHeaderText(null);
            alert.setContentText("Weet je zeker dat je de patient '" + patient.getName() + "' wilt verwijderen?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                patients.remove(patient);

                refreshPatientListView();

                showAlert("Succes", "Patient verwijderd: " + patient.getName());
            }
        }
    }

    private void refreshPatientListView() {
        patientListView.setItems(null);
        patientListView.setItems(patients);
    }

    private void addZorgverlener() {
        Dialog<Zorgverlener> dialog = new Dialog<>();
        dialog.setTitle("Voeg Zorgverlener toe");

        ButtonType addButton = new ButtonType("Voeg toe", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        TextField nameField = new TextField();
        TextField specialismeField = new TextField();
        TextField contactField = new TextField();

        dialog.getDialogPane().setContent(new VBox(10, new Label("Naam:"), nameField,
                new Label("Specialisme:"), specialismeField,
                new Label("Contactgegevens:"), contactField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    String name = nameField.getText();
                    String specialisme = specialismeField.getText();
                    String contact = contactField.getText();

                    if (name.isEmpty() || specialisme.isEmpty() || contact.isEmpty()) {
                        showAlert("Fout", "Vul alle velden in.");
                        return null;
                    }

                    int generatedID = zorgverleners.size() + 1;
                    Zorgverlener newZorgverlener = new Zorgverlener(generatedID, name, specialisme, contact);
                    zorgverleners.add(newZorgverlener);

                    refreshZorgverlenerListView();

                    return newZorgverlener;
                } catch (Exception e) {
                    showAlert("Fout", "Er is een fout opgetreden bij het toevoegen van de zorgverlener.");
                }
            }
            return null;
        });

        Optional<Zorgverlener> result = dialog.showAndWait();
        result.ifPresent(zorgverlener -> showAlert("Succes", "Zorgverlener toegevoegd: " + zorgverlener.getNaam()));
    }

    private void editZorgverlener(Zorgverlener zorgverlener) {
        if (zorgverlener != null) {
            Dialog<Zorgverlener> dialog = new Dialog<>();
            dialog.setTitle("Bewerk Zorgverlener");

            ButtonType saveButton = new ButtonType("Opslaan", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

            TextField nameField = new TextField(zorgverlener.getNaam());
            TextField specialismeField = new TextField(zorgverlener.getSpecialisme());
            TextField contactField = new TextField(zorgverlener.getContactgegevens());

            dialog.getDialogPane().setContent(new VBox(10, new Label("Naam:"), nameField,
                    new Label("Specialisme:"), specialismeField,
                    new Label("Contactgegevens:"), contactField));

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButton) {
                    try {
                        String name = nameField.getText();
                        String specialisme = specialismeField.getText();
                        String contact = contactField.getText();

                        if (name.isEmpty() || specialisme.isEmpty() || contact.isEmpty()) {
                            showAlert("Fout", "Vul alle velden in.");
                            return null;
                        }

                        zorgverlener.setNaam(name);
                        zorgverlener.setSpecialisme(specialisme);
                        zorgverlener.setContactgegevens(contact);

                        refreshZorgverlenerListView();

                        return zorgverlener;
                    } catch (Exception e) {
                        showAlert("Fout", "Er is een fout opgetreden bij het bewerken van de zorgverlener.");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        }
    }

    private void deleteZorgverlener(Zorgverlener zorgverlener) {
        if (zorgverlener != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bevestig Verwijdering");
            alert.setHeaderText(null);
            alert.setContentText("Weet je zeker dat je de zorgverlener '" + zorgverlener.getNaam() + "' wilt verwijderen?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                zorgverleners.remove(zorgverlener);

                refreshZorgverlenerListView();

                showAlert("Succes", "Zorgverlener verwijderd: " + zorgverlener.getNaam());
            }
        }
    }

    private void refreshZorgverlenerListView() {
        zorgverlenerListView.setItems(null);
        zorgverlenerListView.setItems(zorgverleners);
    }

    private void addAfspraak() {
        Dialog<Afspraak> dialog = new Dialog<>();
        dialog.setTitle("Maak Afspraak");

        ButtonType addButton = new ButtonType("Maak afspraak", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        ChoiceBox<Patient> patientChoiceBox = new ChoiceBox<>(patients);
        ChoiceBox<Zorgverlener> zorgverlenerChoiceBox = new ChoiceBox<>(zorgverleners);
        DatePicker datumPicker = new DatePicker();
        TextField tijdField = new TextField();

        dialog.getDialogPane().setContent(new VBox(10,
                new Label("Selecteer Patient:"), patientChoiceBox,
                new Label("Selecteer Zorgverlener:"), zorgverlenerChoiceBox,
                new Label("Datum:"), datumPicker,
                new Label("Tijd:"), tijdField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    Patient selectedPatient = patientChoiceBox.getValue();
                    Zorgverlener selectedZorgverlener = zorgverlenerChoiceBox.getValue();
                    LocalDate datum = datumPicker.getValue();
                    LocalTime tijd = LocalTime.parse(tijdField.getText());

                    if (selectedPatient == null || selectedZorgverlener == null || datum == null || tijdField.getText().isEmpty()) {
                        showAlert("Fout", "Vul alle velden in.");
                        return null;
                    }

                    int generatedID = afspraken.size() + 1;
                    Afspraak newAfspraak = new Afspraak(generatedID, "Gepland", datum, tijd, selectedPatient, selectedZorgverlener);
                    afspraken.add(newAfspraak);

                    refreshAfspraakListView();

                    return newAfspraak;
                } catch (Exception e) {
                    showAlert("Fout", "Er is een fout opgetreden bij het maken van de afspraak.");
                }
            }
            return null;
        });

        Optional<Afspraak> result = dialog.showAndWait();
        result.ifPresent(afspraak -> showAlert("Succes", "Afspraak gemaakt voor " + afspraak.getDatum() + " om " + afspraak.getTijd()));
    }

    private void editAfspraak(Afspraak afspraak) {
        if (afspraak != null) {
            Dialog<Afspraak> dialog = new Dialog<>();
            dialog.setTitle("Bewerk Afspraak");

            ButtonType saveButton = new ButtonType("Opslaan", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

            ChoiceBox<Patient> patientChoiceBox = new ChoiceBox<>(patients);
            ChoiceBox<Zorgverlener> zorgverlenerChoiceBox = new ChoiceBox<>(zorgverleners);
            DatePicker datumPicker = new DatePicker(afspraak.getDatum());
            TextField tijdField = new TextField(afspraak.getTijd().toString());

            patientChoiceBox.setValue(afspraak.getPatient());
            zorgverlenerChoiceBox.setValue(afspraak.getZorgverlener());

            dialog.getDialogPane().setContent(new VBox(10,
                    new Label("Selecteer Patient:"), patientChoiceBox,
                    new Label("Selecteer Zorgverlener:"), zorgverlenerChoiceBox,
                    new Label("Datum:"), datumPicker,
                    new Label("Tijd:"), tijdField));

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButton) {
                    try {
                        Patient selectedPatient = patientChoiceBox.getValue();
                        Zorgverlener selectedZorgverlener = zorgverlenerChoiceBox.getValue();
                        LocalDate datum = datumPicker.getValue();
                        LocalTime tijd = LocalTime.parse(tijdField.getText());

                        if (selectedPatient == null || selectedZorgverlener == null || datum == null || tijdField.getText().isEmpty()) {
                            showAlert("Fout", "Vul alle velden in.");
                            return null;
                        }

                        afspraak.setPatient(selectedPatient);
                        afspraak.setZorgverlener(selectedZorgverlener);
                        afspraak.setDatum(datum);
                        afspraak.setTijd(tijd);

                        refreshAfspraakListView();

                        return afspraak;
                    } catch (Exception e) {
                        showAlert("Fout", "Er is een fout opgetreden bij het bewerken van de afspraak.");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        }
    }

    private void deleteAfspraak(Afspraak afspraak) {
        if (afspraak != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bevestig Verwijdering");
            alert.setHeaderText(null);
            alert.setContentText("Weet je zeker dat je de afspraak wilt verwijderen?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                afspraken.remove(afspraak);

                refreshAfspraakListView();

                showAlert("Succes", "Afspraak verwijderd op " + afspraak.getDatum() + " om " + afspraak.getTijd());
            }
        }
    }

    private void refreshAfspraakListView() {
        afspraakListView.setItems(null);
        afspraakListView.setItems(afspraken);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Patient {
        private int patientID;
        private String name;
        private LocalDate birthdate;
        private String contact;

        public Patient(int patientID, String name, LocalDate birthdate, String contact) {
            this.patientID = patientID;
            this.name = name;
            this.birthdate = birthdate;
            this.contact = contact;
        }

        public int getPatientID() {
            return patientID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDate getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        @Override
        public String toString() {
            return "Patient{" +
                    "patientID=" + patientID +
                    ", name='" + name + '\'' +
                    ", birthdate=" + birthdate +
                    ", contact='" + contact + '\'' +
                    '}';
        }
    }

    public static class Zorgverlener {
        private int zorgverlenerID;
        private String naam;
        private String specialisme;
        private String contactgegevens;

        public Zorgverlener(int zorgverlenerID, String naam, String specialisme, String contactgegevens) {
            this.zorgverlenerID = zorgverlenerID;
            this.naam = naam;
            this.specialisme = specialisme;
            this.contactgegevens = contactgegevens;
        }

        public int getZorgverlenerID() {
            return zorgverlenerID;
        }

        public String getNaam() {
            return naam;
        }

        public void setNaam(String naam) {
            this.naam = naam;
        }

        public String getSpecialisme() {
            return specialisme;
        }

        public void setSpecialisme(String specialisme) {
            this.specialisme = specialisme;
        }

        public String getContactgegevens() {
            return contactgegevens;
        }

        public void setContactgegevens(String contactgegevens) {
            this.contactgegevens = contactgegevens;
        }

        @Override
        public String toString() {
            return "Zorgverlener{" +
                    "zorgverlenerID=" + zorgverlenerID +
                    ", naam='" + naam + '\'' +
                    ", specialisme='" + specialisme + '\'' +
                    ", contactgegevens='" + contactgegevens + '\'' +
                    '}';
        }
    }

    public static class Afspraak {
        private int afspraakID;
        private String status;
        private LocalDate datum;
        private LocalTime tijd;
        private Patient patient;
        private Zorgverlener zorgverlener;

        public Afspraak(int afspraakID, String status, LocalDate datum, LocalTime tijd, Patient patient, Zorgverlener zorgverlener) {
            this.afspraakID = afspraakID;
            this.status = status;
            this.datum = datum;
            this.tijd = tijd;
            this.patient = patient;
            this.zorgverlener = zorgverlener;
        }

        public int getAfspraakID() {
            return afspraakID;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getDatum() {
            return datum;
        }

        public void setDatum(LocalDate datum) {
            this.datum = datum;
        }

        public LocalTime getTijd() {
            return tijd;
        }

        public void setTijd(LocalTime tijd) {
            this.tijd = tijd;
        }

        public Patient getPatient() {
            return patient;
        }

        public void setPatient(Patient patient) {
            this.patient = patient;
        }

        public Zorgverlener getZorgverlener() {
            return zorgverlener;
        }

        public void setZorgverlener(Zorgverlener zorgverlener) {
            this.zorgverlener = zorgverlener;
        }

        @Override
        public String toString() {
            return "Afspraak{" +
                    "afspraakID=" + afspraakID +
                    ", status='" + status + '\'' +
                    ", datum=" + datum +
                    ", tijd=" + tijd +
                    ", patient=" + patient +
                    ", zorgverlener=" + zorgverlener +
                    '}';
        }
    }

}