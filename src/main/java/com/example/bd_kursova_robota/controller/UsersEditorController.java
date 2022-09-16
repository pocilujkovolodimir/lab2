package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.cell.DictionaryItemCell;
import com.example.bd_kursova_robota.cell.UserItemCell;
import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.User;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.model.base.SQLModel;
import com.example.bd_kursova_robota.util.ListUtil;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class UsersEditorController extends DetailsSupportController{

    @FXML ListView<User> adminList;
    @FXML ListView<User> basicList;
    @FXML ListView<User> guestList;
    @FXML TabPane tabPane;

    private ArrayList<User> initialList;
    private ArrayList<User> finalList;
    private ArrayList<User> updateList = new ArrayList<>();

    private String table;
    private String editorFxml = "details_user_creator.fxml";

    private void intiList(ListView<User> list){
        list.setCellFactory(param -> new UserItemCell());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Видалити");
        deleteMenuItem.setOnAction(event -> {
            contextMenu.hide();
            ObservableList<User> selected = list.getSelectionModel().getSelectedItems();
            for (User model : selected){
                if (model.equals(Storage.getInstance().getCurrentUser())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Самого себе видалити не можна.", ButtonType.OK);
                    alert.setHeaderText("Помилкова команда");
                    alert.showAndWait();
                    return;
                }
                finalList.remove(model);
                updateList.remove(model);
                list.getItems().remove(model);
            }
        });
        MenuItem addMenuItem = new MenuItem("Додати");
        addMenuItem.setOnAction(event -> {
            contextMenu.hide();
            onAdd();
        });
        contextMenu.getItems().add(deleteMenuItem);
        contextMenu.getItems().add(addMenuItem);
        list.setContextMenu(contextMenu);
        list.getItems().addAll(findUsers(list == basicList ? User.TYPE_BASIC : list == adminList ? User.TYPE_ADMIN : User.TYPE_GUEST));
        list.setOnMouseClicked(event -> handleMouseClick(list, event));
    }

    private ArrayList<User> findUsers(int type) {
        ArrayList<User> users = new ArrayList<>();
        for (User user : initialList){
            if (user.getType() == type){
                users.add(user);
            }
        }
        return users;
    }

    private void handleMouseClick(ListView<User> list, MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) return;
        User user = null;

        for (User user1 : list.getSelectionModel().getSelectedItems()){
            user = user1;
            break;
        }

        if (user == null) return;

        HashMap<Integer, Object> args = new HashMap<>();
        args.put(UserCreatorController.KEY_INITIAL_MODEL, user);
        args.put(UserCreatorController.KEY_CREATE_OF_TYPE, list == basicList ? 0 : 1);
        args.put(UserCreatorController.KEY_EXISTING_USERS, finalList);
        openDetails(editorFxml, args, 0);
    }

    public UsersEditorController(){
        initialList = Storage.getInstance().getUsers();
        finalList = new ArrayList<>(initialList);
    }

    @FXML
    public void onCancel(){
        getDialogListener().onCancel();
    }

    @FXML
    public void onSave(){
        Pair<ArrayList<User>, ArrayList<User>> pair = ListUtil.getAddedDeletedScores(initialList, finalList);
        for (User user: pair.getKey()){
            Storage.getInstance().notifyChange(user);
        }
        for (User user: pair.getValue()){
            Storage.getInstance().removeModel(user);
        }
        for (User user : updateList){
            Storage.getInstance().notifyChange(user);
        }
        getDialogListener().onSave();
    }

    @FXML
    public void onAdd(){
        HashMap<Integer, Object> args = new HashMap<>();
        args.put(UserCreatorController.KEY_CREATE_OF_TYPE, tabPane.getSelectionModel().isSelected(0) ? User.TYPE_BASIC : tabPane.getSelectionModel().isSelected(1) ?  User.TYPE_ADMIN : User.TYPE_GUEST);
        args.put(UserCreatorController.KEY_EXISTING_USERS, finalList);
        openDetails(editorFxml, args);
    }

    @FXML
    public void initialize(){
       intiList(basicList);
       intiList(adminList);
       intiList(guestList);
    }

    @Override
    public void onDetailsResult(String fxml, Object object) {
        super.onDetailsResult(fxml, object);
        User user = (User) object;
        getTargetList(user).getItems().add(user);
        finalList.add(user);
    }

    @Override
    public void onDetailsResult(String fxml, Object object, int requestCode) {
        super.onDetailsResult(fxml, object, requestCode);
        User user = (User) object;
        int index = finalList.indexOf(user);
        finalList.remove(user);
        finalList.add(index, user);

        int indexInTarget = getTargetList(user).getItems().indexOf(user);
        getTargetList(user).getItems().remove(indexInTarget);
        getTargetList(user).getItems().add(indexInTarget, (User) object);
        if (user.getId() != SQLModel.ID_NOT_IN_DATABASE) {
            updateList.remove(user);
            updateList.add(user);
        }
    }

    private ListView<User> getTargetList(User user){
        return user.getType() == 0 ? basicList : user.getType() == 1 ? adminList : guestList;
    }

}
