package com.example.bd_kursova_robota.model.view;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Row;
import com.example.bd_kursova_robota.util.NavigationUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

public class Destination {

    interface Transformer{
        public String transform(String str);
    }

    private final String title;
    private String sql;
    private ArrayList<Row> rows;
    private ArrayList<String> columnLabels;
    private ArrayList<String> columnNames;
    private final ArrayList<String> excludedColumns;
    private TableView<Row> tableView;

    private HashMap<String, Transformer> transformers = new HashMap<>();

    public void addTransformer(String columnLabel, Transformer transformer){
        transformers.put(columnLabel, transformer);
    }

    public TableView<Row> getTableView() {
        return tableView;
    }

    public ArrayList<String> getColumnLabels() {
        return columnLabels;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public int getColumnCount(){
        return columnLabels.size();
    }

    @Override
    public String toString() {
        return title;
    }

    public Destination(String title, String sql, String[] excludedColumns){
        this.title = title;
        this.sql = sql;
        this.excludedColumns = new ArrayList<>();
        if (excludedColumns != null) {
            Collections.addAll(this.excludedColumns, excludedColumns);
        }
    }

    public void reload(){
        load(tableView);
    }

    public void load(TableView<Row> tableView){
        try {
            this.tableView = tableView;
            if (loadData() != 0) return;
            tableView.getItems().clear();
            tableView.getColumns().clear();
            for (int i = 0; i < columnNames.size(); i++){
                String columnName = columnNames.get(i);
                String columnLabel = columnLabels.get(i);
                if (excludedColumns.contains(columnName) || excludedColumns.contains(columnLabel)){
                    continue;
                }
                TableColumn<Row, String> column = new TableColumn<>(columnLabel.replace("Kilkist", "Кількість").replace("Suma", "Кількість одиниць").replace("average", "Середній бал"));
                column.setCellValueFactory(param ->{
                    String value = param.getValue().get(columnLabel);
                    Transformer transformer = transformers.get(columnLabel);
                    if (transformer != null){
                        value = transformer.transform(value);
                    }
                    return new SimpleStringProperty(value);
                });
                tableView.getColumns().add(column);
            }
            tableView.getItems().addAll(rows);
            tableView.setContextMenu(getContextMenu(tableView));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isExcluded(String column){
        return excludedColumns.contains(column);
    }

    public int getExcludedCount(){
        return excludedColumns.size();
    }

    public ContextMenu getContextMenu(TableView<Row> tableView) {
        return null;
    }

    public int loadData() throws SQLException {
        rows = new ArrayList<>();
        columnNames = new ArrayList<>();
        columnLabels = new ArrayList<>();
        String sql = this.sql;
        while (sql.indexOf('$') != -1){
            String placeholder = sql.substring(sql.indexOf('$'), sql.indexOf('%') + 1);
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Введіть аргумент запиту");
            dialog.setHeaderText(placeholder.substring(1, placeholder.length() - 1));
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                sql = sql.replace(placeholder, result.get().replaceAll("'", "''"));
            }
            else{
                return -1;
            }
        }
        sql = onModifySQL(sql);
        ResultSet resultSet = Storage.getInstance().universalQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        while(resultSet.next()){
            rows.add(new Row(resultSet, metaData));
        }
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnLabels.add(metaData.getColumnLabel(i));
            columnNames.add(metaData.getColumnName(i));
        }
        return 0;
    }

    public String onModifySQL(String sql) {
        return sql;
    }

}
