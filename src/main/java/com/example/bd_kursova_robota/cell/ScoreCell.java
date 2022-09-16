package com.example.bd_kursova_robota.cell;

import com.example.bd_kursova_robota.HelloApplication;
import com.example.bd_kursova_robota.controller.DocumentRowController;
import com.example.bd_kursova_robota.controller.ScoreRowController;
import com.example.bd_kursova_robota.model.Document;
import com.example.bd_kursova_robota.model.Score;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class ScoreCell extends ListCell<Score> {

    private Parent node;
    private ScoreRowController controller;
    private Score lastItem;

    public ScoreCell(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("row_eie_score.fxml"));
            node = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("New cell created!");
    }

    @Override
    protected void updateItem(Score item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setHeight(33.6);
        if (empty) {
            lastItem = null;
            setGraphic(null);
        }
        else {
            lastItem = item;
            controller.setScore(item);
            setGraphic(node);
        }
    }
}
