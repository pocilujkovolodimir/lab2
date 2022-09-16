package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrivilegeApplication extends SQLModel {

    public static final String[] COLUMNS = new String[]{"privilege_id", "applicant_id"};

    private int id = ID_NOT_IN_DATABASE;
    private DictionaryModel privilege;
    private long applicantId;

    @Override
    public String toString() {
        return String.format("Пільга, ІД студ.: %1$s, %2$d", privilege.toString(), applicantId);
    }

    public DictionaryModel getPrivilege() {
        return privilege;
    }

    public PrivilegeApplication(ResultSet resultSet) throws SQLException {
        id = resultSet.getInt("privilege_application._id");
        applicantId = resultSet.getLong("privilege_application.applicant_id");
        privilege = new DictionaryModel("privilege", resultSet);
    }

    public PrivilegeApplication(DictionaryModel privilege, long applicantId){
        this.privilege = privilege;
        this.applicantId = applicantId;
    }

    public void setPrivilege(DictionaryModel privilege) {
        this.privilege = privilege;
    }

    @Override
    public String getDeleteSQL() {
        return generateDeleteSQL("privilege_application");
    }

    @Override
    public String getInsertSQL() {
        return generateInsertSQL("privilege_application", COLUMNS, new String[]{String.valueOf(privilege.getId()), String.valueOf(applicantId)});
    }

    @Override
    public String getUpdateSQL() {
        return generateUpdateSQL("privilege_application", COLUMNS, new String[]{String.valueOf(privilege.getId()), String.valueOf(applicantId)}, getId());
    }

    @Override
    public long getId() {
        return id;
    }
}
