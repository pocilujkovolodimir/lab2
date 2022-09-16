package com.example.bd_kursova_robota.cell;

import com.example.bd_kursova_robota.HelloApplication;
import com.example.bd_kursova_robota.controller.DocumentRowController;
import com.example.bd_kursova_robota.model.Document;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class DocumentCell extends ListCell<Document> {

    private Parent node;
    private DocumentRowController controller;
    private Document lastItem;

    public DocumentCell(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("row_document.fxml"));
            node = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("New cell created!");
    }

    @Override
    protected void updateItem(Document item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            lastItem = null;
            setGraphic(null);
        }
        else {
            lastItem = item;
            controller.setDocument(item);
            setGraphic(node);
        }
    }
}
