package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.model.Department;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.util.PhoneNumberFilter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DefaultStringConverter;

import java.util.ArrayList;
import java.util.HashMap;

public class DepartmentCreatorController extends DictionaryItemCreatorController{

    @FXML TextField phone;
    @FXML TextField email;

    @Override
    public void onApplyArguments(HashMap<Integer, Object> args) {
        super.onApplyArguments(args);
    }


    @Override
    public void initialize() {
        super.initialize();
        TextFormatter<String> textFormatter = new TextFormatter(new DefaultStringConverter(), "", new PhoneNumberFilter());
        phone.setTextFormatter(textFormatter);
    }

    @Override
    public ArrayList<DoneBlock> getDoneBlocks() {
        ArrayList<DoneBlock> doneBlocks = new ArrayList<>();
        if (phone.getText() == null ||getPhone().length() != 10){
            doneBlocks.add(new DoneBlock("Введіть коректний номер телефону"));
        }
        if (!isValidEmail()){
            doneBlocks.add(new DoneBlock("Введіть коректну адресу електронної пошти"));
        }

        return doneBlocks;
    }

    private boolean isValidEmail() {
        if (email.getText() == null) return false;
        int iAt = email.getText().indexOf('@');
        int iDot = email.getText().lastIndexOf('.');
        return iAt != -1 && iDot != -1 && iDot > iAt;
    }

    @Override
    public void onFill(DictionaryModel model) {
        super.onFill(model);
        Department department = ((Department) model);
        phone.setText(department.getPhone());
        email.setText(department.getEmail());
    }

    @Override
    public void setOkButtonBridge(OKButtonBridge okButtonBridge) {
        super.setOkButtonBridge(okButtonBridge);
    }

    @Override
    public Object getResult() {
        return getModel() == null ? new Department(name.getText(), getPhone(), email.getText()) : ((Department) getModel().setName(name.getText())).setEmail(email.getText()).setPhone(getPhone());
    }

    private String getPhone() {
        return phone.getText().replace(" ", "").replaceAll("-", "").replaceAll("\\(", "").replaceAll("\\)", "");
    }
}
