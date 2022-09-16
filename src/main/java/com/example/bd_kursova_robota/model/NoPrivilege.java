package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.model.base.DictionaryModel;

public class NoPrivilege extends DictionaryModel {

    public NoPrivilege() {
        super("Немає", "");
    }

    @Override
    public long getId() {
        return -1;
    }



    @Override
    public String toString() {
        return "Немає";
    }
}
