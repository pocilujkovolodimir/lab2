package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.model.Document;
import com.example.bd_kursova_robota.model.Score;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class ScoreRowController {

    @FXML Label scoreSubject;
    @FXML Label scoreValue;

    private Score score;

    public void setScore(Score score){
        this.score = score;
        scoreSubject.setText(score.getSubjectTitle());
        scoreValue.setText(String.valueOf(score.getScore()));
    }

}
