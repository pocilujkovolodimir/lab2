package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Document;
import com.example.bd_kursova_robota.model.DocumentType;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class DocumentCreatorController extends DetailsController{

    @FXML Spinner<Integer> quantity;
    @FXML ChoiceBox<DocumentType> name;
    @FXML TextField number;

    @Override
    public boolean canReturnResult() {
        return true;
    }

    @Override
    public Object getResult() {
        return new Document(name.getValue(), number.getText(), quantity.getValue());
    }

    @Override
    public String getTitle() {
        return "Додати документ";
    }

    @FXML
    public void initialize(){

    }

    @Override
    public void onApplyArguments(HashMap<Integer, Object> args) {
        super.onApplyArguments(args);
        name.getItems().addAll(Storage.getInstance().getDocumentTypes());
        name.getSelectionModel().selectFirst();
        quantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99,1));
    }
}
