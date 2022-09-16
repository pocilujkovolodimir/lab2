package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.filter.FloatFilter;
import com.example.bd_kursova_robota.model.Region;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ApplicantsFilterController extends TableFilterController{

    @FXML TextField name;
    @FXML TextField patronymic;
    @FXML TextField surname;
    @FXML TextField avgFrom;
    @FXML TextField avgTo;
    @FXML ChoiceBox<Object> region;

    @FXML
    public void initialize(){
        region.getItems().add("Немає");
        region.getItems().addAll(Storage.getInstance().getRegions());
        region.getSelectionModel().selectFirst();
    }

    @FXML
    public void onClear(){
        name.clear();
        patronymic.clear();
        surname.clear();
        avgFrom.clear();
        avgTo.clear();
        region.getSelectionModel().selectFirst();
        onApply();
    }

    @FXML
    public void onApply(){
        ArrayList<String> whereClauses = new ArrayList<>();
        if (!name.getText().isEmpty()){
            whereClauses.add("_name LIKE '" + name.getText().replaceAll("'", "''") + "%'");
        }
        if (!patronymic.getText().isEmpty()){
            whereClauses.add("patronymic LIKE '" + patronymic.getText().replaceAll("'", "''") + "%'");
        }
        if (!surname.getText().isEmpty()){
            whereClauses.add("surname LIKE '" + surname.getText().replaceAll("'", "''") + "%'");
        }
        if (!avgFrom.getText().isEmpty() && !avgTo.getText().isEmpty()){
            try {
                whereClauses.add(String.format("average BETWEEN %1$s AND %2$s", String.valueOf(Float.parseFloat(avgFrom.getText())), Float.parseFloat(avgTo.getText())));
            }
            catch (Exception e){
                e.printStackTrace();
                clearBetween();
            }
        }
        if (region.getValue() instanceof Region){
            whereClauses.add("region_id = " + ((Region) region.getValue()).getId());
        }
        getFilterListener().onFilterSet(whereClauses);
    }

    private void clearBetween() {
        avgTo.setText("");
        avgFrom.setText("");
    }

}
