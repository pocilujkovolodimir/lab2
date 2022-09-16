package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.model.Document;
import com.example.bd_kursova_robota.model.Offer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OfferRowController {

    @FXML Label primary;
    @FXML Label secondary;
    @FXML Label tertiary;


    public void setOffer(Offer offer){
        primary.setText(offer.getSpecialtyName());
        secondary.setText(offer.getDepartmentName() + " • " + offer.getEducationFormName());
        tertiary.setText(String.format("БМ/КМ: %1$d/%2$d • Вартість контракту: %3$d грн.", offer.getBmCount(), offer.getKmCount(), offer.getContractPrice()));
    }

}
