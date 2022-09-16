package com.example.bd_kursova_robota;

import com.example.bd_kursova_robota.data.Storage;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Вхід до системи");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        Storage.getInstance().cacheDirectories();
    }

    public static void main(String[] args) {
        launch();
    }

}