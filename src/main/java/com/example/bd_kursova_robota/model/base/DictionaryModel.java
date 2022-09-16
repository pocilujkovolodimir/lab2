package com.example.bd_kursova_robota.model.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DictionaryModel extends SQLModel{

    private int id;
    private String name;
    private String table;

    public void setTable(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public DictionaryModel(String table, ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt(table + "._id");
        this.name = resultSet.getString(table + ".title");
        this.table = table;
    }

    public DictionaryModel setName(String name) {
        this.name = name;
        return this;
    }

    public DictionaryModel(String name, String table){
        this.id = ID_NOT_IN_DATABASE;
        this.name = name;
    }

    @Override
    public String getInsertSQL() {
        return generateInsertSQL(table, new String[]{"title"}, new String[]{name});
    }

    @Override
    public String getUpdateSQL() {
        return generateUpdateSQL(table, new String[]{"title"}, new String[]{name}, id);
    }

    @Override
    public String getDeleteSQL() {
        return generateDeleteSQL(table);
    }

    @Override
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryModel that = (DictionaryModel) o;
        return id == ID_NOT_IN_DATABASE ? Objects.equals(name, that.name) : id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return name;
    }
}
