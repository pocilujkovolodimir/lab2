package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.cell.ApplicationCell;
import com.example.bd_kursova_robota.cell.DocumentCell;
import com.example.bd_kursova_robota.cell.ScoreCell;
import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.filter.BaseFilter;
import com.example.bd_kursova_robota.filter.FloatFilter;
import com.example.bd_kursova_robota.filter.IntegerFilter;
import com.example.bd_kursova_robota.filter.LongFilter;
import com.example.bd_kursova_robota.model.*;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.util.ListUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ApplicantDialogController extends DetailsSupportController {

    private Pair<ArrayList<Document>, ArrayList<Document>> pair;

    //tab 1
    @FXML TextField name;
    @FXML TextField patronymic;
    @FXML TextField surname;

    @FXML TextField birthDay;
    @FXML TextField birthMonth;
    @FXML TextField birthYear;

    @FXML RadioButton sxFemale;
    @FXML RadioButton sxMale;

    @FXML ChoiceBox<Region> addressRegion;
    @FXML TextField addressCity;
    @FXML TextField addressStreet;
    @FXML TextField addressBuilding;
    @FXML TextField addressApt;
    @FXML TextField avg;

    @FXML TextField certificateNumber;
    @FXML TextField certificatePassword;

    @FXML Button buttonOk;
    @FXML Button buttonCancel;
    @FXML Label nameLabel;
    @FXML TabPane tabPane;

    //tab 3
    @FXML ListView<Document> list;

    //tab 2
    @FXML ListView<Score> list_eie;

    //tab 4
    @FXML ListView<AdmApplication> listAdm;
    @FXML ChoiceBox<DictionaryModel> privilege;


    private IntegerFilter birthDayFilter;
    private IntegerFilter birthMonthFilter;
    private IntegerFilter birthYearFilter;
    private IntegerFilter addressBuildingFilter;
    private IntegerFilter addressAptFilter;
    private BaseFilter avgFilter;
    private BaseFilter certificateNumberFilter;
    private BaseFilter certificatePasswordFilter;

    private Applicant applicant;
    private Certificate certificate;

    private ArrayList<Document> initialDocuments;
    private ArrayList<Document> documents;

    private ArrayList<Score> initialScores;
    private ArrayList<Score> scores;

    private ArrayList<AdmApplication> initialApplications;
    private ArrayList<AdmApplication> applications;

    private PrivilegeApplication privilegeApplication;

    public ApplicantDialogController(long id){
        this.applicant = id == -1 ? null : Storage.getInstance().loadApplicant(id);
        initialDocuments = applicant == null ? new ArrayList<>() : Storage.getInstance().loadDocuments(applicant.getId());
        documents = new ArrayList<>(initialDocuments);
        if (applicant != null){
            certificate = Storage.getInstance().loadCertificate(applicant.getId());
        }
        initialScores = certificate == null ? new ArrayList<>() : Storage.getInstance().loadScores(certificate.getId());
        scores = new ArrayList<>(initialScores);

        initialApplications = applicant == null ? new ArrayList<>() : Storage.getInstance().loadApplications(applicant.getId());
        applications = new ArrayList<>(initialApplications);

        privilegeApplication = applicant == null ? null : Storage.getInstance().loadPrivilegeApplication(applicant.getId());
    }


    @FXML
    public void onSave(){
        boolean birthDayPassed = birthDayFilter.check();
        boolean birthMonthPassed = birthMonthFilter.check();
        boolean birthYearPassed = birthYearFilter.check();
        boolean birthDatePassed = birthDayFilter.check() && birthMonthFilter.check() && birthYearFilter.check();

        boolean namePassed = !name.getText().isEmpty();
        boolean patronymicPassed = !patronymic.getText().isEmpty();
        boolean surnamePassed = !surname.getText().isEmpty();

        boolean cityPassed = !addressCity.getText().isEmpty();
        boolean streetPassed = !addressStreet.getText().isEmpty();
        boolean buildingPassed = addressBuildingFilter.check();
        boolean aptPassed = addressAptFilter.check();

        boolean certificateNumberPassed = certificateNumberFilter.check();
        boolean certificatePasswordPassed = certificatePasswordFilter.check();

        boolean avgPassed = avgFilter.check();

        if (!namePassed){
            tabPane.getSelectionModel().select(0);
            showPopupMessage("Введіть ім'я", name);
            return;
        }

        if (!patronymicPassed){
            tabPane.getSelectionModel().select(0);
            showPopupMessage("Введіть по-батькові", patronymic);
            return;
        }

        if (!surnamePassed){
            tabPane.getSelectionModel().select(0);
            showPopupMessage("Введіть прізвище", surname);
            return;
        }

        if (!birthDatePassed){
            tabPane.getSelectionModel().select(0);
            String subject = (!birthDayPassed ? "день" : !birthMonthPassed ? "місяць" : "рік");
            showPopupMessage("Введіть " + subject, buttonCancel.getScene().getWindow(), !birthDayPassed ? birthDay : !birthMonthPassed ? birthMonth : !birthYearPassed ? birthYear : null);
            return;
        }

        if (!cityPassed){
            tabPane.getSelectionModel().select(0);
            showPopupMessage("Введіть місто", addressCity);
            return;
        }

        if (!streetPassed){
            tabPane.getSelectionModel().select(0);
            showPopupMessage("Введіть вулицю", addressStreet);
            return;
        }

        if (!buildingPassed){
            tabPane.getSelectionModel().select(0);
            showPopupMessage("Введіть номер будинку", addressBuilding);
            return;
        }

        if (!aptPassed){
            tabPane.getSelectionModel().select(0);
            showPopupMessage("Введіть номер квартири", addressApt);
            return;
        }

        if (!avgPassed){
            tabPane.getSelectionModel().select(0);
            showPopupMessage("Введіть середній бал", avg);
            return;
        }

        if (!certificateNumberPassed){
            tabPane.getSelectionModel().select(1);
            showPopupMessage("Введіть номер сертифікату ЗНО", certificateNumber);
            return;
        }

        if (!certificatePasswordPassed){
            tabPane.getSelectionModel().select(1);
            showPopupMessage("Введіть пароль сертифікату ЗНО", certificatePassword);
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(birthDay.getText() + "-" + birthMonth.getText() + "-" +birthYear.getText());
        }
        catch (ParseException e) {
            showPopupMessage("Дата не існує!", buttonCancel.getScene().getWindow());
            return;
        }

        if (applicant == null){

            this.applicant =  new Applicant();
        }

        if (certificate == null){
            this.certificate = new Certificate();
        }

        certificate.fill(Long.parseLong(certificateNumber.getText()), Integer.parseInt(certificatePassword.getText()));
        certificate.setApplicantId(applicant.getPreId());

        applicant.fill(name.getText(),
                patronymic.getText(),
                surname.getText(),
                birthYear.getText() + "-" + birthMonth.getText() + "-" + birthDay.getText(),
                addressRegion.getValue(),
                addressCity.getText(),
                addressStreet.getText(),
                Integer.parseInt(addressBuilding.getText()),
                Integer.parseInt(addressApt.getText()),
                Float.parseFloat(avg.getText().replaceAll(",", ".")),
                sxFemale.isSelected());

        Storage.getInstance().notifyChange(applicant, certificate);
        Pair<ArrayList<Document>, ArrayList<Document>> pair = ListUtil.getAddedDeletedScores(initialDocuments, documents);
        for (Document document: pair.getKey()){
            document.setApplicantId(applicant.getPreId());
            Storage.getInstance().notifyChange(document);
        }
        for (Document document: pair.getValue()){
            Storage.getInstance().removeModel(document);
        }

        Pair<ArrayList<Score>, ArrayList<Score>> pair2 = ListUtil.getAddedDeletedScores(initialScores, scores);
        for (Score score: pair2.getKey()){
            score.setCertificateId(certificate.getPreId());
            Storage.getInstance().notifyChange(score);
        }
        for (Score score: pair2.getValue()){
            Storage.getInstance().removeModel(score);
        }

        Pair<ArrayList<AdmApplication>, ArrayList<AdmApplication>> pair3 = ListUtil.getAddedDeletedScores(initialApplications, applications);
        for (AdmApplication admApplication: pair3.getKey()){
            admApplication.setApplicantId(applicant.getPreId());
            Storage.getInstance().notifyChange(admApplication);
        }
        for (AdmApplication admApplication: pair3.getValue()){
            Storage.getInstance().removeModel(admApplication);
        }

        if (privilege.getValue() instanceof NoPrivilege && privilegeApplication != null){
            System.out.println("DESELECTED PRIVILEGE");
            Storage.getInstance().removeModel(privilegeApplication);
        }
        else if (!(privilege.getValue() instanceof NoPrivilege)) {
            System.out.println("SELECTED PRIVILEGE");
            if (privilegeApplication == null) {
                privilegeApplication = new PrivilegeApplication(privilege.getValue(), applicant.getPreId());
            }
            else {
                privilegeApplication.setPrivilege(privilege.getValue());
            }
            Storage.getInstance().notifyChange(privilegeApplication);
        }

        getDialogListener().onSave();
    }

    @FXML
    public void onCancel(){
        if (getDialogListener() != null) getDialogListener().onCancel();
    }

    @FXML
    public void initialize(){
        double rem = javafx.scene.text.Font.getDefault().getSize();
        setPopupMarginTop(0.3 * rem);
        birthDayFilter = new IntegerFilter(birthDay, 1, 31, 2, birthMonth).attach();
        birthMonthFilter = new IntegerFilter(birthMonth, 1, 12, 2, birthYear).attach();
        birthYearFilter = new IntegerFilter(birthYear, 1900, Calendar.getInstance().get(Calendar.YEAR), 4, birthDay).attach();
        addressBuildingFilter = new IntegerFilter(addressBuilding, 0, Integer.MAX_VALUE, -1, null).attach();
        addressAptFilter = new IntegerFilter(addressApt, 0, Integer.MAX_VALUE, -1, null).attach();
        avgFilter = new FloatFilter(avg, 2, 12, -1, null).attach();
        addressRegion.getItems().addAll(Storage.getInstance().getRegions());
        addressRegion.getSelectionModel().selectFirst();
        certificateNumberFilter = new LongFilter(certificateNumber, 1000000, 9999999, -1, null);
        certificatePasswordFilter = new LongFilter(certificatePassword, 1000, 9999, -1, null);

        initializeTab2();
        initializeTab3();
        initializeApplicationsTab();

        if (applicant != null){
            fillCertificateTab();
            fillTab1(applicant);
            fillTab3();
            fillTab4();
        }
    }

    private void fillTab1(Applicant applicant) {
        name.setText(applicant.getName());
        patronymic.setText(applicant.getPatronymic());
        surname.setText(applicant.getSurname());

        sxFemale.setSelected(applicant.isFemale());
        sxMale.setSelected(!applicant.isFemale());

        addressRegion.setValue(applicant.getRegion());
        addressStreet.setText(applicant.getStreet());
        addressCity.setText(applicant.getCity());
        addressBuilding.setText(String.valueOf(applicant.getBuilding()));
        addressApt.setText(String.valueOf(applicant.getApt()));

        avg.setText(String.valueOf(applicant.getAvg()));

        String[] dateParts = applicant.getBirthDate().split("-");
        birthDay.setText(dateParts[2]);
        birthMonth.setText(dateParts[1]);
        birthYear.setText(dateParts[0]);
    }

    private void fillTab3(){
        list.getItems().addAll(documents);
    }

    private void fillTab4(){
        listAdm.getItems().addAll(applications);
        if (privilegeApplication == null){
            privilege.getSelectionModel().selectFirst();
        }
        else{
            privilege.getSelectionModel().select(privilegeApplication.getPrivilege());
        }
    }

    private void initializeTab3() {
        list.setCellFactory(param -> new DocumentCell());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Видалити");
        deleteMenuItem.setOnAction(event -> {
            contextMenu.hide();
            ObservableList<Document> selected = list.getSelectionModel().getSelectedItems();
            for (Document document : selected){
                documents.remove(document);
                list.getItems().remove(document);
            }
        });
        MenuItem addMenuItem = new MenuItem("Додати");
        addMenuItem.setOnAction(event -> {
            contextMenu.hide();
            openDetails("details_document_creator.fxml", null);
        });
        contextMenu.getItems().add(deleteMenuItem);
        contextMenu.getItems().add(addMenuItem);
        list.setContextMenu(contextMenu);
    }

    private void initializeApplicationsTab() {
        listAdm.setCellFactory(param -> new ApplicationCell());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Видалити");
        deleteMenuItem.setOnAction(event -> {
            contextMenu.hide();
            ObservableList<AdmApplication> selected = listAdm.getSelectionModel().getSelectedItems();
            for (AdmApplication application : selected){
                applications.remove(application);
                listAdm.getItems().remove(application);
            }
        });
        MenuItem addMenuItem = new MenuItem("Додати");
        addMenuItem.setOnAction(event -> {
            contextMenu.hide();
            HashMap<Integer, Object> args = new HashMap<>();
            args.put(ApplicationCreatorController.KEY_SCORES, scores);
            openDetails("details_application_creator.fxml", args);
        });
        contextMenu.getItems().add(deleteMenuItem);
        contextMenu.getItems().add(addMenuItem);
        listAdm.setContextMenu(contextMenu);

        privilege.getItems().add(new NoPrivilege());
        privilege.getItems().addAll(Storage.getInstance().getPrivileges());
    }

    private void initializeTab2() {
        list_eie.setCellFactory(param -> new ScoreCell());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Видалити");
        deleteMenuItem.setOnAction(event -> {
            contextMenu.hide();
            ObservableList<Score> selected = list_eie.getSelectionModel().getSelectedItems();
            for (Score score : selected){
                scores.remove(score);
                list_eie.getItems().remove(score);
            }
        });
        MenuItem addMenuItem = new MenuItem("Додати");
        addMenuItem.setOnAction(event -> {
            contextMenu.hide();
            openDetails("details_score_creator.fxml", null);
        });
        contextMenu.getItems().add(deleteMenuItem);
        contextMenu.getItems().add(addMenuItem);
        list_eie.setContextMenu(contextMenu);
        list_eie.refresh();
    }

    private void fillCertificateTab(){
        if (certificate == null) return;
        certificateNumber.setText(String.valueOf(certificate.getNumber()));
        certificatePassword.setText(String.valueOf(certificate.getPassword()));
        list_eie.getItems().addAll(initialScores);
    }

    public void onDetailsResult(String fxml, Object object){
        if (fxml.equals("details_document_creator.fxml")){
            documents.add((Document) object);
            list.getItems().add((Document) object);
        }
        else if (fxml.equals("details_score_creator.fxml")){
            scores.add((Score) object);
            list_eie.getItems().add((Score) object);
        }
        else if (fxml.equals("details_application_creator.fxml")){
            applications.add((AdmApplication) object);
            listAdm.getItems().add((AdmApplication) object);
        }
    }

}
