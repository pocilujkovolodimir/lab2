package com.example.bd_kursova_robota.util;

import com.example.bd_kursova_robota.model.Score;
import javafx.util.Pair;

import java.util.ArrayList;

public class ListUtil {

    public static <K> Pair<ArrayList<K>, ArrayList<K>> getAddedDeletedScores(ArrayList<K> old, ArrayList<K> _new){
        ArrayList<K> added = new ArrayList<>();
        ArrayList<K> deleted = new ArrayList<>();
        for (K k: old){
            if (!_new.contains(k)){
                deleted.add(k);
            }
        }
        for (K k: _new){
            if (!old.contains(k)){
                added.add(k);
            }
        }
        return new Pair<>(added, deleted);
    }

}
