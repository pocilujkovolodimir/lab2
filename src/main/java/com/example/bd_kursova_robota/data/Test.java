package com.example.bd_kursova_robota.data;

import com.example.bd_kursova_robota.model.User;
import com.example.bd_kursova_robota.model.base.SQLModel;

import java.sql.Statement;

public class Test {

    public interface GradeOwner{

        int getGrade(); //отримати оцінку
        int[] getGradeBounds(); //отримати шкалу оцінювання (100-200, 0-12, тощо)

    }

    public void notifyChange(SQLModel... sqlModels){
        for (SQLModel sqlModel : sqlModels) {
            try {
                //виконаємо додавання/оновлення даних до бази даних
                boolean inDatabase = sqlModel.getId() != SQLModel.ID_NOT_IN_DATABASE;
                Statement statement = connection.createStatement();
                String sql = inDatabase ? sqlModel.getUpdateSQL() : sqlModel.getInsertSQL();
                statement.execute(sql);
                //занесемо інформацію про дану дію до журналу дій
                String formattedEvent = SQLUtil.prepareString(String.format(notInDatabase ? LogUtil.EVENT_INSERTED : LogUtil.EVENT_UPDATED, currentUser.getLogin()));
                String logSql = String.format("INSERT INTO action_log(title, _sql) VALUES ('%1$s', '%2$s')", formattedEvent, SQLUtil.prepareString(sql));
                executeSQL(logSql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setCurrentUser(User currentUser) {
        //встановимо користувача як поточного
        this.currentUser = currentUser;
        //занесемо інформацію про дану дію до журналу дій
        String formattedEvent = SQLUtil.prepareString(String.format(LogUtil.EVENT_SIGNED_IN, currentUser.getLogin()));
        String logSql = String.format("INSERT INTO action_log(title, _sql) VALUES ('%1$s', '%2$s')",
                formattedEvent, LogUtil.NON_SQL_EVENT);
        executeSQL(logSql);
    }



}
