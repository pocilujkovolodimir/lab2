package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Applicant extends SQLModel {

    private float avg; //середній бал атестата

    public float getAvg() {
        return avg;
    }

    private final long id; //ідентифікатор
    private String name; //ім'я
    private String patronymic; //по-батькові
    private String surname; //прізвище
    private String birthDate; //дата народження
    private Region region; //регіон
    private String city; //місто
    private String street; //вулиця
    private int building; //будинок
    private int apt; //квартира

    private boolean isFemale;

    private long preId = System.currentTimeMillis();

    @Override
    public String toString() {
        return name + " " +patronymic + " " + surname +" "+birthDate;
    }

    public Applicant(int id){
        super();
        this.preId = id;
        this.id = id;
    }

    public Applicant(){
        super();
        id = ID_NOT_IN_DATABASE;
    }

    @Override
    public long getId() {
        return id;
    }

    public long getPreId() {
        return preId;
    }

    public Applicant(ResultSet resultSet) throws SQLException{
        id = resultSet.getLong("_id");
        preId = id;
        fill(resultSet.getString("_name"),
                resultSet.getString("patronymic"),
                resultSet.getString("surname"),
                resultSet.getString("date_of_birth"),
                Storage.getInstance().getRegion(resultSet.getInt("region_id")),
                resultSet.getString("city"),
                resultSet.getString("street"),
                resultSet.getInt("building"),
                resultSet.getInt("apartment"),
                resultSet.getFloat("average"),
                resultSet.getBoolean("is_female"));
    }

    public Applicant fill(String name, String patronymic, String surname, String birthDate, Region region, String city, String street, int building, int apt, float avg, boolean isFemale){
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.birthDate = birthDate;
        this.region = region;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apt = apt;
        this.avg = avg;
        this.isFemale = isFemale;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public Region getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getBuilding() {
        return building;
    }

    public int getApt() {
        return apt;
    }


    public boolean isFemale() {
        return isFemale;
    }

    public String getInsertSQL(){
        String[] columns = new String[]{"_id", "_name", "patronymic", "surname", "region_id", "city", "street", "building", "apartment", "is_female", "average", "date_of_birth"};
        String[] values = new String[]{String.valueOf(preId), name, patronymic, surname, String.valueOf(region.getId()), city, street, String.valueOf(building), String.valueOf(apt), String.valueOf(isFemale ? 1 : 0), String.valueOf(avg), birthDate};
        return SQLModel.generateInsertSQL("applicant", columns, values);
    }

    @Override
    public String getUpdateSQL() {
        String[] columns = new String[]{"_name", "patronymic", "surname", "region_id", "city", "street", "building", "apartment", "is_female", "average", "date_of_birth"};
        String[] values = new String[]{name, patronymic, surname, String.valueOf(region.getId()), city, street, String.valueOf(building), String.valueOf(apt), String.valueOf(isFemale ? 1 : 0), String.valueOf(avg), birthDate};
        return SQLModel.generateUpdateSQL("applicant", columns, values, getId());
    }

    @Override
    public ArrayList<String> getDeleteDescendantsSQL() {
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DELETE FROM document WHERE applicant_id = " + id);
        Certificate certificate = Storage.getInstance().loadCertificate(id);
        queries.addAll(certificate.getDeleteDescendantsSQL());
        queries.add(certificate.getDeleteSQL());
        queries.add("DELETE FROM privilege_application WHERE applicant_id = " + id);
        queries.add("DELETE FROM admission_application WHERE applicant_id = " + id);
        return queries;
    }

    @Override
    public String getDeleteSQL() {
        return generateDeleteSQL("applicant");
    }
}
