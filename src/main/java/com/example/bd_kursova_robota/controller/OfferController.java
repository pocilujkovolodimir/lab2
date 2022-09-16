package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.filter.IntegerFilter;
import com.example.bd_kursova_robota.model.Department;
import com.example.bd_kursova_robota.model.EducationForm;
import com.example.bd_kursova_robota.model.Offer;
import com.example.bd_kursova_robota.model.Specialty;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class OfferController extends BaseController{

    @FXML ChoiceBox<DictionaryModel> department;
    @FXML ChoiceBox<DictionaryModel> specialty;
    @FXML ChoiceBox<DictionaryModel> educationForm;

    @FXML TextField contractPrice;
    @FXML TextField length;
    @FXML TextField bmCount;
    @FXML TextField kmCount;

    @FXML RadioButton statusActive;
    @FXML RadioButton statusInactive;

    private IntegerFilter priceFiler;
    private IntegerFilter lengthFilter;
    private IntegerFilter bmFilter;
    private IntegerFilter kmFilter;

    private Offer offer;

    public OfferController(int id){
        offer = id == -1 ? null : Storage.getInstance().loadOffer(id);
    }

    @FXML
    public void initialize(){
        department.getItems().addAll(Storage.getInstance().getDepartments());
        specialty.getItems().addAll(Storage.getInstance().getSpecialties());
        educationForm.getItems().addAll(Storage.getInstance().getEducationForms());
        priceFiler = new IntegerFilter(contractPrice, 0, 100000, -1, null).attach();
        lengthFilter = new IntegerFilter(length, 0, 500, -1, null).attach();
        bmFilter = new IntegerFilter(bmCount, 0, 100000, -1, null).attach();
        kmFilter = new IntegerFilter(kmCount, 0, 100000, -1, null).attach();
        if (offer != null){
            fill(offer);
        }
    }

    private void fill(Offer offer) {
        department.setValue(Storage.getInstance().getDepartment(offer.getDepartmentId()));
        specialty.setValue(Storage.getInstance().getSpecialty(offer.getSpecialtyId()));
        educationForm.setValue(Storage.getInstance().getEducationForm(offer.getEducationFormId()));
        contractPrice.setText(String.valueOf(offer.getContractPrice()));
        length.setText(String.valueOf(offer.getCourseLength()));
        bmCount.setText(String.valueOf(offer.getBmCount()));
        kmCount.setText(String.valueOf(offer.getKmCount()));
        statusActive.setSelected(offer.getStatus() == 1);
        statusInactive.setSelected(offer.getStatus() == 0);
    }

    @FXML
    public void onSave(){
        if (department.getValue() == null){
            showPopupMessage("Виберіть факультет");
            return;
        }
        if (specialty.getValue() == null){
            showPopupMessage("Виберіть спеціальність");
            return;
        }
        if (educationForm.getValue() == null){
            showPopupMessage("Виберіть форму навчання");
            return;
        }
        if (!priceFiler.check()){
            showPopupMessage("Виберіть вартість навчання");
            return;
        }
        if (!lengthFilter.check()){
            showPopupMessage("Виберіть тривалість навчання");
            return;
        }
        if (!bmFilter.check()){
            showPopupMessage("Виберіть кількість бюдж. місць");
            return;
        }
        if (!kmFilter.check()){
            showPopupMessage("Виберіть кількість контр. місць");
            return;
        }
        if (offer == null){
            offer = new Offer();
        }
        offer.fill((int) department.getValue().getId(), (int) specialty.getValue().getId(), (int) educationForm.getValue().getId(), Integer.parseInt(contractPrice.getText()), Integer.parseInt(length.getText()), Integer.parseInt(bmCount.getText()), Integer.parseInt(kmCount.getText()), statusActive.isSelected() ? 1 : 0);
        Storage.getInstance().notifyChange(offer);
        getDialogListener().onSave();
    }

    @FXML
    public void onCancel(){
        getDialogListener().onCancel();
    }

    @Override
    public Window getPopupWindow() {
        return department.getScene().getWindow();
    }
}
