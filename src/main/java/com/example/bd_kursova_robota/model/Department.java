package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.DictionaryModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Department extends DictionaryModel {

    private String phone;
    private String email;

    public Department setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Department setEmail(String email) {
        this.email = email;
        return this;
    }

    public Department(ResultSet resultSet) throws SQLException {
        super("department", resultSet);
        phone = resultSet.getString("phone");
        email = resultSet.getString("email");
    }

    public Department(String name, String phone, String email) {
        super(name, "department");
        this.phone = phone;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getInsertSQL() {
        return generateInsertSQL(getTable(), new String[]{"title", "phone", "email"}, new String[]{getName(), phone, email});
    }

    @Override
    public String getUpdateSQL() {
        return generateUpdateSQL(getTable(), new String[]{"title", "phone", "email"}, new String[]{getName(), phone, email}, getId());
    }

}
