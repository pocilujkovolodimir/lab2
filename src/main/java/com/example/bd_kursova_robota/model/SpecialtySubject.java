package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecialtySubject extends SQLModel {

    private int id = ID_NOT_IN_DATABASE;
    private int subjectId;
    private int specialtyId;
    private float coefficient;

    @Override
    public String toString() {
        return String.format("ІД предмета, ІД спец: %1$d, %2$d", subjectId, specialtyId);
    }

    public SpecialtySubject(ResultSet resultSet) throws SQLException {
        id = resultSet.getInt("_id");
        subjectId = resultSet.getInt("eie_subject_id");
        specialtyId = resultSet.getInt("specialty_id");
        coefficient = resultSet.getFloat("coefficient");
        System.out.println("SUBJECT ID" + subjectId);
    }

    public SpecialtySubject setSubjectId(int subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public SpecialtySubject setCoefficient(float coefficient) {
        this.coefficient = coefficient;
        return this;
    }

    public float getCoefficient() {
        return coefficient;
    }

    public SpecialtySubject(Subject subject, Specialty specialty, float coefficient){
        this.subjectId = subject.getId();
        this.specialtyId = specialty.getPreId();
        this.coefficient = coefficient;
    }


    @Override
    public String getInsertSQL() {
        return generateInsertSQL("specialty_subject", new String[]{"eie_subject_id", "specialty_id", "coefficient"}, new String[]{String.valueOf(subjectId), String.valueOf(specialtyId), String.valueOf(coefficient)});
    }

    @Override
    public String getUpdateSQL() {
        return generateUpdateSQL("specialty_subject", new String[]{"eie_subject_id", "specialty_id", "coefficient"}, new String[]{String.valueOf(subjectId), String.valueOf(specialtyId), String.valueOf(coefficient)}, getId());
    }

    @Override
    public long getId() {
        return id;
    }

    public long getSubjectId() {
        return subjectId;
    }
}
