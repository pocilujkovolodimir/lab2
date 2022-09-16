package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class User extends SQLModel {

    public static final int TYPE_BASIC = 0;
    public static final int TYPE_ADMIN = 1;
    public static final int TYPE_GUEST = 2;

    private int id = ID_NOT_IN_DATABASE;
    private String login;
    private int passwordHash;
    private int type = TYPE_ADMIN;


    @Override
    public String toString() {
        return login;
    }

    public User(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("_id");
        this.login = resultSet.getString("login");
        this.passwordHash = resultSet.getInt("password_hash");
        this.type = resultSet.getInt("_type");
    }

    public User(String login, String password, int type){
        this.login = login;
        this.passwordHash = password.hashCode();
        this.type = type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getInsertSQL() {
        return generateInsertSQL("program_user", new String[]{"login", "password_hash", "_type"}, new String[]{login, String.valueOf(passwordHash), String.valueOf(type)});
    }

    @Override
    public String getUpdateSQL() {
        return generateUpdateSQL("program_user", new String[]{"login", "password_hash"}, new String[]{login, String.valueOf(passwordHash)}, id);
    }

    @Override
    public String getDeleteSQL() {
        return generateDeleteSQL("program_user");
    }

    public String getLogin() {
        return login;
    }

    public int getType() {
        return type;
    }

    public boolean isCorrectPassword(String input){
        return input.hashCode() == passwordHash;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public User setPassword(String password) {
        if (password.isEmpty()) return this;
        this.passwordHash = password.hashCode();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return id == ID_NOT_IN_DATABASE ? Objects.equals(login, that.login) : id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }

}
