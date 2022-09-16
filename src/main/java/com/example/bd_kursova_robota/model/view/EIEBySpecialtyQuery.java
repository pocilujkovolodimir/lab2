package com.example.bd_kursova_robota.model.view;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Row;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.draw.LineSeparator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EIEBySpecialtyQuery extends QueryDestination{

    public static final String QUERY = "SELECT specialty._id AS 'sid', s.eie_subject_id AS 'sbd', specialty.title AS 'Спеціальність', e.title AS 'Найвагоміший предмет ЗНО'\n" +
            "FROM specialty\n" +
            "JOIN specialty_subject s ON specialty._id = s.specialty_id AND s.coefficient >= (\n" +
            "\tSELECT MAX(specialty_subject.coefficient)\n" +
            "    \tFROM specialty\n" +
            "\tJOIN specialty_subject ON specialty._id = specialty_subject.specialty_id\n" +
            "    \tWHERE s.specialty_id = specialty_subject.specialty_id\n" +
            ")\n" +
            "JOIN eie_subject e ON s.eie_subject_id = e._id \n" +
            "GROUP BY specialty._id\n";

    public EIEBySpecialtyQuery() {
        super("7.2 Найвагоміший предмет кожної спец.", QUERY, new String[]{"sid", "sbd"});
        setReportTitle("про предмети зовнішнього незалежного оцінювання, необхідні для вступу на наявні спеціальності");
    }

    @Override
    public ArrayList<Element> getReportElements() throws Exception {
        ArrayList<Element> elements = super.getReportElements();

        BaseFont timesNewRoman = getTimesNewRoman();
        BaseFont timesNewRomanBold = getTimesNewRomanBold();
        BaseFont timesNewRomanItalic = getTimesNewRomanItalic();

        Font fontNormal = new Font(timesNewRoman, 14);
        Font fontBold = new Font(timesNewRomanBold, 14);
        Font fontItalic = new Font(timesNewRomanItalic, 14);
        Font fontHeader = new Font(timesNewRomanBold, 20);

        Paragraph p1 = new Paragraph("У табл. 1 для кожної наявної спеціальності наведено предмет ЗНО, що має найбільший, для неї, вступний коефіцієнт.", fontNormal);
        p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        p1.setFirstLineIndent(54);
        p1.setSpacingAfter(6);
        p1.setSpacingBefore(10);
        elements.add(0, p1);

        p1 = new Paragraph("Таблиця 1 – Спеціальності та їх «найвагоміші» предмети ЗНО.", fontNormal);
        p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        elements.add(1, p1);
        //elements.add(0, new LineSeparator());
        int i = 2;
        for (Row row : getRows()){

            p1 = new Paragraph(String.format("У табл. %1$s наведено предмети ЗНО, що є необхідними для вступу на спеціальність «%2$s»", i, row.get("Спеціальність")), fontNormal);
            p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            p1.setFirstLineIndent(54);
            p1.setSpacingAfter(6);
            p1.setSpacingBefore(10);
            elements.add(p1);

            p1 = new Paragraph(String.format("Таблиця %1$d – Предмети ЗНО для спеціальності «%2$s»", i, row.get("Спеціальність")), fontNormal);
            p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            elements.add(p1);

            Table table = new Table(getColumnCount() - getExcludedCount());
            table.setPadding(5);
            table.setSpacing(0);
            table.setBorderWidth(0);
            table.setWidth(100);
            table.setCellsFitPage(true);


            table.addCell(styleCell(new Cell(new Phrase("Предмет", fontBold)), true));
            table.addCell(styleCell(new Cell(new Phrase("Коефіцієнт", fontBold)), true));

            table.endHeaders();

            ResultSet resultSet = Storage.getInstance().universalQuery("SELECT specialty_subject.eie_subject_id, eie_subject.title, specialty_subject.coefficient FROM specialty_subject JOIN eie_subject ON specialty_subject.eie_subject_id = eie_subject._id WHERE specialty_subject.specialty_id = " +row.get("sid"));
            while (resultSet.next()){
                boolean bold = row.get("sbd").equals(resultSet.getString(1));
                table.addCell(styleCell(new Cell(new Phrase(resultSet.getString(2), bold ? fontBold : fontNormal)), false));
                table.addCell(styleCell(new Cell(new Phrase(resultSet.getString(3), bold ? fontBold :fontNormal)), false));
            }
            table.setComplete(true);
            elements.add(table);
            i++;
        }


        return elements;
    }
}
