package com.gsnotes.services.impl;

import com.gsnotes.services.*;
import com.gsnotes.utils.export.ExportExcelFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportExcelFileImpl2 implements IExportFile {
    @Autowired
    IPersonService personService;

    @Autowired
    IModuleService moduleService;

    @Autowired
    IInscriptionMatiereService inscriptionMatiereService;

    @Autowired
    IElementService elementService;

    private static final List<ExportExcelFile.Etudiant> etudiantList = new ArrayList<>();
    private static CellRangeAddressList cellRangeAddressList = new CellRangeAddressList();
    private static Workbook workbook;
    private static XSSFSheet sheet;
    private XSSFRow row;
    private String year;
    private String niveau;
    private List<String> modulesList;

    public ExportExcelFileImpl2() {
    }

    public void setModulesList(String alias) {
        modulesList = moduleService.getModulesByNiveauAlias(alias);
    }

    public static void addCellRangeAddress(int a, int b, int c, int d) {
        cellRangeAddressList.addCellRangeAddress(new CellRangeAddress(a, b, c, d));
    }

    public void InsertModulesToHeader(String alias) {
        int numberOfModules = modulesList.size();

        for (int i = 4; i <= numberOfModules * 4; i += 4) {
            Cell cell = row.createCell(i);
            cell.setCellValue(modulesList.get(i / 4 - 1));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i + 3));
            addCellRangeAddress(1, 2, i, i + 3);
            CellStyle cellStyle = workbook.createCellStyle();
            cell.setCellStyle(cellStyle);
        }

        int boundry = numberOfModules * 4 + 4;
    }

    public void InsertModuleInfos(String alias) {

        row = sheet.createRow(3);
        int i = 4;
        for (String module : modulesList) {
            List<String> elements = moduleService.getElementsByModuleAlias(module);

            if (elements.size() == 2) {
                row.createCell(i).setCellValue("Element 1");
                addCellRangeAddress(3, 3, i, i);

                row.createCell(i + 1).setCellValue("Element 2");
                addCellRangeAddress(3, 3, i, i + 1);
            } else {
                row.createCell(i).setCellValue("Module");
                sheet.addMergedRegion(new CellRangeAddress(3, 3, i, i + 1));
                addCellRangeAddress(3, 3, i, i + 1);

            }

            row.createCell(i + 2).setCellValue("Moyenne");
            addCellRangeAddress(3, 3, i, i + 2);

            row.createCell(i + 3).setCellValue("Validation");
            addCellRangeAddress(3, 3, i, i + 3);

            i += 4;
        }
    }

    public void AddStylesToCells() {

        for (int i = 0; i < 8; i++)
            sheet.autoSizeColumn(i);

        for (Row row : sheet) {
            for (Cell cell : row) {
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                cell.setCellStyle(cellStyle);
            }
        }

        for (CellRangeAddress cellAddress : cellRangeAddressList.getCellRangeAddresses()) {
            RegionUtil.setBorderTop(BorderStyle.THIN, cellAddress, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellAddress, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, cellAddress, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellAddress, sheet);
        }

    }

    public void mergeCells(int modulesInS1, int modulesInS2) {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, (modulesInS1 + 1) * 4 - 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, (modulesInS1 + 1) * 4, (modulesInS1 + modulesInS2 + 1) * 4 - 1));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 3, 3));
    }

    public void insertEtudiant(List<String> etudiants, int currentRow) {
        row.createCell(0).setCellValue("x");
        addCellRangeAddress(currentRow, currentRow, 0, 1);

        row.createCell(1).setCellValue(etudiants.get(2));
        addCellRangeAddress(currentRow, currentRow, 1, 2);

        row.createCell(2).setCellValue(etudiants.get(0));
        addCellRangeAddress(currentRow, currentRow, 2, 3);

        row.createCell(3).setCellValue(etudiants.get(1));
        addCellRangeAddress(currentRow, currentRow, 3, 4);
    }

    public String prepareGlobalFormula(List<String> columnsIndexes) {
        String formula = "(";
        int size = columnsIndexes.size();

        for (int i = 0; i < size - 1; i++)
            formula += columnsIndexes.get(i) + " + ";

        formula += columnsIndexes.get(size - 1) + ")/" + size;
        formula = "ROUND(" + formula + ", 2)";

        return formula;
    }

    public void insertNotes(String alias, String cne, String year, int rowPosition, int size) {
        int currentColumn = 4;
        Cell cell = null;
        List<String> columnsIndexes = new ArrayList<>();

        for (String module : modulesList) {
            List<String> elements = moduleService.getElementsByModuleAlias(module);
            System.out.println("elements = " + elements);

            if (elements.size() >= 2) {
                cell = row.createCell(currentColumn);
                CellAddress firstMark = cell.getAddress();
                cell.setCellValue(inscriptionMatiereService.getElementNotes(cne, elements.get(0), year));
                addCellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn + 1);

                cell = row.createCell(currentColumn + 1);
                CellAddress secondMark = cell.getAddress();
                cell.setCellValue(inscriptionMatiereService.getElementNotes(cne, elements.get(1), year));
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 1, currentColumn + 2);

                String formula = "(" + firstMark.toString() + " * " + elementService.getCoefficientByElementAlias(elements.get(0)) +
                        " + " + secondMark + " * " + elementService.getCoefficientByElementAlias(elements.get(1)) + ")";
                formula = "ROUND(" + formula + ",2)";

                cell = row.createCell(currentColumn + 2);
                columnsIndexes.add(cell.getAddress().toString());
                cell.setCellFormula(formula);
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 2, currentColumn + 3);

                formula = "IF(" + cell.getAddress() + "< 12, \"NV\", \"V\")";
                row.createCell(currentColumn + 3).setCellFormula(formula);
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 3, currentColumn + 4);

            } else {

                cell = row.createCell(currentColumn);
                String moduleMarkAdress = cell.getAddress().toString();
                cell.setCellValue(moduleService.getNoteByModuleAlias(cne, module, year));
                addCellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn + 2);

                cell = row.createCell(currentColumn + 2);
                columnsIndexes.add(cell.getAddress().toString());
                cell.setCellFormula(moduleMarkAdress);
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 2, currentColumn + 3);

                String formula = "IF(" + cell.getAddress() + "< 12, \"NV\", \"V\")";
                row.createCell(currentColumn + 3).setCellFormula(formula);
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 3, currentColumn + 4);

                sheet.addMergedRegion(new CellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn + 1));
            }

            currentColumn += 4;
        }

        String formula = prepareGlobalFormula(columnsIndexes);

        cell = row.createCell(currentColumn);
        String rankAdress = cell.getAddress().toString();
        cell.setCellFormula(formula);
        addCellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn);

        String range = "";

        if (rankAdress.length() > 3)
            range = rankAdress.substring(0, rankAdress.length() - 2) + "5:" +
                    rankAdress.substring(0, rankAdress.length() - 2) + size;
        else
            range = rankAdress.substring(0, rankAdress.length() - 1) + "5:" +
                    rankAdress.substring(0, rankAdress.length() - 1) + size;


        formula = "SUMPRODUCT((" + rankAdress + "<=" + range + " )/COUNTIF( " + range + "," + range + "))";
        row.createCell(++currentColumn).setCellFormula(formula);
        addCellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn);

    }

    public void writeHeader(String year, String alias) throws IOException {
        workbook = new XSSFWorkbook();

        sheet = (XSSFSheet) workbook.createSheet("Délibration des notes");

        row = sheet.createRow(0);
        row.createCell(0).setCellValue("Année Universitaire");
        addCellRangeAddress(0, 0, 0, 1);

        row.createCell(1).setCellValue(year);
        addCellRangeAddress(0, 0, 1, 2);

        row.createCell(2).setCellValue("Date de délibération");
        addCellRangeAddress(0, 0, 2, 3);

        row.createCell(3).setCellValue("22/07/2021");
        addCellRangeAddress(0, 0, 3, 4);

        row.createCell(4).setCellValue("Semestre 1");
        addCellRangeAddress(0, 0, 4, 29);

        row.createCell(28).setCellValue("Semestre 2");
        addCellRangeAddress(0, 0, 28, 53);

        int boundry = 52;
        Cell cell = row.createCell(boundry);
        cell.setCellValue("Moyenne");
        sheet.addMergedRegion(new CellRangeAddress(0, 3, boundry, boundry));
        addCellRangeAddress(0, 3, 52, 53);


        cell = row.createCell(boundry + 1);
        cell.setCellValue("Rang");
        sheet.addMergedRegion(new CellRangeAddress(0, 3, boundry + 1, boundry + 1));
        addCellRangeAddress(0, 3, boundry + 1, boundry + 1);

        row = sheet.createRow(1);
        row.createCell(0).setCellValue("Classe");
        addCellRangeAddress(1, 1, 0, 1);

        row.createCell(2).setCellValue(alias);
        addCellRangeAddress(1, 1, 2, 3);

        InsertModulesToHeader(alias);

        row = sheet.createRow(2);
        row.createCell(0).setCellValue("ID ETUDIANT");
        addCellRangeAddress(2, 3, 0, 0);

        row.createCell(1).setCellValue("CNE");
        addCellRangeAddress(2, 2, 1, 2);

        row.createCell(2).setCellValue("NOM");
        addCellRangeAddress(2, 3, 2, 3);

        row.createCell(3).setCellValue("PRENOM");
        addCellRangeAddress(2, 3, 3, 4);

        InsertModuleInfos(alias);

        mergeCells(6, 6);

        List<List<String>> etudiants = personService.getStudents(alias, year);

        System.out.println("etudiants = " + etudiants);

        int size = etudiants.size();

        for (int i = 0; i < size; i++) {
            System.out.println("i = " + i);
            row = sheet.createRow(i + 4);

            System.out.println("etd = " + etudiants.get(i));
            insertEtudiant(etudiants.get(i), i + 4);
            insertNotes(alias, etudiants.get(i).get(2), year, i + 4, size + 4);
        }

        AddStylesToCells();
    }

    public void exportFile(HttpServletResponse response, String year, String niveau) throws IOException {
        setModulesList(niveau);
        this.writeHeader(year, niveau);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}