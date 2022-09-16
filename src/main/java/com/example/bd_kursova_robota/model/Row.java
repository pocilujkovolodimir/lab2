package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

public class Row extends SQLModel {

    public HashMap<String, String> values = new HashMap<>();

    String table;

    public void setTable(String table) {
        this.table = table;
    }

    public Row(ResultSet resultSet, ResultSetMetaData metaData) throws SQLException {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String value =  resultSet.getString(i);
            values.put(metaData.getColumnLabel(i), value == null ? "" : value);
        }
    }

    public String get(String key){
        return values.get(key);
    }

    @Override
    public long getId() {
        return Long.parseLong(values.get("_id"));
    }

    @Override
    public String getDeleteSQL() {
        return generateDeleteSQL(table);
    }
}
