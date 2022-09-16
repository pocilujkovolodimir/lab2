package com.example.bd_kursova_robota.data;

public class SQLUtil {
    public static String prepareString(String event) {
        return event.replaceAll("'", "''");
    }
}
