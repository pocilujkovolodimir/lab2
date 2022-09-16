package com.example.bd_kursova_robota.model.base;

import java.util.ArrayList;
import java.util.UUID;

public abstract class SQLModel {

    public static final int ID_NOT_IN_DATABASE = -1;

    protected static int getUniqueId() {
        UUID idOne = UUID.randomUUID();
        String string= "" + idOne;
        int uid = string.hashCode();
        String filterString= "" + uid;
        string = filterString.replaceAll("-", "");
        return Integer.parseInt(string);
    }

    public String getInsertSQL(){
        return null;
    }

    public String getUpdateSQL(){
        return null;
    }

    public String getDeleteSQL(){
        return null;
    }

    public abstract long getId();

    protected static String generateInsertSQL(String table, String[] columns, String[] values){
        String sql = String.format("INSERT INTO %1$s(%2$s) VALUES (%3$s)", table, getCommaSeparatedStrings(columns, false), getCommaSeparatedStrings(values, true));
        System.out.println(sql);
        return sql;
    }

    protected static String generateUpdateSQL(String table, String[] columns, String[] values, long id){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < columns.length; i++){
            stringBuilder.append(String.format("%1$s = %2$s", columns[i], addQuotes(values[i].replaceAll("'", "''"))));
            if (i == columns.length - 1) break;
            stringBuilder.append(", ");
        }
        String sql = String.format("UPDATE %1$s SET %2$s WHERE _id = %3$s", table, stringBuilder, id);
        System.out.println(sql);
        return sql;
    }

    public String generateDeleteSQL(String table){
        return generateDeleteSQL(table, getId());
    }

    public ArrayList<String> getDeleteDescendantsSQL(){
       return new ArrayList<>();
    }

    public static String generateDeleteSQL(String table, long id){
        return String.format("DELETE FROM %1$s WHERE _id = %2$s", table, id);
    }

    private static String getCommaSeparatedStrings(String[] strings, boolean addQuotes){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length; i++){
            String str = strings[i].replaceAll("'", "''");
            if (addQuotes) str = addQuotes(str);
            stringBuilder.append(str);
            if (i == strings.length - 1) break;
            stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    private static String addQuotes(String str){
        return String.format("'%1$s'", str);
    }


}
