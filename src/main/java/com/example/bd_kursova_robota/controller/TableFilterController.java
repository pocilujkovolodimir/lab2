package com.example.bd_kursova_robota.controller;

import java.util.ArrayList;

public class TableFilterController {

    public interface FilterListener{
        void onFilterSet(ArrayList<String> whereClauses);
    }

    private FilterListener filterListener;

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    public FilterListener getFilterListener() {
        return filterListener;
    }
}
