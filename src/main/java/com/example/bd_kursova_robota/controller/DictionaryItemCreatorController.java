package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Document;
import com.example.bd_kursova_robota.model.DocumentType;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class DictionaryItemCreatorController extends DetailsController{

    public static final int KEY_INITIAL_MODEL = 0;

    private DictionaryModel model;

    @FXML TextField name;

    @Override
    public boolean canReturnResult() {
        return true;
    }

    @Override
    public Object getResult() {
        return model == null ? new DictionaryModel(name.getText(), "") : model.setName(name.getText());
    }

    @Override
    public String getTitle() {
        return model == null ? "Додати елемент" : "Редагувати елемент";
    }

    protected DictionaryModel getModel() {
        return model;
    }

    @FXML
    public void initialize(){
        Platform.runLater(()->{
            name.requestFocus();
            if (model != null){

                onFill(model);
            }
        });
    }

    public void onFill(DictionaryModel model) {
        name.setText(model.getName());
        name.selectAll();
    }

    @Override
    public void onApplyArguments(HashMap<Integer, Object> args) {
        super.onApplyArguments(args);
        if (args != null && args.containsKey(KEY_INITIAL_MODEL)){
            model = (DictionaryModel) args.get(KEY_INITIAL_MODEL);
        }
    }
}
