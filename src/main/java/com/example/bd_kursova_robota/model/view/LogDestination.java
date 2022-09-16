package com.example.bd_kursova_robota.model.view;

import com.example.bd_kursova_robota.model.view.TableDestination;

public class LogDestination extends TableDestination {

    public static final String QUERY_VIEW_LOG = "SELECT _time AS 'Час', title AS 'Опис дії', _sql AS 'Запит SQL' FROM action_log ORDER BY _time DESC";

    public LogDestination() {
        super("Журнал дій", QUERY_VIEW_LOG, null, null);
    }
}
