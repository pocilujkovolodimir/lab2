module com.example.bd_kursova_robota {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    requires mysql.connector.java;
    requires com.github.librepdf.openpdf;


    requires java.sql;
    requires java.desktop;


    opens com.example.bd_kursova_robota to javafx.fxml;
    opens com.example.bd_kursova_robota.controller to javafx.fxml;
    exports com.example.bd_kursova_robota;
    exports com.example.bd_kursova_robota.controller;
    exports com.example.bd_kursova_robota.model;
    exports com.example.bd_kursova_robota.model.base;
    exports com.example.bd_kursova_robota.model.view;
    opens com.example.bd_kursova_robota.model.view to javafx.fxml;

}