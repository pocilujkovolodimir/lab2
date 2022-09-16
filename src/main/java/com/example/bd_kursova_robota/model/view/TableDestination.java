package com.example.bd_kursova_robota.model.view;

import com.example.bd_kursova_robota.HelloApplication;
import com.example.bd_kursova_robota.controller.TableFilterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.util.ArrayList;

public class TableDestination extends Destination{

    private final String filterFxml;
    private ArrayList<String> filterWhereClauses = null;
    private TableFilterController filterController = null;

    private final TableFilterController.FilterListener filterListener = whereClauses -> {
        this.filterWhereClauses = whereClauses;
        reload();
    };

    public TableDestination(String title, String sql, String filterFxml, String[] excludedColumns) {
        super(title, sql, excludedColumns);
        this.filterFxml = filterFxml;
    }

    public Node getFilterLayout(){
        if (filterFxml == null) return null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(filterFxml));
            Node node = fxmlLoader.load();
            filterController = fxmlLoader.getController();
            filterController.setFilterListener(filterListener);
            return node;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void onFilterLayoutDetached(){
        filterWhereClauses = null;
        filterController.setFilterListener(null);
        filterController = null;
    }

    @Override
    public String onModifySQL(String sql) {
        if (filterWhereClauses == null || filterWhereClauses.size() == 0) return sql;
        StringBuilder stringBuilder = new StringBuilder(sql);
        stringBuilder.append(" WHERE ");
        for (int i = 0; i < filterWhereClauses.size(); i++){
            stringBuilder.append(filterWhereClauses.get(i));
            if (i != filterWhereClauses.size() - 1){
                stringBuilder.append(" AND ");
            }
        }
        String newSQL = stringBuilder.toString();
        System.out.println(newSQL);
        return newSQL;
    }
}
