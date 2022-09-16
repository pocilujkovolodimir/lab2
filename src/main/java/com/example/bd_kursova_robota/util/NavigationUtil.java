package com.example.bd_kursova_robota.util;

import com.example.bd_kursova_robota.HelloApplication;
import com.example.bd_kursova_robota.controller.*;
import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.User;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.util.ArrayList;

public class NavigationUtil {

    public static void openDictionaryEditor(String table, String windowTitle){
        openDictionaryEditor(table, windowTitle, null, null);
    }

    public static void openDictionaryEditor(String table, String windowTitle, String editorFxml, DictionaryEditorController.DataLoader dataLoader){
        try {
            FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("scene_dictionary_editor.fxml"));
            DictionaryEditorController controller = new DictionaryEditorController(table, dataLoader);
            Stage stage = new Stage();
            controller.setDialogListener(new BaseController.DialogListener() {
                @Override
                public void onSave() {
                    onCancel();
                    Storage.getInstance().cacheDirectories();
                }

                @Override
                public void onCancel() {
                    stage.close();
                }

                @Override
                public void onTitleVisibilityChanged(boolean titleVisible) {
                    stage.setTitle(titleVisible ? windowTitle : "");
                }
            });
            if (editorFxml != null){
                controller.setEditorFxml(editorFxml);
            }

            fxmlLoader2.setController(controller);
            Parent parent = fxmlLoader2.load();
            Scene scene = new Scene(parent);

            stage.setResizable(false);
            stage.setTitle(windowTitle);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openDepartmentsEditor(){
        openDictionaryEditor("department", "Факультети", "details_department_creator.fxml", new DictionaryEditorController.DataLoader() {
            @Override
            public ArrayList<DictionaryModel> load(String table) {
                return Storage.getInstance().getDepartments();
            }
        });
    }

    public static void openSpecialtiesEditor(){
        openDictionaryEditor("specialty", "Спеціальності", "details_specialty_creator.fxml", new DictionaryEditorController.DataLoader() {
            @Override
            public ArrayList<DictionaryModel> load(String table) {
                return Storage.getInstance().getSpecialties();
            }
        });
    }

    public static void openPrivilegeEditor(){
        openDictionaryEditor("privilege", "Пільги");
    }

    public static void openSubjectEditor(){
        openDictionaryEditor("eie_subject", "Предмети ЗНО");
    }

    public static void openRegionEditor(){
        openDictionaryEditor("region", "Області");
    }

    public static void openEducationFormEditor(){
        openDictionaryEditor("education_form", "Форми навчання");
    }

    public static void openUsersEditor(){
        try {
            String windowTitle = "Користувачі";
            FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("scene_users_editor.fxml"));
            UsersEditorController controller = new UsersEditorController();
            Stage stage = new Stage();
            controller.setDialogListener(new BaseController.DialogListener() {
                @Override
                public void onSave() {
                    onCancel();
                }

                @Override
                public void onCancel() {
                    stage.close();
                }

                @Override
                public void onTitleVisibilityChanged(boolean titleVisible) {
                    stage.setTitle(titleVisible ? windowTitle : "");
                }
            });

            fxmlLoader2.setController(controller);
            Parent parent = fxmlLoader2.load();
            Scene scene = new Scene(parent);

            stage.setResizable(false);
            stage.setTitle(windowTitle);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void openMainPanel(User user){
        try {
            String windowTitle = "Панель користувача: " + user.getLogin();
            FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("scene_admin_panel.fxml"));
            Stage stage = new Stage();
            Parent parent = fxmlLoader2.load();
            Scene scene = new Scene(parent);
            stage.setTitle(windowTitle);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    openSignInPanel();
                }
            });
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void openApplicantEditor(Node alignTo, long id, Runnable onSaved) {
        String windowTitle = id == -1 ? "Новий абітурієнт" : "Редагувати абітурієнта";
        try {
            FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("scene_applicant.fxml"));
            fxmlLoader2.setController(new ApplicantDialogController(id));
            Parent parent = fxmlLoader2.load();
            ApplicantDialogController dialogController = fxmlLoader2.getController();

            if (alignTo != null) {
                PopOver popOver = new PopOver();
                dialogController.setDialogListener(new BaseController.DialogListener() {
                    @Override
                    public void onSave() {
                        popOver.hide();
                    }

                    @Override
                    public void onCancel() {
                        popOver.hide();
                    }

                    @Override
                    public void onTitleVisibilityChanged(boolean titleVisible) {
                        popOver.setTitle(titleVisible ? windowTitle : "");
                    }
                });
                popOver.setContentNode(parent);
                popOver.setTitle("Новий абітурієнт");
                popOver.setCloseButtonEnabled(true);
                popOver.setHeaderAlwaysVisible(true);
                popOver.show(alignTo);
                double rem = javafx.scene.text.Font.getDefault().getSize();
                dialogController.setPopupInsetLeft(popOver.getArrowIndent() + popOver.getArrowSize());
                dialogController.setPopupInsetTop(1.6 * rem);
                dialogController.setPopupInsetRight(0.9 * rem);
            }
            else{
                Stage stage = new Stage();
                dialogController.setDialogListener(new BaseController.DialogListener() {
                    @Override
                    public void onSave() {
                        onCancel();
                        if (onSaved != null) onSaved.run();
                    }

                    @Override
                    public void onCancel() {
                        stage.close();
                    }

                    @Override
                    public void onTitleVisibilityChanged(boolean titleVisible) {
                        stage.setTitle(titleVisible ? windowTitle : "");
                    }
                });
                Scene scene = new Scene(parent);
                parent.setStyle("-fx-background-color: white;");
                stage.setTitle(windowTitle);
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openOfferEditor(int id, Runnable onEnd){
        String windowTitle = id == -1 ? "Нова вступна пропозиція" : "Редагувати вступну пропозицію";
        try {
            FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("scene_offer_creator.fxml"));
            fxmlLoader2.setController(new OfferController(id));
            Parent parent = fxmlLoader2.load();
            OfferController dialogController = fxmlLoader2.getController();
            Stage stage = new Stage();
            dialogController.setDialogListener(new BaseController.DialogListener() {
                @Override
                public void onSave() {
                    onCancel();
                    onEnd.run();
                }

                @Override
                public void onCancel() {
                    stage.close();
                }

                @Override
                public void onTitleVisibilityChanged(boolean titleVisible) {
                    stage.setTitle(titleVisible ? windowTitle : "");
                }
            });
            Scene scene = new Scene(parent);
            parent.setStyle("-fx-background-color: white;");
            stage.setTitle(windowTitle);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void openSignInPanel(){
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Вхід до системи");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
