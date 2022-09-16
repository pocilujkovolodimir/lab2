package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.model.Department;
import com.example.bd_kursova_robota.model.User;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.util.PhoneNumberFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DefaultStringConverter;

import java.util.ArrayList;
import java.util.HashMap;

public class UserCreatorController extends DetailsController{

    public static final int KEY_INITIAL_MODEL = 0;
    public static final Integer KEY_CREATE_OF_TYPE = 1;
    public static final Integer KEY_EXISTING_USERS = 2;

    @FXML TextField login;
    @FXML PasswordField password;
    @FXML PasswordField passwordRepeat;

    private User model;
    private int type;
    private ArrayList<User> existingUsers;

    @Override
    public ArrayList<DoneBlock> getDoneBlocks() {
        ArrayList<DoneBlock> doneBlocks = new ArrayList<>();

        if (login.getText() == null || login.getText().isEmpty()){
            doneBlocks.add(new DoneBlock("Введіть логін"));
        }
        if (model == null) {
            for (User user : existingUsers) {
                if (user.getLogin().equals(login.getText())) {
                    doneBlocks.add(new DoneBlock("Користувач з таким логіном вже існує"));
                    break;
                }
            }

        }
        boolean ifUpdate = model != null && !password.getText().isEmpty() && password.getText().length() < 6;
        if (model== null && (password.getText() == null || password.getText().length() < 6) || ifUpdate){
            doneBlocks.add(new DoneBlock("Введіть пароль довжиною 6 символів"));
        }
        String nonNull1 = password.getText();
        if (nonNull1 == null) nonNull1 = "";
        String nonNull2 = passwordRepeat.getText();
        if (nonNull2 == null) nonNull2 = "";
        if (!nonNull1.equals(nonNull2)){
            doneBlocks.add(new DoneBlock("Паролі не збігаються"));
        }

        return doneBlocks;
    }

    @Override
    public String getTitle() {
        return model == null ? "Новий" + " " + (type == User.TYPE_ADMIN ? "адміністратор" : type == User.TYPE_BASIC ? "звич. корист." : "статист") : "Редагувати корист.";
    }

    public void onFill(User model) {
        login.setText(model.getLogin());
    }

    @Override
    public Object getResult() {
        return model == null ? new User(login.getText(), password.getText(), type) : model.setLogin(login.getText()).setPassword(password.getText());
    }

    @FXML
    public void initialize(){
        Platform.runLater(()->{
            login.requestFocus();
            if (model != null){
                onFill(model);
            }
            else{
                password.setPromptText("");
                password.setText("");
                passwordRepeat.setText("");
            }
        });
    }

    @Override
    public boolean canReturnResult() {
        return true;
    }

    @Override
    public void onApplyArguments(HashMap<Integer, Object> args) {
        super.onApplyArguments(args);
        if (args != null){
            if (args.containsKey(KEY_INITIAL_MODEL)) {
                model = (User) args.get(KEY_INITIAL_MODEL);
            }
            else{
                type = (int) args.get(KEY_CREATE_OF_TYPE);
            }
            existingUsers = (ArrayList<User>) args.get(KEY_EXISTING_USERS);
        }
    }

}
