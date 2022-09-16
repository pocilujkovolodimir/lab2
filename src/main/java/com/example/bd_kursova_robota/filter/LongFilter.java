package com.example.bd_kursova_robota.filter;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class LongFilter extends BaseFilter{

    public LongFilter(TextField textField, int min, int max, int switchLength, Node nextNode) {
        super(textField, min, max, switchLength, nextNode);
    }

    @Override
    public boolean isParsable(String input) {
        try{
            Long.parseLong(input);
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid(String input, long min, long max) {
        float value = Float.parseFloat(input.replace(",", "."));
        return value <= max && value >= min;
    }

}
