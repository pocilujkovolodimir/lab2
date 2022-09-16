package com.example.bd_kursova_robota;

import com.example.bd_kursova_robota.controller.BaseController;
import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.User;
import com.example.bd_kursova_robota.util.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class HelloController extends BaseController{

    @FXML TextField signInLogin;
    @FXML PasswordField signInPassword;


    @FXML
    public void signIn(){
        String login = signInLogin.getText();
        String password = signInPassword.getText();
        User user = Storage.getInstance().findUser(login);
        if (user == null){
            showPopupMessage("Такого користувача не існує");
            return;
        }
        boolean isCorrectPassword = user.isCorrectPassword(password);
        if (isCorrectPassword){
            Storage.getInstance().setCurrentUser(user);
            NavigationUtil.openMainPanel(user);
            signInLogin.getScene().getWindow().hide();
        }
        else{
            showPopupMessage("Невірний пароль");
        }
    }

    @Override
    public Window getPopupWindow() {
        return signInPassword.getScene().getWindow();
    }

}