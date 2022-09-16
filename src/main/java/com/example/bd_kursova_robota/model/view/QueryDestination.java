package com.example.bd_kursova_robota.model.view;

import com.example.bd_kursova_robota.data.Storage;
import com.example.bd_kursova_robota.model.Row;
import com.example.bd_kursova_robota.util.NavigationUtil;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.alignment.VerticalAlignment;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class QueryDestination extends Destination{

    private static BaseFont timesNewRoman = null;
    private static BaseFont timesNewRomanBold = null;
    private static BaseFont timesNewRomanItalic = null;

    private String reportTitle;

    public QueryDestination setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
        return this;
    }

    public QueryDestination(String title, String sql) {
        this(title, sql, null);
    }

    public QueryDestination(String title, String sql, String[] excludedColumns) {
        super(title, sql, excludedColumns);
    }

    public ContextMenu getContextMenu(TableView<Row> tableView){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem printReportMenuItem = new MenuItem("Сформувати звіт");
        printReportMenuItem.setOnAction(event -> {
            try {
                printReport(tableView.getScene().getWindow());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
        contextMenu.getItems().addAll(printReportMenuItem);
        return contextMenu;
    }

    private void printReport(Window window) throws Exception {
        File file = requestFile(window);
        Document document = new Document(PageSize.A4, 72, 72, 36, 36);
        PdfWriter.getInstance(document,new FileOutputStream(file));
        document.open();
        for (Element element : getReportHeader()) {
            document.add(element);
        }
        for (Element element : getReportElements()) {
            document.add(element);
        }
        document.close();

        Desktop.getDesktop().open(file);
    }

    public ArrayList<Element> getReportElements() throws Exception{
        ArrayList<Element> reportElements = new ArrayList<>();

        BaseFont timesNewRoman = getTimesNewRoman();
        BaseFont timesNewRomanBold = getTimesNewRomanBold();
        Font fontNormal = new Font(timesNewRoman, 14);
        Font fontBold = new Font(timesNewRomanBold, 14);

        Table table = new Table(getColumnCount() - getExcludedCount());
        table.setPadding(5);
        table.setSpacing(0);
        table.setBorderWidth(0);
        table.setWidth(100);

        for (String columnLabel : getColumnLabels()){
            if (isExcluded(columnLabel)) continue;
            table.addCell(styleCell(new Cell(new Phrase(columnLabel.replace("Kilkist", "Кількість").replace("Suma", "Кількість одиниць").replace("average", "Середній бал"), fontBold)), true));
        }

        table.endHeaders();

        for(Row row : getRows()) {
            for (String columnName : getColumnLabels()) {
                if (isExcluded(columnName)) continue;
                table.addCell(styleCell(new Cell(new Phrase(row.get(columnName), fontNormal)), false));
            }
        }

        reportElements.add(table);
        return reportElements;
    }

    public ArrayList<Element> getReportHeader() throws Exception{
        ArrayList<Element> reportElements = new ArrayList<>();

        BaseFont timesNewRoman = getTimesNewRoman();
        BaseFont timesNewRomanBold = getTimesNewRomanBold();
        BaseFont timesNewRomanItalic = getTimesNewRomanItalic();

        Font fontNormal = new Font(timesNewRoman, 14);
        Font fontBold = new Font(timesNewRomanBold, 14);
        Font fontItalic = new Font(timesNewRomanItalic, 14);
        Font fontHeader = new Font(timesNewRomanBold, 20);
        Font fontSecondary = new Font(timesNewRoman, 14, Font.NORMAL, Color.GRAY);

        Paragraph p1 = new Paragraph("Вступна комісія ВУЗу", fontNormal);
        p1.setAlignment(Paragraph.ALIGN_RIGHT);
        p1.setSpacingAfter(16);

        reportElements.add(p1);

        Paragraph paragraph = new Paragraph("З В І Т", fontHeader);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph.setSpacingAfter(6);

        reportElements.add(paragraph);

        paragraph = new Paragraph(reportTitle, fontBold);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph.setSpacingAfter(16);

        reportElements.add(paragraph);

        paragraph = new Paragraph("Дата формування: " + new SimpleDateFormat("dd.MM.yyyy").format(new Date(System.currentTimeMillis())) + "\nКористувач: " + Storage.getInstance().getCurrentUser().getLogin(), fontItalic);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph.setSpacingAfter(6);

        reportElements.add(paragraph);
        return reportElements;
    }


    public static BaseFont getTimesNewRoman() throws Exception {
        if (timesNewRoman == null){
            timesNewRoman = BaseFont.createFont("c:\\windows\\fonts\\times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }
        return timesNewRoman;
    }

    public static BaseFont getTimesNewRomanItalic() throws Exception {
        if (timesNewRomanItalic == null){
            timesNewRomanItalic = BaseFont.createFont("c:\\windows\\fonts\\timesi.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }
        return timesNewRomanItalic;
    }

    public static BaseFont getTimesNewRomanBold() throws Exception {
        if (timesNewRomanBold == null){
            timesNewRomanBold = BaseFont.createFont("c:\\windows\\fonts\\timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }
        return timesNewRomanBold;
    }

    Cell styleCell(Cell cell, boolean center) {
        cell.setUseAscender(true);
        cell.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setHorizontalAlignment(center ? HorizontalAlignment.CENTER : HorizontalAlignment.LEFT);
        cell.setBorderWidth(1);
        return cell;
    }

    private File requestFile(Window window) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(window);
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();
        return file;
    }

}
