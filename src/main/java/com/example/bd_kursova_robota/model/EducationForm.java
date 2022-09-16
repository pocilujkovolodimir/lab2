package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EducationForm extends SQLModel {

    private int id;
    private String title;

    public EducationForm(ResultSet resultSet) throws SQLException {
        id = resultSet.getInt("_id");
        title = resultSet.getString("title");
    }

    public String getTitle() {
        return title;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return title;
    }
}
