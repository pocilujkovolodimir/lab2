package com.example.bd_kursova_robota.model.view;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Row;
import com.example.bd_kursova_robota.util.NavigationUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

public class OffersDestination extends TableDestination{

    private static final String QUERY_VIEW_OFFERS = "SELECT offer._id, specialty.title AS 'Спеціальність', department.title AS 'Факультет', education_form.title AS 'Форма нав.', offer.bm_count AS 'БМ', offer.km_count AS 'КМ', offer.contract_price AS 'Вартість контр.', offer._status AS 'Статус', offer.course_length AS 'Тривалість нав.', offer._comment AS 'Коментар' FROM offer JOIN department ON department._id = offer.department_id JOIN specialty ON specialty._id = offer.specialty_id JOIN education_form ON education_form._id = offer.education_form_id ";

    public OffersDestination() {
        super("Вступні пропозиції", QUERY_VIEW_OFFERS, "filter_offers.fxml", new String[]{"_id"});
        addTransformer("Статус", str -> str.equals("1") ? "Активна" : "Неактивна");
        addTransformer("Тривалість нав.", str -> {
            int allMonths = Integer.parseInt(str);
            int years = allMonths  / 12;
            int months = allMonths % 12;
            return String.format("%1$d р. %2$d м.", years, months);
        });
    }

    public ContextMenu getContextMenu(TableView<Row> tableView){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Додати");
        addMenuItem.setOnAction(event -> {
            NavigationUtil.openOfferEditor(-1, () -> reload());
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

            NavigationUtil.openOfferEditor(Integer.parseInt(selectedRow.get("_id")), ()->{
                reload();
            });
        });
        contextMenu.getItems().addAll(editMenuItem, addMenuItem);
        return contextMenu;
    }
}
