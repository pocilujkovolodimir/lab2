package com.example.bd_kursova_robota.controller;

import javafx.scene.Node;

import java.awt.*;

public class DoneBlock {

    private String message;
    private Node node;

    public DoneBlock(String message){
        this(message, null);
    }

    public Node getNode() {
        return node;
    }

    public DoneBlock(String message, Node node){
        this.message = message;
        this.node = node;
    }

    public String getMessage() {
        return message;
    }

}
