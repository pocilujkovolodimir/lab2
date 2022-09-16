package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Document extends SQLModel {

    int quantity; //кількість копій (наприклад, фотокартки, зазвичай, приймають в кількості 6-ти шт.)
    DocumentType documentType; //вид документу

    public DocumentType getDocumentType() {
        return documentType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    String number;
    long applicantId;
    int id;




    public Document(ResultSet resultSet) throws SQLException {
        id = resultSet.getInt("_id");
        documentType = Storage.getInstance().getDocumentType(resultSet.getInt("document_type_id"));
        number = resultSet.getString("_number");
        quantity = resultSet.getInt("quantity");
        applicantId = resultSet.getLong("applicant_id");
    }

    @Override
    public String getInsertSQL() {
        String[] columns = new String[]{"document_type_id", "applicant_id", "_number", "quantity"};
        String[] values = new String[]{String.valueOf(documentType.getId()), String.valueOf(applicantId), String.valueOf(number), String.valueOf(quantity)};
        return generateInsertSQL("document", columns, values);
    }

    @Override
    public String getDeleteSQL() {
        return "DELETE FROM document WHERE _id = " + id;
    }

    @Override
    public long getId() {
        return id;
    }

    public Document(DocumentType documentType, String number, int quantity){
        this.id = ID_NOT_IN_DATABASE;
        this.documentType = documentType;
        this.number = number;
        this.quantity = quantity;
    }

    public void setApplicantId(long applicantId) {
        this.applicantId = applicantId;
    }


    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return quantity == document.quantity && documentType.equals(document.documentType) && number.equals(document.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentType, number, quantity);
    }
}
