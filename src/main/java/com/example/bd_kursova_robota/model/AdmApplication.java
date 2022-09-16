package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdmApplication extends SQLModel {

    public static final String[] COLUMNS = new String[]{"applicant_id", "offer_id", "_year", "_status"};

    private int id = ID_NOT_IN_DATABASE;
    private long applicantId;
    private int offerId;
    private int year;
    private int status;

    public int getYear() {
        return year;
    }

    private String specialtyName;

    public AdmApplication(ResultSet resultSet) throws SQLException{
        this.id = resultSet.getInt("_id");
        this.applicantId = resultSet.getLong("applicant_id");
        this.offerId = resultSet.getInt("offer_id");
        this.year = resultSet.getInt("_year");
        this.status = resultSet.getInt("_status");
        this.specialtyName = resultSet.getString("specialty.title");
    }

    public int getStatus() {
        return status;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public AdmApplication setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
        return this;
    }

    public AdmApplication(int offerId, int year, int status){
        this.offerId = offerId;
        this.year = year;
        this.status = status;
    }

    public long getApplicantId() {
        return applicantId;
    }

    @Override
    public String getInsertSQL() {
        String[] values = new String[]{String.valueOf(applicantId), String.valueOf(offerId), String.valueOf(year), String.valueOf(status)};
        return generateInsertSQL("admission_application", COLUMNS, values);
    }

    @Override
    public String getUpdateSQL() {
        String[] values = new String[]{String.valueOf(applicantId), String.valueOf(offerId), String.valueOf(year), String.valueOf(status)};
        return generateUpdateSQL("admission_application", COLUMNS, values, id);
    }

    @Override
    public String getDeleteSQL() {
        return generateDeleteSQL("admission_application");
    }

    public void setApplicantId(long applicantId) {
        this.applicantId = applicantId;
    }

    @Override
    public long getId() {
        return id;
    }



}
