package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Offer extends SQLModel {

    private int id = ID_NOT_IN_DATABASE;
    private int departmentId;
    private int specialtyId;
    private int educationFormId;
    private int contractPrice;
    private int courseLength;
    private int bmCount;
    private int kmCount;
    private int status;

    private String departmentName;
    private String specialtyName;
    private String educationFormName;
    private String comment;

    public int getStatus() {
        return status;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    public int getEducationFormId() {
        return educationFormId;
    }

    public int getContractPrice() {
        return contractPrice;
    }

    public int getCourseLength() {
        return courseLength;
    }

    public int getBmCount() {
        return bmCount;
    }

    public int getKmCount() {
        return kmCount;
    }

    public Offer(){

    }

    public Offer(ResultSet resultSet) throws SQLException {
        id = resultSet.getInt("offer._id");
        fill(resultSet.getInt("offer.department_id"),
                resultSet.getInt("offer.specialty_id"),
                resultSet.getInt("offer.education_form_id"),
                resultSet.getInt("offer.contract_price"),
                resultSet.getInt("offer.course_length"),
                resultSet.getInt("offer.bm_count"),
                resultSet.getInt("offer.km_count"),
                resultSet.getInt("offer._status"));
        try{
            departmentName = resultSet.getString("department.title");
            specialtyName = resultSet.getString("specialty.title");
            educationFormName = resultSet.getString("education_form.title");
            comment = resultSet.getString("comment");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public String getEducationFormName() {
        return educationFormName;
    }

    public void fill(int departmentId, int specialtyId, int educationFormId, int contractPrice, int courseLength, int bmCount, int kmCount, int status){
        this.status = status;
        this.departmentId = departmentId;
        this.specialtyId = specialtyId;
        this.educationFormId = educationFormId;
        this.contractPrice = contractPrice;
        this.courseLength = courseLength;
        this.bmCount = bmCount;
        this.kmCount = kmCount;
    }

    @Override
    public String getInsertSQL() {
        String[] columns = new String[]{"department_id", "specialty_id", "education_form_id", "contract_price", "course_length", "bm_count", "km_count", "_status"};
        String[] values = new String[]{String.valueOf(departmentId), String.valueOf(specialtyId), String.valueOf(educationFormId), String.valueOf(contractPrice), String.valueOf(courseLength), String.valueOf(bmCount), String.valueOf(kmCount), String.valueOf(status)};
        return generateInsertSQL("offer", columns, values);
    }

    @Override
    public String getUpdateSQL() {
        String[] columns = new String[]{"department_id", "specialty_id", "education_form_id", "contract_price", "course_length", "bm_count", "km_count", "_status"};
        String[] values = new String[]{String.valueOf(departmentId), String.valueOf(specialtyId), String.valueOf(educationFormId), String.valueOf(contractPrice), String.valueOf(courseLength), String.valueOf(bmCount), String.valueOf(kmCount), String.valueOf(status)};
        return generateUpdateSQL("offer", columns, values, id);
    }

    @Override
    public String getDeleteSQL() {
        return generateDeleteSQL("offer");
    }

    @Override
    public long getId() {
        return id;
    }
}
