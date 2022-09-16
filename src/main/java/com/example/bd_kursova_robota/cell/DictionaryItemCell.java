package com.example.bd_kursova_robota.cell;

import com.example.bd_kursova_robota.HelloApplication;
import com.example.bd_kursova_robota.controller.DictionaryItemRowController;
import com.example.bd_kursova_robota.controller.DocumentRowController;
import com.example.bd_kursova_robota.model.Document;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class DictionaryItemCell extends ListCell<DictionaryModel> {

    private Parent node;
    private DictionaryItemRowController controller;

    public DictionaryItemCell(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("row_dictionary_item.fxml"));
            node = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("New cell created!");
    }

    @Override
    protected void updateItem(DictionaryModel item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setHeight(33.6);
        if (empty) {
            controller.setModel(null);
            setGraphic(node);
        }
        else {
            controller.setModel(item);
            setGraphic(node);
        }
    }
}
