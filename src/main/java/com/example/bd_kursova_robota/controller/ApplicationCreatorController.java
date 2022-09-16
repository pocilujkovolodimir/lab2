package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.cell.DocumentCell;
import com.example.bd_kursova_robota.cell.OfferCell;
import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.filter.IntegerFilter;
import com.example.bd_kursova_robota.model.AdmApplication;
import com.example.bd_kursova_robota.model.Offer;
import com.example.bd_kursova_robota.model.Score;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationCreatorController extends DetailsController{

    public static final Integer KEY_SCORES = 1;

    @FXML TextField year;
    @FXML RadioButton statusAccepted;
    @FXML RadioButton statusNotAccepted;
    @FXML ListView<Offer> list;

    private ArrayList<Score> scores;

    private IntegerFilter yearFilter;

    @FXML
    public void initialize(){
        list.setCellFactory(param -> new OfferCell());
        yearFilter = new IntegerFilter(year, 1990, 2050, -1, null).attach();
    }

    @Override
    public String getTitle() {
        return "Нова заява";
    }

    @Override
    public boolean canReturnResult() {
        return true;
    }

    @Override
    public ArrayList<DoneBlock> getDoneBlocks() {
        ArrayList<DoneBlock> doneBlocks = new ArrayList<>();
        if (!yearFilter.check()){
            doneBlocks.add(new DoneBlock("Введіть рік"));
        }
        if (getSelectedOffer() == null){
            doneBlocks.add(new DoneBlock("Оберіть вступну пропозицію"));
        }
        return doneBlocks;
    }

    @Override
    public Object getResult() {
        Offer offer = getSelectedOffer();
        int year = Integer.parseInt(this.year.getText());
        int status = statusAccepted.isSelected() ? 1 : 0;
        return new AdmApplication((int) offer.getId(), year, status).setSpecialtyName(offer.getSpecialtyName());
    }

    private Offer getSelectedOffer() {
        Offer offer = null;
        for (Offer offer1 : list.getSelectionModel().getSelectedItems()){
            offer = offer1;
            break;
        }
        return offer;
    }

    @Override
    public void onApplyArguments(HashMap<Integer, Object> args) {
        super.onApplyArguments(args);
        scores = (ArrayList<Score>) args.get(KEY_SCORES);
        list.getItems().addAll(Storage.getInstance().getOffers(scores));
    }
}
