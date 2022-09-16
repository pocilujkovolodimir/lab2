package com.example.bd_kursova_robota.filter;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class IntegerFilter {

    private TextField textField;
    private int min;
    private int max;
    private int switchLength;
    private Node nextNode;

    public IntegerFilter(TextField textField, int min, int max, int switchLength, Node nextNode){
        this.textField = textField;
        this.min = min;
        this.max = max;
        this.switchLength = switchLength;
        this.nextNode = nextNode;
    }

    public IntegerFilter attach(){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == switchLength){
                if (!check()){
                    textField.setText("");
                }
                else if (nextNode != null){
                    nextNode.requestFocus();
                }
            }
        });
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!check()) textField.setText("");
        });
        return this;
    }

    public void setMax(int max) {
        if (check()){
            int v = Integer.parseInt(textField.getText());
            if (v > max) v = max;
            textField.setText(String.valueOf(v));
        }
        this.max = max;
    }

    public boolean check(){
        String input = textField.getText();
        if (input == null || input.isEmpty()) return false;
        try {
            int v = Integer.parseInt(input);
            if (v < min || v > max) return false;
        }
        catch (Exception e){
            return false;
        }
        return true;
    }


}
