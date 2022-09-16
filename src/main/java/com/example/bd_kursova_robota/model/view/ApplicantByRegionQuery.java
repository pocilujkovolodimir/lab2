package com.example.bd_kursova_robota.model.view;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Row;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

public class ApplicantByRegionQuery extends QueryDestination{

    public static final String QUERY = "SELECT region.title AS 'Область', a._name AS 'Ім''я', a.patronymic AS 'По-батькові' , a.surname AS 'Прізвище', a.average AS 'Середній бал атестата'\n" +
            "FROM applicant a\n" +
            "JOIN region ON a.region_id = region._id\n" +
            "WHERE average = (\n" +
            "\tSELECT MAX(a2.average)\n" +
            "\tFROM applicant a2\n" +
            "\tJOIN region ON a2.region_id = a.region_id AND a2.region_id = region._id\n" +
            ")\n";

    public ApplicantByRegionQuery() {
        super("7.1 Абітурієнт кожного регіону з макс. балом", QUERY, new String[]{});
        setReportTitle("про абітурієнтів з найвищою навчальною успішністю за кожною областю України");
    }

    @Override
    public ArrayList<Element> getReportElements() throws Exception {
        ArrayList<Element> elements = new ArrayList<>();

        BaseFont timesNewRoman = getTimesNewRoman();
        BaseFont timesNewRomanBold = getTimesNewRomanBold();
        BaseFont timesNewRomanItalic = getTimesNewRomanItalic();

        Font fontNormal = new Font(timesNewRoman, 14);
        Font fontBold = new Font(timesNewRomanBold, 14);
        Font fontItalic = new Font(timesNewRomanItalic, 14);
        Font fontHeader = new Font(timesNewRomanBold, 20);

        Table table = new Table(3);
        table.setPadding(5);
        table.setSpacing(0);
        table.setBorderWidth(0);
        table.setWidth(100);

        table.addCell(styleCell(new Cell(new Phrase("Область", fontBold)), true));
        table.addCell(styleCell(new Cell(new Phrase("ПІБ", fontBold)), true));
        table.addCell(styleCell(new Cell(new Phrase("Середній бал атестата", fontBold)), true));


        table.endHeaders();

        for(Row row : getRows()) {
            table.addCell(styleCell(new Cell(new Phrase(row.get("Область"), fontNormal)), false));
            table.addCell(styleCell(new Cell(new Phrase(row.get("Ім'я") + " " +row.get("По-батькові")+" " + row.get("Прізвище"), fontNormal)), false));
            table.addCell(styleCell(new Cell(new Phrase(row.get("Середній бал атестата"), fontNormal)), false));
        }

        elements.add(table);



        Paragraph p1 = new Paragraph("У табл. 1 для областей України наведено ПІБ абітурієнтів з найвищим середнім балом атестата", fontNormal);
        p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        p1.setFirstLineIndent(54);
        p1.setSpacingAfter(6);
        p1.setSpacingBefore(10);
        elements.add(0, p1);

        p1 = new Paragraph("Таблиця 1 – Області та їх найуспішніші абітурієнти.", fontNormal);
        p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        elements.add(1, p1);
        //elements.add(0, new LineSeparator());
        int i = 2;
        HashSet<String> regions = new HashSet<>();
        for (Row row: getRows()){
            regions.add(row.get("Область"));
        }

        for (String region : regions){

            p1 = new Paragraph(String.format("У табл. %1$s наведено ПІБ абітурієнтів з області «%2$s»", i, region), fontNormal);
            p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            p1.setFirstLineIndent(54);
            p1.setSpacingAfter(6);
            p1.setSpacingBefore(10);
            elements.add(p1);

            p1 = new Paragraph(String.format("Таблиця %1$d – Абітурієнти з області «%2$s»", i, region), fontNormal);
            p1.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            elements.add(p1);

            table = new Table(2);
            table.setPadding(5);
            table.setSpacing(0);
            table.setBorderWidth(0);
            table.setWidth(100);
            table.setCellsFitPage(true);


            table.addCell(styleCell(new Cell(new Phrase("ПІБ", fontBold)), true));
            table.addCell(styleCell(new Cell(new Phrase("Середній бал атестата", fontBold)), true));

            table.endHeaders();

            ResultSet resultSet = Storage.getInstance().universalQuery("SELECT applicant._name, applicant.patronymic, applicant.surname, applicant.average FROM applicant JOIN region ON region._id = applicant.region_id WHERE region.title = '" +region+"'");
            while (resultSet.next()){
                table.addCell(styleCell(new Cell(new Phrase(resultSet.getString(1) + " " + resultSet.getString(2) + " " +resultSet.getString(3), fontNormal)), false));
                table.addCell(styleCell(new Cell(new Phrase(resultSet.getString(4), fontNormal)), false));
            }
            table.setComplete(true);
            elements.add(table);
            i++;
        }


        return elements;
    }

}
