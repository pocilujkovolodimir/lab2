package com.example.bd_kursova_robota.data;

import com.example.bd_kursova_robota.model.User;
import com.example.bd_kursova_robota.model.view.*;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class UserUtil {

    public static ArrayList<TreeItem<Destination>> getDestinations(User user){
        ArrayList<TreeItem<Destination>> destinations = new ArrayList<>();
        if (user.getType() != User.TYPE_GUEST) {
            TreeItem<Destination> tablesItem = new TreeItem<>(new FormalDestination("Таблиці"));
            tablesItem.setExpanded(true);
            tablesItem.getChildren().add(new TreeItem<>(new ApplicantsDestination()));
            tablesItem.getChildren().add(new TreeItem<>(new OffersDestination()));
            tablesItem.getChildren().add(new TreeItem<>(new ApplicationsDestination()));
            if (user.getType() == User.TYPE_ADMIN) {
                tablesItem.getChildren().add(new TreeItem<>(new LogDestination()));
            }
            destinations.add(tablesItem);
        }
        TreeItem<Destination> queriesItem = new TreeItem<>(new FormalDestination("Запити"));
        queriesItem.setExpanded(true);
        queriesItem.getChildren().addAll(getQueries());
        destinations.add(queriesItem);
        return destinations;
    }

    private static List<TreeItem<Destination>> getQueries() {
        ArrayList<TreeItem<Destination>> destinations = new ArrayList<>();
        destinations.add(new TreeItem<>(new QueryDestination("1.1 Бали ЗНО вступника", "SELECT eie_subject.title AS 'Предмет', certificate_item.score AS 'Бал' \n" +
                "FROM applicant \n" +
                "JOIN certificate ON applicant.surname = '$Введіть прізвище абітурієнта:%'  AND applicant._id = certificate.applicant_id \n" +
                "JOIN certificate_item ON certificate._id = certificate_item.certificate_id\n" +
                "JOIN eie_subject ON certificate_item.eie_subject_id = eie_subject._id\n" +
                "ORDER BY eie_subject.title\n")));
        destinations.add(new TreeItem<>(new QueryDestination("1.2 Заяви на спеціальність", "SELECT admission_application._year AS 'Рік', applicant._name AS 'Ім''я', applicant.patronymic AS 'По-батькові', applicant.surname AS 'Прізвище', specialty.title AS 'Спеціальність', department.title AS 'Факультет', education_form.title AS 'Форма нав.'\n" +
                "FROM admission_application\n" +
                "JOIN offer ON admission_application.offer_id = offer._id\n" +
                "JOIN specialty ON specialty.title =  '$Введіть назву спеціальності:%' AND offer.specialty_id = specialty._id\n" +
                "JOIN department ON offer.department_id = department._id\n" +
                "JOIN education_form ON education_form._id = offer.education_form_id\n" +
                "JOIN applicant ON admission_application.applicant_id = applicant._id\n" +
                "ORDER BY applicant.surname\n")));
        destinations.add(new TreeItem<>(new QueryDestination("2.1 Пропозиції за початком факультету", "SELECT department.title AS 'Факультет', specialty.title AS 'Спеціальність', education_form.title AS 'Форма нав.', offer.bm_count AS 'БМ', offer.km_count AS 'КМ', offer.contract_price AS 'Вартість контр.', offer._status AS 'Статус'\n" +
                "FROM offer\n" +
                "JOIN department ON department.title LIKE  '$Введіть початок назви факультету:%%' AND department._id = offer.department_id\n" +
                "JOIN specialty ON offer.specialty_id = specialty._id\n" +
                "JOIN education_form ON offer.education_form_id = education_form._id\n")));
        destinations.add(new TreeItem<>(new QueryDestination("2.2 Абітурієнти за початком прізвища", "SELECT _name AS 'І''мя', patronymic AS 'По-батькові', surname AS 'Прізвище', date_of_birth AS 'Дата нар.', region.title AS 'Область', city AS 'Місто', street AS 'Вулиця', average AS 'Серед. бал'\n" +
                "FROM applicant\n" +
                "JOIN region ON applicant.region_id = region._id\n" +
                "WHERE applicant.surname LIKE '$Введіть початок прізвища абітурієнта:%%'\n")));
        destinations.add(new TreeItem<>(new QueryDestination("3.1 Абітурієнти з балом у діапазоні", "SELECT _name AS 'І''мя', patronymic AS 'По-батькові', surname AS 'Прізвище', date_of_birth AS 'Дата нар.', average AS 'Серед. бал'\n" +
                "FROM applicant\n" +
                "WHERE applicant.average BETWEEN $Введіть найнижчий бал діапазону:% AND $Введіть найвищий бал діапазону:%\n")));
        destinations.add(new TreeItem<>(new QueryDestination("3.2 Пропозиції з БМ у діапазоні", "SELECT specialty.title AS 'Спеціальність', department.title AS 'Факультет', education_form.title AS 'Форма нав.', offer.bm_count AS 'БМ', offer.km_count AS 'КМ'\n" +
                "FROM offer\n" +
                "JOIN department ON offer.department_id = department._id\n" +
                "JOIN specialty ON offer.specialty_id = specialty._id\n" +
                "JOIN education_form ON offer.education_form_id = education_form._id\n" +
                "WHERE offer.bm_count BETWEEN $Введіть мінімальну кількість БМ діапазону:% AND $Введіть максимальну кількість БМ діапазону:%\n")));
        destinations.add(new TreeItem<>(new QueryDestination("4.1 Кількість заяв у абітурієнта", "SELECT applicant._name AS 'Ім''я', applicant.patronymic AS 'По-батькові', applicant.surname AS 'Прізвище', COUNT(applicant._id) AS 'Кількість заяв'\n" +
                "FROM admission_application\n" +
                "JOIN applicant ON admission_application.applicant_id = applicant._id\n" +
                "WHERE applicant.surname = '$Введіть прізвище абітурієнта:%'\n" +
                "GROUP BY applicant._id")));
        destinations.add(new TreeItem<>(new QueryDestination("4.2 Прийняті заяви за рік", "SELECT COUNT(applicant._id) AS 'Кількість прийнятих заяв'\n" +
                "FROM applicant \n" +
                "JOIN admission_application ON admission_application._status = 1 AND admission_application._year = $Введіть рік:% AND applicant._id = admission_application.applicant_id\n")));
        destinations.add(new TreeItem<>(new QueryDestination("5.1 Кількість заяв за кожен рік", "SELECT _year AS 'Рік', COUNT(_id) AS 'Кількість поданих заяв'\n" +
                "FROM admission_application\n" +
                "GROUP BY _year ORDER BY _year\n").setReportTitle("щодо кількості поданих до ВУЗу заяв за кожен рік")));
        destinations.add(new TreeItem<>(new QueryDestination("5.2 Кількість здавачів кожного предмету", "SELECT eie_subject.title AS 'Предмет', COUNT(applicant._id) AS 'Кількість здавачів'\n" +
                "FROM applicant\n" +
                "JOIN certificate ON applicant._id = certificate.applicant_id\n" +
                "JOIN certificate_item ON certificate._id = certificate_item.certificate_id\n" +
                "JOIN eie_subject ON certificate_item.eie_subject_id = eie_subject._id\n" +
                "GROUP BY eie_subject.title\n").setReportTitle("щодо кількості здавачів кожного предмету зовнішнього незалежного оцінювання")));
        destinations.add(new TreeItem<>(new QueryDestination("6.1 Факультет з найб. кільк. проп.", "SELECT department.title AS 'Факультет', COUNT(offer._id) AS Kilkist\n" +
                "FROM department\n" +
                "JOIN offer ON offer._status = 1 AND department._id = offer.department_id\n" +
                "GROUP BY department._id\n" +
                "HAVING Kilkist >= ALL(\n" +
                "\tSELECT COUNT(offer._id)\n" +
                "\tFROM department\n" +
                "\tJOIN offer ON offer._status = 1 AND department._id = offer.department_id\n" +
                "\tGROUP BY department._id\n" +
                ")\n")));
        destinations.add(new TreeItem<>(new QueryDestination("6.2 Вид документів з найб. кільк.", "SELECT document_type.title AS 'Вид документу', SUM(document.quantity) as Suma\n" +
                "FROM document_type\n" +
                "JOIN document ON document_type._id = document.document_type_id\n" +
                "GROUP BY document_type._id\n" +
                "HAVING Suma >= ALL (\n" +
                "\tSELECT SUM(document.quantity)\n" +
                "\tFROM document_type\n" +
                "\tJOIN document ON document_type._id = document.document_type_id\n" +
                "\tGROUP BY document_type._id\n" +
                ")\n")));
        destinations.add(new TreeItem<>(new ApplicantByRegionQuery()));
        destinations.add(new TreeItem<>(new EIEBySpecialtyQuery()));
        destinations.add(new TreeItem<>(new QueryDestination("8.1 Студенти не пільговіки-сироти", "SELECT applicant._name AS 'Ім''я', applicant.patronymic AS 'По-батькові', applicant.surname AS 'Прізвище', date_of_birth AS 'Дата нар.'\n" +
                "FROM applicant LEFT JOIN (\n" +
                "\tSELECT DISTINCT applicant._id\n" +
                "\tFROM applicant\n" +
                "JOIN privilege_application ON applicant._id = privilege_application.applicant_id\n" +
                "JOIN privilege ON privilege_application.privilege_id = privilege._id AND privilege.title = 'Сирота'\n" +
                ") T\n" +
                "ON applicant._id = T._id\n" +
                "WHERE T._id IS NULL;\n")));
        destinations.add(new TreeItem<>(new QueryDestination("8.2 Спеціальності без заяв за рік", "SELECT specialty.title AS 'Спеціальність'\n" +
                "FROM specialty LEFT JOIN (\n" +
                "SELECT DISTINCT specialty._id as sp_id\n" +
                "FROM specialty\n" +
                "JOIN offer ON specialty._id = offer.specialty_id\n" +
                "JOIN admission_application ON offer._id = admission_application.offer_id\n" +
                "WHERE admission_application._year = $Введіть рік:%\n" +
                ") T\n" +
                "ON specialty._id = T.sp_id\n" +
                "WHERE T.sp_id IS NULL;\n")));
        destinations.add(new TreeItem<>(new QueryDestination("9.1 Предмети ЗНО за успішністю", "SELECT eie_subject.title AS 'Предмет ЗНО', AVG(certificate_item.score) as average, 'Висока загальна успішність' as 'Коментар'\n" +
                "FROM eie_subject \n" +
                "JOIN certificate_item ON eie_subject._id = certificate_item.eie_subject_id\n" +
                "GROUP BY eie_subject._id\n" +
                "HAVING average >= 170\n" +
                "UNION\n" +
                "SELECT eie_subject.title, AVG(certificate_item.score) as average, 'Середня загальна успішність' as 'Коментар'\n" +
                "FROM eie_subject \n" +
                "JOIN certificate_item ON eie_subject._id = certificate_item.eie_subject_id\n" +
                "GROUP BY eie_subject._id\n" +
                "HAVING average >= 150 AND average < 170\n" +
                "UNION\n" +
                "SELECT eie_subject.title, AVG(certificate_item.score) as average, 'Низька загальна успішність' as 'Коментар'\n" +
                "FROM eie_subject \n" +
                "JOIN certificate_item ON eie_subject._id = certificate_item.eie_subject_id\n" +
                "GROUP BY eie_subject._id\n" +
                "HAVING average < 150 \n" +
                "UNION\n" +
                "SELECT eie_subject.title, 0 as average, 'Немає даних' as 'Коментар'\n" +
                "FROM eie_subject \n" +
                "LEFT JOIN certificate_item ON eie_subject._id = certificate_item.eie_subject_id\n" +
                "WHERE certificate_item._id is null\n").setReportTitle("щодо загальної успішності абітурієнтів у предметах зовнішнього незалежного оцінювання")));
        destinations.add(new TreeItem<>(new QueryDestination("9.2 Спеціальності за популярністю", "SELECT specialty.title AS 'Спеціальність', COUNT(admission_application._id) AS Kilkist, 'Популярна' AS 'Коментар'\n" +
                "FROM specialty\n" +
                "JOIN offer ON specialty._id = offer.specialty_id\n" +
                "JOIN admission_application ON offer._id = admission_application.offer_id\n" +
                "GROUP BY specialty._id\n" +
                "HAVING Kilkist >= ALL(\n" +
                "\tSELECT AVG(T.c)\n" +
                "    FROM(\n" +
                "\tSELECT COUNT(admission_application._id) as c\n" +
                "\tFROM specialty\n" +
                "\tJOIN offer ON specialty._id = offer.specialty_id\n" +
                "\tJOIN admission_application ON offer._id = admission_application.offer_id\n" +
                "\tGROUP BY specialty._id\n" +
                "    ) T\n" +
                ")\n" +
                "UNION\n" +
                "SELECT specialty.title, COUNT(admission_application._id) as Kilkist, 'Малопопулярна' AS _comment\n" +
                "FROM specialty\n" +
                "JOIN offer ON specialty._id = offer.specialty_id\n" +
                "JOIN admission_application ON offer._id = admission_application.offer_id\n" +
                "GROUP BY specialty._id\n" +
                "HAVING Kilkist < ALL(\n" +
                "\tSELECT AVG(T.c)\n" +
                "    FROM(\n" +
                "\tSELECT COUNT(admission_application._id) as c\n" +
                "\tFROM specialty\n" +
                "\tJOIN offer ON specialty._id = offer.specialty_id\n" +
                "\tJOIN admission_application ON offer._id = admission_application.offer_id\n" +
                "\tGROUP BY specialty._id\n" +
                "    ) T\n" +
                ")\n").setReportTitle("щодо оцінки популярності представлених спеціальностей за кількістю заяв на вступ")));
        return destinations;
    }

}
