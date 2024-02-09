module com.example.hospital {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;


    opens com.example.hospital to javafx.fxml;
    exports com.example.hospital;
}