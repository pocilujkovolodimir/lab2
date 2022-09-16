package com.example.bd_kursova_robota.model;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Score extends SQLModel {

    private int id = ID_NOT_IN_DATABASE;
    private long certificateId;
    private long subjectId;
    private String subjectTitle;
    private int score;

    @Override
    public String toString() {
        return String.format("Предмет, ІД сертиф, бал: %1$s, %2$d, %3$d", subjectTitle, certificateId, score);
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setCertificateId(long certificateId) {
        this.certificateId = certificateId;
    }

    public Score(ResultSet resultSet) throws SQLException {
        id = resultSet.getInt("_id");
        certificateId = resultSet.getLong("certificate_id");
        subjectId = resultSet.getInt("eie_subject_id");
        subjectTitle = resultSet.getString("title");
        score = resultSet.getInt("score");
    }

    public Score(Subject subject, int score){
        this.subjectId = subject.getId();
        this.subjectTitle = subject.getName();
        this.score = score;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public int getScore() {
        return score;
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getInsertSQL() {
        String[] columns = new String[]{"certificate_id", "eie_subject_id", "score"};
        String[] values = new String[]{String.valueOf(certificateId), String.valueOf(subjectId), String.valueOf(score)};
        return generateInsertSQL("certificate_item", columns, values);
    }

    @Override
    public String getDeleteSQL() {
        return generateDeleteSQL("certificate_item");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score1 = (Score) o;
        return id == score1.id && certificateId == score1.certificateId && subjectId == score1.subjectId && score == score1.score && subjectTitle.equals(score1.subjectTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, certificateId, subjectId, subjectTitle, score);
    }
}
