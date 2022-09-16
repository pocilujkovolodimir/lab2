package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.data.Test;
import com.example.bd_kursova_robota.model.base.SQLModel;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Certificate extends SQLModel implements Test.GradeOwner {

    @Override
    public int getGrade() {
        //завантажуємо дані з бази даних
        return 0;
    }

    @Override
    public int[] getGradeBounds() {
        return new int[]{100, 200};
    }

    private long id;
    private long preId = System.currentTimeMillis();
    private long number;
    private int password;
    private long applicantId;

    public Certificate(ResultSet resultSet) throws SQLException {
        id = resultSet.getLong("_id");
        preId = id;
        applicantId = resultSet.getLong("applicant_id");
        fill(resultSet.getLong("_number"),
            password = resultSet.getInt("_password"));
    }

    public void fill(long number, int password){
        this.preId = id == ID_NOT_IN_DATABASE ? System.currentTimeMillis() : id;
        this.number = number;
        this.password = password;
    }

    public long getPreId() {
        return preId;
    }

    @Override
    public long getId() {
        return id;
    }

    public Certificate(){
        id = ID_NOT_IN_DATABASE;
        fill(number, password);
    }

    public long getNumber() {
        return number;
    }

    public int getPassword() {
        return password;
    }

    public void setApplicantId(long applicantId) {
        this.applicantId = applicantId;
    }

    public long getApplicantId() {
        return applicantId;
    }

    @Override
    public String getInsertSQL() {
        String[] columns = new String[]{"_id", "applicant_id", "_number", "_password"};
        String[] values = new String[]{String.valueOf(getPreId()), String.valueOf(applicantId), String.valueOf(number), String.valueOf(password)};
        return generateInsertSQL("certificate", columns, values);
    }

    @Override
    public String getUpdateSQL() {
        String[] columns = new String[]{"_number", "_password"};
        String[] values = new String[]{String.valueOf(number), String.valueOf(password)};
        return generateUpdateSQL("certificate", columns, values, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return getId() == that.getId() && password == that.password && number == that.number;
    }

    @Override
    public ArrayList<String> getDeleteDescendantsSQL() {
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DELETE FROM certificate_item WHERE certificate_id = " + id);
        return queries;
    }

    @Override
    public String getDeleteSQL() {
        return generateDeleteSQL("certificate");
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), number, password);
    }
}
