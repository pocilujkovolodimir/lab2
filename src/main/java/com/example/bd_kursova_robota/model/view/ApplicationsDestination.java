package com.example.bd_kursova_robota.model.view;

import com.example.bd_kursova_robota.data.LogUtil;
import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Row;
import com.example.bd_kursova_robota.util.NavigationUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public class ApplicationsDestination extends TableDestination{

    private static final String QUERY_VIEW_APPLICATIONS = "SELECT admission_application._id, admission_application._year AS 'Рік', admission_application._status AS 'Статус', applicant._name AS 'Ім''я', applicant.patronymic AS 'По-батькові', applicant.surname AS 'Прізвище', specialty.title AS 'Спеціальність', department.title AS 'Факультет', education_form.title AS 'Форма нав.' FROM admission_application JOIN offer ON admission_application.offer_id = offer._id JOIN department ON department._id = offer.department_id JOIN specialty ON specialty._id = offer.specialty_id JOIN education_form ON education_form._id = offer.education_form_id JOIN applicant ON admission_application.applicant_id = applicant._id";

    public ApplicationsDestination() {
        super("Заяви на вступ", QUERY_VIEW_APPLICATIONS, "filter_applications.fxml", new String[]{"_id"});
        addTransformer("Статус", str -> str.equals("1") ? "Прийнята" : "Не прийнята");

    }

    @Override
    public int loadData() throws SQLException {
        int res = super.loadData();
        if (res == 0){
            for (Row row : getRows()){
                row.setTable("admission_application");
            }
        }
        return res;
    }

    public ContextMenu getContextMenu(TableView<Row> tableView){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem acceptMenuItem = new MenuItem("Прийняти");
        acceptMenuItem.setOnAction(event -> {
            contextMenu.hide();
            Row selectedRow = getSelectedRow();
            if (selectedRow == null) return;

            setStatus(selectedRow, 1);
            tableView.refresh();
        });
        MenuItem denyMenuItem = new MenuItem("Відхилити");
        denyMenuItem.setOnAction(event -> {
            contextMenu.hide();
            Row selectedRow = getSelectedRow();
            if (selectedRow == null) return;

            setStatus(selectedRow, 0);
            tableView.refresh();
        });
        MenuItem deleteMenuItem = new MenuItem("Видалити");
        deleteMenuItem.setOnAction(event -> {
            contextMenu.hide();
            Row selectedRow = getSelectedRow();
            if (selectedRow == null) return;
            Storage.getInstance().askBeforeRemoving(selectedRow, "Видалити заяву?", "Цю дію не можна відмінити");
            reload();
        });
        contextMenu.getItems().addAll(acceptMenuItem, denyMenuItem, new SeparatorMenuItem(), deleteMenuItem);
        return contextMenu;
    }

    private void setStatus(Row selectedRow, int status) {
        selectedRow.values.put("Статус", String.valueOf(status));
        String sql = String.format("UPDATE admission_application SET _status = %1$d WHERE _id = %2$d", status, selectedRow.getId());
        Storage.getInstance().executeSQL(sql);
        Storage.getInstance().log(LogUtil.EVENT_UPDATED, sql);
    }

    private Row getSelectedRow() {
        ObservableList<Row> selected = getTableView().getSelectionModel().getSelectedItems();
        Row selectedRow = null;
        for (Row row : selected){
            selectedRow = row;
            break;
        }
        return selectedRow;
    }
}
