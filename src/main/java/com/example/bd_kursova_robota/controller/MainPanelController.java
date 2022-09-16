package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.data.UserUtil;
import com.example.bd_kursova_robota.model.Row;
import com.example.bd_kursova_robota.model.User;
import com.example.bd_kursova_robota.model.view.*;
import com.example.bd_kursova_robota.util.NavigationUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class MainPanelController extends BaseController{

    @FXML TableView<Row> tableView;
    @FXML TreeView<Destination> treeView;
    @FXML StackPane filterParent;

    @FXML Menu dictionariesMenu;
    @FXML Menu updateMenu;
    @FXML Menu usersMenu;


    private Destination currentDestination = null;

    @FXML
    public void initialize(){
        User currentUser = Storage.getInstance().getCurrentUser();
        usersMenu.setVisible(currentUser.getType() == User.TYPE_ADMIN);
        updateMenu.setVisible(currentUser.getType() == User.TYPE_BASIC || currentUser.getType() == User.TYPE_ADMIN);
        dictionariesMenu.setVisible(currentUser.getType() == User.TYPE_BASIC || currentUser.getType() == User.TYPE_ADMIN);

        TreeItem<Destination> rootItem = new TreeItem<>(new FormalDestination("Вступна комісія"));
        rootItem.setExpanded(true);
        rootItem.getChildren().addAll(UserUtil.getDestinations(currentUser));

        treeView.setRoot(rootItem);
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Destination>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Destination>> observable, TreeItem<Destination> oldValue, TreeItem<Destination> newValue) {
                clearCurrentDestination();
                if (!(newValue.getValue() instanceof FormalDestination)){
                    setDestination(newValue.getValue());
                }
            }
        });
        tableView.setPlaceholder(new Label("(Пусто)"));
    }

    private void clearCurrentDestination() {
        tableView.getItems().clear();
        tableView.getColumns().clear();
        tableView.setContextMenu(null);
        filterParent.getChildren().clear();
    }

    private void setDestination(Destination value) {
        value.load(tableView);
        currentDestination = value;
        if (value instanceof TableDestination){
            Node node = ((TableDestination) value).getFilterLayout();
            if (node != null){
                filterParent.getChildren().add(node);
                filterParent.setVisible(true);
            }
            else{
                filterParent.setVisible(false);
            }
        }
        else{
            filterParent.setVisible(false);
        }
        filterParent.setManaged(filterParent.isVisible());
    }

    @FXML
    public void openDepartmentsEditor(){
        NavigationUtil.openDepartmentsEditor();
    }

    @FXML
    public void openSpecialtiesEditor(){
        NavigationUtil.openSpecialtiesEditor();
    }

    @FXML
    public void openEducationFormsEditor(){
        NavigationUtil.openEducationFormEditor();
    }

    @FXML
    public void openPrivilegeEditor(){
        NavigationUtil.openPrivilegeEditor();
    }

    @FXML
    public void openDocumentTypesEditor(){
        NavigationUtil.openDictionaryEditor("document_type", "Види документів");
    }

    @FXML
    public void openSubjectsEditor(){
        NavigationUtil.openSubjectEditor();
    }

    @FXML
    public void openRegionEditor(){
        NavigationUtil.openRegionEditor();
    }

    @FXML
    public void openUsersEditor(){
        NavigationUtil.openUsersEditor();
    }

    @FXML
    public void commentApplicant(){
        Storage.getInstance().executeSQL("UPDATE applicant\n" +
                "SET _comment = '';\n");
        Storage.getInstance().executeSQL("UPDATE applicant a\n" +
                "SET _comment = 'Має найвищий середній бал ЗНО'\n" +
                "WHERE (\n" +
                "\tSELECT SUM(certificate_item.score) / 3\n" +
                "    FROM certificate_item\n" +
                "    JOIN certificate ON certificate_item.certificate_id = certificate._id\n" +
                "    WHERE certificate.applicant_id = a._id\n" +
                "    )\n" +
                "    >=\n" +
                "    ALL (\n" +
                "    SELECT SUM(certificate_item.score) / 3\n" +
                "    FROM certificate_item\n" +
                "    GROUP BY certificate_item.certificate_id\n" +
                "    );\n");
        currentDestination.reload();
    }

    @FXML
    public void commentOffer(){
        Storage.getInstance().executeSQL("UPDATE offer\n" +
                "SET _comment = ''\n");
        Storage.getInstance().executeSQL("UPDATE offer o\n" +
                "SET _comment = 'Найпопулярніша вступна пропозиція'\n" +
                "WHERE (\n" +
                "\tSELECT COUNT(admission_application._id)\n" +
                "    FROM admission_application \n" +
                "    WHERE admission_application.offer_id = o._id\n" +
                ")\n" +
                ">=\n" +
                "ALL(\n" +
                "\tSELECT COUNT(admission_application._id)\n" +
                "    FROM admission_application \n" +
                "    GROUP BY admission_application.offer_id\n" +
                ")\n");
        currentDestination.reload();
    }

}
