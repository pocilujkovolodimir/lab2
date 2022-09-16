package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.cell.DictionaryItemCell;
import com.example.bd_kursova_robota.cell.DocumentCell;
import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Document;
import com.example.bd_kursova_robota.model.Score;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.model.base.SQLModel;
import com.example.bd_kursova_robota.util.ListUtil;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryEditorController extends DetailsSupportController{

    public interface DataLoader{
        public ArrayList<DictionaryModel> load(String table);
    }

    @FXML ListView<DictionaryModel> list;

    private ArrayList<DictionaryModel> initialList;
    private ArrayList<DictionaryModel> finalList;
    private ArrayList<DictionaryModel> updateList = new ArrayList<>();
    private String table;
    private String editorFxml = "details_dictionary_item_creator.fxml";

    public void setEditorFxml(String editorFxml) {
        this.editorFxml = editorFxml;
    }

    public DictionaryEditorController(String table){
        this(table, null);
    }

    public DictionaryEditorController(String table, DataLoader dataLoader){
        if (dataLoader == null){
            dataLoader = table1 -> Storage.getInstance().loadTableAsDictionary(table1);
        }
        initialList = dataLoader.load(table);
        finalList = new ArrayList<>(initialList);
        this.table = table;
    }

    @FXML
    public void onCancel(){
        getDialogListener().onCancel();
    }

    @FXML
    public void onSave(){
        Pair<ArrayList<DictionaryModel>, ArrayList<DictionaryModel>> pair = ListUtil.getAddedDeletedScores(initialList, finalList);
        for (DictionaryModel dictionaryModel: pair.getKey()){
            dictionaryModel.setTable(table);
            Storage.getInstance().notifyChange(dictionaryModel);
        }
        for (DictionaryModel dictionaryModel: pair.getValue()){
            if (!Storage.getInstance().removeModel(dictionaryModel)){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Не вдалося видалити елемент \"" + dictionaryModel.getName()+"\".");
                alert.setHeaderText("Перевірка цілісності");
                alert.showAndWait();
            };
        }
        for (DictionaryModel dictionaryModel : updateList){
            Storage.getInstance().notifyChange(dictionaryModel);
        }
        getDialogListener().onSave();
    }

    @FXML
    public void onAdd(){
        openDetails(editorFxml, null);
    }

    @FXML
    public void initialize(){
        list.setCellFactory(param -> new DictionaryItemCell());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Видалити");
        deleteMenuItem.setOnAction(event -> {
            contextMenu.hide();
            ObservableList<DictionaryModel> selected = list.getSelectionModel().getSelectedItems();
            for (DictionaryModel model : selected){
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
        list.getItems().addAll(initialList);
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) return;
                DictionaryModel dictionaryModel = null;

                for (DictionaryModel dictionaryModel1 : list.getSelectionModel().getSelectedItems()){
                    dictionaryModel = dictionaryModel1;
                    break;
                }

                if (dictionaryModel == null) return;

                HashMap<Integer, Object> args = new HashMap<>();
                args.put(DictionaryItemCreatorController.KEY_INITIAL_MODEL, dictionaryModel);
                openDetails(editorFxml, args, 0);
            }
        });
    }

    @Override
    public void onDetailsResult(String fxml, Object object) {
        super.onDetailsResult(fxml, object);
        list.getItems().add((DictionaryModel) object);
        finalList.add((DictionaryModel) object);
    }

    @Override
    public void onDetailsResult(String fxml, Object object, int requestCode) {
        super.onDetailsResult(fxml, object, requestCode);
        System.out.println("REQUESTED_CODE");
        int index = finalList.indexOf((DictionaryModel) object);
        finalList.remove((DictionaryModel) object);
        finalList.add(index, (DictionaryModel) object);
        list.getItems().remove(index);
        list.getItems().add(index, (DictionaryModel) object);
        if (((DictionaryModel) object).getId() != SQLModel.ID_NOT_IN_DATABASE) {
            updateList.remove(object);
            updateList.add((DictionaryModel) object);
        }
    }

}
