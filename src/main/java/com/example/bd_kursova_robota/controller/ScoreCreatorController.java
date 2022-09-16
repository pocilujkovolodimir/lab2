package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Document;
import com.example.bd_kursova_robota.model.DocumentType;
import com.example.bd_kursova_robota.model.Score;
import com.example.bd_kursova_robota.model.Subject;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class ScoreCreatorController extends DetailsController{

    @FXML Spinner<Integer> score;
    @FXML ChoiceBox<Subject> subject;

    @Override
    public boolean canReturnResult() {
        return true;
    }

    @Override
    public Object getResult() {
        return new Score(subject.getValue(), score.getValue());
    }

    @Override
    public String getTitle() {
        return "Додати бал";
    }

    @FXML
    public void initialize(){
        subject.getItems().addAll(Storage.getInstance().getSubjects());
        subject.getSelectionModel().selectFirst();
        score.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 200,100));
    }

}
