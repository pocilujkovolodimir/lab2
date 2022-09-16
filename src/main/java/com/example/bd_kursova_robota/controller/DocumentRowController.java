package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.model.Document;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DocumentRowController {

    @FXML Label documentName;
    @FXML Label documentNumber;
    @FXML Label documentQuantity;

    private Document document;

    public void setDocument(Document document){
        this.document = document;
        documentName.setText(document.getDocumentType().getName());
        documentNumber.setText(document.getNumber().isEmpty() ? "(немає)" : document.getNumber());
        documentQuantity.setText(String.valueOf(document.getQuantity()));
    }

}
