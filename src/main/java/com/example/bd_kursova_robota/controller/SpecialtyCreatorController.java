package com.example.bd_kursova_robota.controller;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.filter.IntegerFilter;
import com.example.bd_kursova_robota.model.Department;
import com.example.bd_kursova_robota.model.Specialty;
import com.example.bd_kursova_robota.model.SpecialtySubject;
import com.example.bd_kursova_robota.model.Subject;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.util.PhoneNumberFilter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.DefaultStringConverter;

import java.util.ArrayList;
import java.util.HashSet;

public class SpecialtyCreatorController extends DictionaryItemCreatorController{

    @FXML TextField code;

    @FXML ChoiceBox<Subject> subject1;
    @FXML ChoiceBox<Subject> subject2;
    @FXML ChoiceBox<Subject> subject3;

    @FXML Spinner<Double> subject1Coefficient;
    @FXML Spinner<Double> subject2Coefficient;
    @FXML Spinner<Double> subject3Coefficient;

    private IntegerFilter codeFilter;

    private SpecialtySubject[] specialtySubjects = new SpecialtySubject[3];

    @Override
    public void initialize() {
        super.initialize();
        codeFilter = new IntegerFilter(code, 0, 1000, -1, null).attach();
        subject1.getItems().addAll(Storage.getInstance().getSubjects());
        subject2.getItems().addAll(Storage.getInstance().getSubjects());
        subject3.getItems().addAll(Storage.getInstance().getSubjects());
        subject1Coefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1, 0.1, 0.1));
        subject2Coefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1, 0.1, 0.1));
        subject3Coefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1, 0.1, 0.1));
    }

    @Override
    public void onFill(DictionaryModel model) {
        super.onFill(model);
        Specialty specialty = ((Specialty) model);
        code.setText(String.valueOf(specialty.getCode()));
        specialtySubjects = Storage.getInstance().getSpecialtySubjects(specialty.getPreId());

        subject1.setValue(Storage.getInstance().getSubject(specialtySubjects[0].getSubjectId()));
        subject2.setValue(Storage.getInstance().getSubject(specialtySubjects[1].getSubjectId()));
        subject3.setValue(Storage.getInstance().getSubject(specialtySubjects[2].getSubjectId()));

        subject1Coefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1, specialtySubjects[0].getCoefficient(), 0.1));
        subject2Coefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1, specialtySubjects[1].getCoefficient(), 0.1));
        subject3Coefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1, specialtySubjects[2].getCoefficient(), 0.1));
    }

    @Override
    public Object getResult() {
        Specialty result = getModel() == null ? new Specialty(name.getText(), Integer.parseInt(code.getText())) : ((Specialty) getModel().setName(name.getText())).setCode(Integer.parseInt(code.getText()));
        if (getModel() == null) {
            SpecialtySubject[] specialtySubjects = new SpecialtySubject[3];
            specialtySubjects[0] = new SpecialtySubject(subject1.getValue(), result, subject1Coefficient.getValue().floatValue());
            specialtySubjects[1] = new SpecialtySubject(subject2.getValue(), result, subject2Coefficient.getValue().floatValue());
            specialtySubjects[2] = new SpecialtySubject(subject3.getValue(), result, subject3Coefficient.getValue().floatValue());
            Storage.getInstance().notifyChange(specialtySubjects);
        }
        else{
            specialtySubjects[0].setSubjectId(subject1.getValue().getId()).setCoefficient(subject1Coefficient.getValue().floatValue());
            specialtySubjects[1].setSubjectId(subject2.getValue().getId()).setCoefficient(subject2Coefficient.getValue().floatValue());
            specialtySubjects[2].setSubjectId(subject3.getValue().getId()).setCoefficient(subject3Coefficient.getValue().floatValue());
            Storage.getInstance().notifyChange(specialtySubjects);
        }
        return result;
    }

    @Override
    public ArrayList<DoneBlock> getDoneBlocks() {
        ArrayList<DoneBlock> doneBlocks = new ArrayList<>();
        if (!codeFilter.check()){
            doneBlocks.add(new DoneBlock("Введіть код спеціальності"));
        }
        if (name.getText().isEmpty()){
            doneBlocks.add(new DoneBlock("Введіть назву спеціальності"));
        }
        if (getUniqueSubjectCount() != 3){
            doneBlocks.add(new DoneBlock("Виберіть три різних предмета"));
        }
        float sum = subject1Coefficient.getValue().floatValue() + subject2Coefficient.getValue().floatValue() + subject3Coefficient.getValue().floatValue();
        if (sum != 1){
            doneBlocks.add(new DoneBlock("Сума коефіцієнтів повинна дорівнювати одиниці"));
        }
        return doneBlocks;
    }

    private int getUniqueSubjectCount() {
        HashSet<Subject> subjects = new HashSet<>();
        subjects.add(subject1.getValue());
        subjects.add(subject2.getValue());
        subjects.add(subject3.getValue());
        return subjects.size();
    }
}
