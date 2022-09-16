package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ApplicationsFilterController extends TableFilterController{

    @FXML ChoiceBox<Object> department;
    @FXML ChoiceBox<Object> specialty;
    @FXML ChoiceBox<Object> educationForm;
    @FXML ChoiceBox<String> status;
    @FXML TextField year;

    @FXML
    public void initialize(){
        department.getItems().add("Немає");
        department.getItems().addAll(Storage.getInstance().getDepartments());
        department.getSelectionModel().selectFirst();

        specialty.getItems().add("Немає");
        specialty.getItems().addAll(Storage.getInstance().getSpecialties());
        specialty.getSelectionModel().selectFirst();

        educationForm.getItems().add("Немає");
        educationForm.getItems().addAll(Storage.getInstance().getEducationForms());
        educationForm.getSelectionModel().selectFirst();

        status.getItems().addAll("Немає", "Не прийнято", "Прийнято");
    }

    @FXML
    public void onClear(){
        year.clear();
        department.getSelectionModel().selectFirst();
        specialty.getSelectionModel().selectFirst();
        educationForm.getSelectionModel().selectFirst();
        onApply();
    }

    @FXML
    public void onApply(){
        ArrayList<String> whereClauses = new ArrayList<>();
        if (!year.getText().isEmpty()){
            try {
                whereClauses.add(String.format("_year = %1$d", Integer.parseInt(year.getText())));
            }
            catch (Exception e){
                year.clear();
            }
        }
        if (department.getValue() instanceof DictionaryModel){
            whereClauses.add("department_id = " + ((DictionaryModel) department.getValue()).getId());
        }
        if (specialty.getValue() instanceof DictionaryModel){
            whereClauses.add("specialty_id = " + ((DictionaryModel) specialty.getValue()).getId());
        }
        if (educationForm.getValue() instanceof DictionaryModel){
            whereClauses.add("education_form_id = " + ((DictionaryModel) educationForm.getValue()).getId());
        }
        if (status.getSelectionModel().getSelectedIndex() > 0){
            whereClauses.add("admission_application._status = " + (status.getSelectionModel().getSelectedIndex() - 1));
        }
        getFilterListener().onFilterSet(whereClauses);
    }


}
