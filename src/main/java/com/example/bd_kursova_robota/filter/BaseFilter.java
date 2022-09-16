package com.example.bd_kursova_robota.filter;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public abstract class BaseFilter {

    private TextField textField;
    private long min;
    private long max;
    private int switchLength;
    private Node nextNode;

    public BaseFilter(TextField textField, long min, long max, int switchLength, Node nextNode){
        this.textField = textField;
        this.min = min;
        this.max = max;
        this.switchLength = switchLength;
        this.nextNode = nextNode;
    }

    public BaseFilter attach(){
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

    public boolean check(){
        String input = textField.getText();
        if (input == null || input.isEmpty() || !isParsable(input)) return false;
        return isValid(input, min, max);
    }

    public abstract boolean isParsable(String input);

    public abstract boolean isValid(String input, long min, long max);

}
