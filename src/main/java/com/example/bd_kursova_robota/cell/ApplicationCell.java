package com.example.bd_kursova_robota.cell;

import com.example.bd_kursova_robota.HelloApplication;
import com.example.bd_kursova_robota.controller.ApplicationRowController;
import com.example.bd_kursova_robota.controller.OfferRowController;
import com.example.bd_kursova_robota.model.AdmApplication;
import com.example.bd_kursova_robota.model.Offer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class ApplicationCell extends ListCell<AdmApplication> {

    private Parent node;
    private ApplicationRowController controller;

    public ApplicationCell(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("row_adm_application.fxml"));
            node = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("New cell created!");
    }

    @Override
    protected void updateItem(AdmApplication item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        }
        else {
            controller.setScore(item);
            setGraphic(node);
        }
    }
}
