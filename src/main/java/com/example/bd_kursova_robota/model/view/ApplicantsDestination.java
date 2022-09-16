package com.example.bd_kursova_robota.model.view;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Row;
import com.example.bd_kursova_robota.util.NavigationUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class ApplicantsDestination extends TableDestination{

    private static final String QUERY_VIEW_APPLICANTS = "SELECT applicant._id, _name AS 'І''мя', patronymic AS 'По-батькові', surname AS 'Прізвище', date_of_birth AS 'Дата нар.', region.title AS 'Область', city AS 'Місто', street AS 'Вулиця', average AS 'Серед. бал', is_female AS 'Стать', applicant._comment AS 'Коментар' FROM applicant JOIN region ON region._id = applicant.region_id";

    public ApplicantsDestination() {
        super("Абітурієнти", QUERY_VIEW_APPLICANTS, "filter_applicants.fxml", new String[]{"_id"});
        addTransformer("Стать", new Transformer() {
            @Override
            public String transform(String str) {
                return str.equals("1") ? "Жіноча" : "Чоловіча";
            }
        });
    }

    public ContextMenu getContextMenu(TableView<Row> tableView){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Видалити");
        deleteMenuItem.setOnAction(event -> {
            contextMenu.hide();

            ObservableList<Row> selected = tableView.getSelectionModel().getSelectedItems();
            Row selectedRow = null;
            for (Row row : selected){
                selectedRow = row;
                break;
            }
            if (selectedRow == null) return;

            Storage.getInstance().askBeforeRemoving(Storage.getInstance().loadApplicant(selectedRow.getId()), "Видалити абітурієнта?", "Цю дію не можна відмінити");
            load(tableView);

        });
        MenuItem editMenuItem = new MenuItem("Редагувати");
        editMenuItem.setOnAction(event -> {
            contextMenu.hide();

            ObservableList<Row> selected = tableView.getSelectionModel().getSelectedItems();
            Row selectedRow = null;
            for (Row row : selected){
                selectedRow = row;
                break;
            }
            if (selectedRow == null) return;

            NavigationUtil.openApplicantEditor(null, Long.parseLong(selectedRow.get("_id")), ()->{
                reload();
            });
        });
        MenuItem addMenuItem = new MenuItem("Додати");
        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NavigationUtil.openApplicantEditor(null, -1, ()->{
                    reload();
                });
            }
        });
        contextMenu.getItems().addAll(addMenuItem, editMenuItem, deleteMenuItem);
        return contextMenu;
    }
}
