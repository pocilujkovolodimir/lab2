package com.example.bd_kursova_robota.data;

import com.example.bd_kursova_robota.model.*;
import com.example.bd_kursova_robota.model.base.DictionaryModel;
import com.example.bd_kursova_robota.model.base.SQLModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class Storage {
    private static Storage storage;

    public static Storage getInstance() {
        if (storage == null){
            storage = new Storage();
        }
        return storage;
    }

    private Storage(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/kursova_robota", "root", "0969985273iq");
        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
    }
    public static final String QUERY_APPLICABLE_OFFERS = "SELECT * FROM offer JOIN education_form ON education_form._id = offer.education_form_id JOIN department ON department._id = offer.department_id JOIN specialty ON specialty._id = offer.specialty_id JOIN specialty_subject ON specialty_subject.specialty_id = specialty._id AND (specialty_subject.eie_subject_id = %1$d OR specialty_subject.eie_subject_id = %2$d OR specialty_subject.eie_subject_id = %3$d) WHERE offer._status = 1 GROUP BY offer._id HAVING COUNT(*) = 3";

    private Connection connection;

    private ArrayList<DocumentType> documentTypes = null;
    private ArrayList<Subject> subjects = null;
    private ArrayList<Region> regions = null;
    private ArrayList<DictionaryModel> departments = null;
    private ArrayList<DictionaryModel> specialties = null;
    private ArrayList<DictionaryModel> educationForms = null;
    private ArrayList<DictionaryModel> privileges = null;
    private User currentUser;





    public void cacheDirectories(){
        documentTypes = null;
        regions = null;
        subjects = null;
        specialties = null;
        departments = null;
        educationForms = null;
        privileges = null;
        getDocumentTypes();
        getRegions();
        getSubjects();
        getSpecialties();
        getDepartments();
        getEducationForms();
        getPrivileges();
    }

    public void executeSQL(String sql){
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DictionaryModel> getPrivileges() {
        if (privileges == null) {
            privileges = loadTableAsDictionary("privilege");
        }
        return privileges;
    }

    public ArrayList<Subject> getSubjects(){
        if (subjects == null) {
            subjects = new ArrayList<>();
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM eie_subject");
                while (resultSet.next()) {
                    subjects.add(new Subject(resultSet.getInt("_id"), resultSet.getString("title")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subjects;
    }

    public ArrayList<DocumentType> getDocumentTypes(){
        if (documentTypes == null) {
            documentTypes = new ArrayList<>();
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM document_type");
                while (resultSet.next()) {
                    documentTypes.add(new DocumentType(resultSet.getInt("_id"), resultSet.getString("title")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return documentTypes;
    }

    public ArrayList<Region> getRegions(){
        if (regions == null) {
            regions = new ArrayList<>();
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM region");
                while (resultSet.next()) {
                    regions.add(new Region(resultSet.getInt("_id"), resultSet.getString("title")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return regions;
    }

    public Region getRegion(int id){
        for (Region region : getRegions()){
            if (region.getId() == id) return region;
        }
        return null;
    }

    public void notifyChange(SQLModel ... sqlModels){
        for (SQLModel sqlModel : sqlModels) {
            try {
                boolean notInDatabase = sqlModel.getId() == SQLModel.ID_NOT_IN_DATABASE;
                Statement statement = connection.createStatement();
                String sql = notInDatabase ? sqlModel.getInsertSQL() : sqlModel.getUpdateSQL();
                statement.execute(sql);
                log(notInDatabase ? LogUtil.EVENT_INSERTED : LogUtil.EVENT_UPDATED, sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean removeModel(SQLModel sqlModel){
        try {
            String deleteSql = sqlModel.getDeleteSQL();
            ArrayList<String> deleteDescendantsSql = sqlModel.getDeleteDescendantsSQL();
            System.out.println(deleteSql);
            Statement statement = connection.createStatement();
            for (String sql : deleteDescendantsSql) {
                statement.execute(sql);
                log(LogUtil.EVENT_DELETED, deleteSql);
                System.out.println(sql);
            }
            statement.execute(deleteSql);
            log(LogUtil.EVENT_DELETED, deleteSql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int askBeforeRemoving(SQLModel sqlModel, String alertHeader, String alertBody){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.CANCEL, ButtonType.YES);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertBody);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            return removeModel(sqlModel) ? 0 : 1;
        }
        else{
            return 2;
        }
    }

    public void log(String event, String sql){
        String formattedEvent = SQLUtil.prepareString(String.format(event, currentUser.getLogin()));
        String logSql = String.format("INSERT INTO action_log(title, _sql) VALUES ('%1$s', '%2$s')", formattedEvent, SQLUtil.prepareString(sql));
        executeSQL(logSql);
    }

    public Applicant loadApplicant(long id){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM applicant WHERE _id = " + id);
            if (!resultSet.next()){
                System.out.println("EMPTY!");
                return null;
            }
            System.out.println("FOUND!");
            return new Applicant(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet universalQuery(String sql){
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Certificate loadCertificate(long applicantId){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM certificate WHERE applicant_id = " + applicantId);
            if (!resultSet.next()){
                return null;
            }
            System.out.println("FOUND!");
            return new Certificate(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivilegeApplication loadPrivilegeApplication(long applicantId){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM privilege_application JOIN privilege ON privilege_application.privilege_id = privilege._id WHERE applicant_id = " + applicantId);
            if (!resultSet.next()){
                return null;
            }
            System.out.println("FOUND!");
            return new PrivilegeApplication(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Document> loadDocuments(long applicantId){
        ArrayList<Document> documents = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM document WHERE applicant_id = " + applicantId);
            while (resultSet.next()){
                documents.add(new Document(resultSet));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }

    public DocumentType getDocumentType(int id){
        for(DocumentType documentType : getDocumentTypes()){
            if (documentType.getId() == id) return documentType;
        }
        return null;
    }

    public ArrayList<Score> loadScores(long certificateId) {
        ArrayList<Score> scores = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM certificate_item JOIN eie_subject ON certificate_item.eie_subject_id = eie_subject._id AND certificate_id = " + certificateId);
            while (resultSet.next()){
                scores.add(new Score(resultSet));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return scores;
    }

    public ArrayList<AdmApplication> loadApplications(long applicantId) {
        ArrayList<AdmApplication> applications = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM admission_application JOIN offer ON admission_application.offer_id = offer._id JOIN specialty ON offer.specialty_id = specialty._id WHERE applicant_id = " + applicantId);
            while (resultSet.next()){
                applications.add(new AdmApplication(resultSet));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return applications;
    }

    public ArrayList<DictionaryModel> loadTableAsDictionary(String table) {
        ArrayList<DictionaryModel> dictionary = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
            while (resultSet.next()){
                dictionary.add(new DictionaryModel(table, resultSet));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    public ArrayList<DictionaryModel> getDepartments() {
        if (departments == null) {
            departments = new ArrayList<>();
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM " + "department");
                while (resultSet.next()) {
                    departments.add(new Department(resultSet));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return departments;
    }

    public ArrayList<DictionaryModel> getSpecialties() {
        if (specialties == null) {
            specialties = new ArrayList<>();
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM " + "specialty");
                while (resultSet.next()) {
                    specialties.add(new Specialty(resultSet));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return specialties;
    }

    public SpecialtySubject[] getSpecialtySubjects(int specialtyId) {
        SpecialtySubject[] specialtySubjects = new SpecialtySubject[3];
        int i = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM specialty_subject WHERE specialty_id = " + specialtyId);
            while (resultSet.next()){
                specialtySubjects[i] = new SpecialtySubject(resultSet);
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return i == 3 ? specialtySubjects : null;
    }

    public Subject getSubject(long id) {
        System.out.println("Looking for subject: "+id);
        for (Subject subject: getSubjects()){
            if (id == subject.getId()){
                return subject;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + "program_user");
            while (resultSet.next()){
                users.add(new User(resultSet));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public User findUser(String login) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM program_user WHERE login = '" + login + "'");
            if (!resultSet.next()){
                return null;
            }
            return new User(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<DictionaryModel> getEducationForms() {
        if (educationForms == null) {
            educationForms = loadTableAsDictionary("education_form");
        }
        return educationForms;
    }

    public DictionaryModel getDepartment(int departmentId) {
        for (DictionaryModel department : getDepartments()){
            if (department.getId() == departmentId){
                return department;
            }
        }
        return null;
    }

    public DictionaryModel getEducationForm(int id) {
        for (DictionaryModel educationForm : getEducationForms()){
            if (educationForm.getId() == id){
                return educationForm;
            }
        }
        return null;
    }

    public DictionaryModel getSpecialty(int id) {
        for (DictionaryModel specialty : getSpecialties()){
            if (specialty.getId() == id){
                return specialty;
            }
        }
        return null;
    }

    public Collection<? extends Offer> getOffers(ArrayList<Score> eieScores) {
        if (eieScores.size() != 3) return new ArrayList<>();

        ArrayList<Offer> offers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_APPLICABLE_OFFERS, eieScores.get(0).getSubjectId(), eieScores.get(1).getSubjectId(), eieScores.get(2).getSubjectId()));
            while (resultSet.next()){
                offers.add(new Offer(resultSet));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return offers;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        log(LogUtil.EVENT_SIGNED_IN, "–");
    }

    public Offer loadOffer(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM offer WHERE _id = " + id);
            if (!resultSet.next()){
                return null;
            }
            return new Offer(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
