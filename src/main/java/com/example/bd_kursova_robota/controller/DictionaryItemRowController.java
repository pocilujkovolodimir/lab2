package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.model.base.DictionaryModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class DictionaryItemRowController {

    @FXML Label label;

    public void setModel(DictionaryModel model){
        label.setText(model == null ? "" : model.getName());
    }

}
