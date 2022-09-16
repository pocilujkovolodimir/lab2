package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Specialty extends DictionaryModel {

    private static final String TABLE_NAME = "specialty";

    private int code;
    private int preId = SQLModel.getUniqueId();

    public int getCode() {
        return code;
    }

    public Specialty(ResultSet resultSet) throws SQLException {
        super(TABLE_NAME, resultSet);
        code = resultSet.getInt("_code");
        preId = resultSet.getInt("_id");
    }

    public Specialty(String name, int code) {
        super(name, TABLE_NAME);
        this.code = code;
    }

    @Override
    public String getInsertSQL() {
        return generateInsertSQL(TABLE_NAME, new String[]{"_id", "title", "_code"}, new String[]{String.valueOf(preId), getName(), String.valueOf(code)});
    }

    @Override
    public String getUpdateSQL() {
        return generateUpdateSQL(TABLE_NAME, new String[]{"title", "_code"}, new String[]{getName(), String.valueOf(code)}, getId());
    }



    @Override
    public ArrayList<String> getDeleteDescendantsSQL() {
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DELETE FROM specialty_subject WHERE specialty_id = " + getId());
        return queries;
    }

    public Specialty setCode(int code) {
        this.code = code;
        return this;
    }

    public int getPreId() {
        return preId;
    }

}
