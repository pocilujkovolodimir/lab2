package com.example.bd_kursova_robota.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DetailsController {

    public ArrayList<DoneBlock> getDoneBlocks() {
        return null;
    }

    public interface OKButtonBridge{
        void setOKEnabled(boolean enabled);
    }

    private OKButtonBridge okButtonBridge;
    private int requestCode = 0;

    public void setOkButtonBridge(OKButtonBridge okButtonBridge) {
        this.okButtonBridge = okButtonBridge;
    }

    public void setDoneEnabled(boolean enabled){
        if (okButtonBridge != null){
            okButtonBridge.setOKEnabled(enabled);
        }
    }

    public void onApplyArguments(HashMap<Integer, Object> args){

    }

    public int getRequestCode(){
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public Object getResult(){
        return null;
    }

    public boolean canReturnResult(){
        return false;
    }

    public String getTitle(){
        return null;
    }

}
