package com.example.bd_kursova_robota.cell;

import com.example.bd_kursova_robota.HelloApplication;
import com.example.bd_kursova_robota.controller.DictionaryItemRowController;
import com.example.bd_kursova_robota.model.User;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class UserItemCell extends ListCell<User> {

    private Parent node;
    private DictionaryItemRowController controller;

    public UserItemCell(){
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
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setHeight(33.6);
        if (empty) {
            controller.setModel(null);
            setGraphic(node);
        }
        else {
            controller.setModel(new DictionaryModel(item.getLogin(), ""));
            setGraphic(node);
        }
    }
}
