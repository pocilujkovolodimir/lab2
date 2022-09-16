package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.model.AdmApplication;
import com.example.bd_kursova_robota.model.Score;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class ApplicationRowController {

    @FXML Label specialty;
    @FXML Label status;
    @FXML Label year;

    public void setScore(AdmApplication admApplication){
        specialty.setText(admApplication.getSpecialtyName());
        status.setText(String.valueOf(admApplication.getStatus() == 1 ? "Прийнято" : "–"));
        year.setText(String.valueOf(admApplication.getYear()));
    }

}
