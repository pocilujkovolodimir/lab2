package com.example.bd_kursova_robota.cell;

import com.example.bd_kursova_robota.HelloApplication;
import com.example.bd_kursova_robota.controller.DocumentRowController;
import com.example.bd_kursova_robota.controller.OfferController;
import com.example.bd_kursova_robota.controller.OfferRowController;
import com.example.bd_kursova_robota.model.Document;
import com.example.bd_kursova_robota.model.Offer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class OfferCell extends ListCell<Offer> {

    private Parent node;
    private OfferRowController controller;

    public OfferCell(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("row_offer.fxml"));
            node = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("New cell created!");
    }

    @Override
    protected void updateItem(Offer item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        }
        else {
            controller.setOffer(item);
            setGraphic(node);
        }
    }
}
