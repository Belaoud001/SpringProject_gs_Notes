package com.gsnotes.services.impl;

import com.gsnotes.services.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Service
public class ExportExcelFileImpl implements IExportFile {

    @Autowired
    IPersonService personService;

    @Autowired
    IModuleService moduleService;

    @Autowired
    IInscriptionMatiereService inscriptionMatiereService;

    @Autowired
    IElementService elementService;

    @Autowired
    IInscriptionModuleService iInscriptionModuleService;

    private static CellRangeAddressList cellRangeAddressList = new CellRangeAddressList();
    private static Workbook workbook;
    private static XSSFSheet sheet;
    private XSSFRow row;
    private List<String> modulesList;
    private HashMap<String, List<Object>> moduleNoteMap;
    private HashMap<String, List<Double>> elementNoteMap;

    public ExportExcelFileImpl() {

    }

    public void setModulesList(String alias) {
         modulesList = moduleService.getModulesByNiveauAlias(alias);
    }

    public void loadNotes(String year, String niveau) {
        moduleNoteMap = new HashMap<>();

        for (String module: modulesList) {
            List<String> elements = moduleService.getElementsByModuleAlias(module);
            if(elements.size() == 2) {
                List<Object> tempArray = new ArrayList<>();
                tempArray.add(new ArrayList<>(Arrays.asList(elements.get(0), elementService.getCoefficientByElementAlias(elements.get(0)))));
                tempArray.add(new ArrayList<>(Arrays.asList(elements.get(1), elementService.getCoefficientByElementAlias(elements.get(1)))));
                moduleNoteMap.put(module, tempArray);

                if(elementNoteMap == null)
                    elementNoteMap = new HashMap<>();

                elementNoteMap.put(elements.get(0), inscriptionMatiereService.getNotesByElementAndYear(year, niveau, elements.get(0)));
                elementNoteMap.put(elements.get(1), inscriptionMatiereService.getNotesByElementAndYear(year, niveau, elements.get(1)));
            } else {
                moduleNoteMap.put(module, iInscriptionModuleService.getNotesByModuleAndYear(year, niveau, module));
            }
        }
    }

    public static void addCellRangeAddress(int a, int b, int c, int d) {
        cellRangeAddressList.addCellRangeAddress(new CellRangeAddress(a, b, c, d));
    }

    public void InsertModulesToHeader() {
        int numberOfModules = modulesList.size();

        for(int i = 4; i <= numberOfModules*4; i+=4) {
            Cell cell = row.createCell(i);
            cell.setCellValue(modulesList.get(i/4 - 1));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i+3));
            addCellRangeAddress(1, 2, i, i+3);
            CellStyle cellStyle = workbook.createCellStyle();
            cell.setCellStyle(cellStyle);
        }

        int boundry =  numberOfModules*4+ 4;
    }

    public void InsertModuleInfos() {

        row = sheet.createRow(3);
        int i = 4;
        for (String module: modulesList){
            List<Object> moduleData = moduleNoteMap.get(module);

            if(moduleData.size() == 2) {
                row.createCell(i).setCellValue("Element 1");
                addCellRangeAddress(3, 3, i, i);

                row.createCell(i+1).setCellValue("Element 2");
                addCellRangeAddress(3, 3, i, i + 1);
            } else {
                row.createCell(i).setCellValue("Module");
                sheet.addMergedRegion(new CellRangeAddress(3, 3, i, i + 1));
                addCellRangeAddress(3, 3, i, i + 1);

            }

            row.createCell(i+2).setCellValue("Moyenne");
            addCellRangeAddress(3, 3, i, i + 2);

            row.createCell(i+3).setCellValue("Validation");
            addCellRangeAddress(3, 3, i, i + 3);

            i+=4;
        }
    }

    public void AddStylesToCells() {

        for(int i = 0; i < 8; i++)
            sheet.autoSizeColumn(i);

        for(Row row: sheet) {
            for(Cell cell: row) {
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                cell.setCellStyle(cellStyle);
            }
        }

        for(CellRangeAddress cellAddress: cellRangeAddressList.getCellRangeAddresses()){
            if(cellAddress != null) {
                RegionUtil.setBorderTop(BorderStyle.THIN, cellAddress, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellAddress, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellAddress, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellAddress, sheet);
            }
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
        row.createCell(0).setCellValue(etudiants.get(3));
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

    public void insertNotes(int rowPosition, int size) {
        int currentColumn = 4;
        Cell cell = null;
        List<String> columnsIndexes = new ArrayList<>();

        for (String module: modulesList) {
            List<Object> moduleData = moduleNoteMap.get(module);
            if(moduleData.size() <= 2) {
                cell = row.createCell(currentColumn);
                CellAddress firstMark = cell.getAddress();
                List<String> element1Coefficient =  (ArrayList<String>)moduleData.get(0);
                cell.setCellValue(elementNoteMap.get(element1Coefficient.get(0)).get(rowPosition - 4));
                addCellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn + 1);

                cell = row.createCell(currentColumn + 1);
                CellAddress secondMark = cell.getAddress();
                List<String> element2Coefficient =  (ArrayList<String>)moduleData.get(1);
                cell.setCellValue(elementNoteMap.get(element2Coefficient.get(0)).get(rowPosition - 4));
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 1, currentColumn + 2);

                String formula = "(" +firstMark.toString() + " * " + String.valueOf(element1Coefficient.get(1)) +
                                    " + " + secondMark + " * " + String.valueOf(element2Coefficient.get(1)) + ")";
                formula = "ROUND(" + formula + ",2)";

                cell = row.createCell(currentColumn + 2);
                columnsIndexes.add(cell.getAddress().toString());
                cell.setCellFormula(formula);
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 2, currentColumn + 3);

                formula = "IF("+cell.getAddress()+"< 12, \"NV\", \"V\")";
                row.createCell(currentColumn + 3).setCellFormula(formula);
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 3, currentColumn + 4);

            } else {

                cell = row.createCell(currentColumn);
                String moduleMarkAdress = cell.getAddress().toString();
                cell.setCellValue((double) moduleNoteMap.get(module).get(rowPosition - 4));
                addCellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn + 2);

                cell = row.createCell(currentColumn + 2);
                columnsIndexes.add(cell.getAddress().toString());
                cell.setCellFormula(moduleMarkAdress);
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 2, currentColumn + 3);

                String formula = "IF("+cell.getAddress()+"< 12, \"NV\", \"V\")";
                row.createCell(currentColumn + 3).setCellFormula(formula);
                addCellRangeAddress(rowPosition, rowPosition, currentColumn + 3, currentColumn + 4);

                sheet.addMergedRegion(new CellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn+1));
            }

            currentColumn += 4;
        }

        String formula = prepareGlobalFormula(columnsIndexes);

        cell = row.createCell(currentColumn);
        String rankAdress = cell.getAddress().toString();
        cell.setCellFormula(formula);
        addCellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn);

        String range = "";

        if(rankAdress.length() > 3)
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
        int numberOfModules = moduleService.moduleBySemester(alias, "S1");

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
        addCellRangeAddress(0, 0, 4, numberOfModules * 4 + 4);

        row.createCell(numberOfModules * 4 + 4).setCellValue("Semestre 2");
        addCellRangeAddress(0, 0, 28, 28 + numberOfModules * 4 + 1);

        int boundry = 4 + numberOfModules * 4 + numberOfModules * 4;

        Cell cell = row.createCell(boundry);
        cell.setCellValue("Moyenne");
        sheet.addMergedRegion(new CellRangeAddress(0, 3, boundry, boundry));
        addCellRangeAddress(0, 3, boundry, boundry+1);

        cell = row.createCell(boundry+1);
        cell.setCellValue("Rang");
        sheet.addMergedRegion(new CellRangeAddress(0, 3, boundry+1, boundry+1));
        addCellRangeAddress(0, 3, boundry+1, boundry+1);

        row = sheet.createRow(1);
        row.createCell(0).setCellValue("Classe");
        addCellRangeAddress(1, 1, 0, 1);

        row.createCell(2).setCellValue(alias);
        addCellRangeAddress(1, 1, 2, 3);

        InsertModulesToHeader();

        row = sheet.createRow(2);
        row.createCell(0).setCellValue("ID ETUDIANT");
        addCellRangeAddress(2, 3, 0, 0);

        row.createCell(1).setCellValue("CNE");
        addCellRangeAddress(2, 3, 1, 2);

        row.createCell(2).setCellValue("NOM");
        addCellRangeAddress(2, 3, 2, 3);

        row.createCell(3).setCellValue("PRENOM");
        addCellRangeAddress(2, 3, 3, 4);

        InsertModuleInfos();

        mergeCells(numberOfModules, numberOfModules);

        List<List<String>> etudiants = personService.getStudents(alias, year);

        int size = etudiants.size();

        for(int i = 0; i < size; i++) {
            row = sheet.createRow(i+4);

            insertEtudiant(etudiants.get(i), i + 4);
            insertNotes(i + 4, size + 4);
        }

        AddStylesToCells();
    }

    public void exportFile(HttpServletResponse response, String year, String niveau) throws IOException {
        setModulesList(niveau);
        loadNotes(year, niveau);

        this.writeHeader(year, niveau);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
