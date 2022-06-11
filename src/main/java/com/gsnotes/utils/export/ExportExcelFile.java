package com.gsnotes.utils.export;

import com.gsnotes.dao.IUtilisateurDao;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class ExportExcelFile {

    private IUtilisateurDao personDao;

    private static final List<Etudiant> etudiantList = new ArrayList<>();
    private static Workbook workbook;
    private static XSSFSheet sheet;
    private XSSFRow row;
    private String year;
    private String niveau;


    public ExportExcelFile(String year, String niveau, IUtilisateurDao personDao) {
        etudiantList.add(new Etudiant("1", "EE147593", "Belaoud", "Abdelhalim"));
        etudiantList.add(new Etudiant("2", "AA123654", "Richard", "Kevins"));
        etudiantList.add(new Etudiant("3", "UE47593", "Tom", "Grom"));
        etudiantList.add(new Etudiant("4", "OI47593", "Peter", "White"));
        etudiantList.add(new Etudiant("5", "EP144593", "Wallace", "Ederson"));
        System.out.println("tessst");
        this.year = year;
        this.niveau = niveau;
        this.personDao = personDao;
    }

    public static class Etudiant {

        private String id;
        private String cne;
        private String nom;
        private String prenom;
        public static final Map<String, ArrayList<Object>> moduleNotes = new HashMap<>();

        public Etudiant(String id, String cne, String nom, String prenom) {
            this.id = id;
            this.cne = cne;
            this.nom = nom;
            this.prenom = prenom;

            moduleNotes.put("Python pour les sciences de données", new ArrayList<>(Arrays.asList(15, 17, 0.4, 0.6)));
            moduleNotes.put("Programmation Java Avancée", new ArrayList<>(List.of(13)));
            moduleNotes.put("Langues et Communication Professionnelle 2 \n Soft Skils", new ArrayList<>(List.of(18)));
            moduleNotes.put("Linux et programmation système", new ArrayList<>(List.of(17)));
            moduleNotes.put("Administration des Bases de données Avancées", new ArrayList<>(List.of(17)));
            moduleNotes.put("Administration réseaux et systèmes", new ArrayList<>(List.of(17)));
            moduleNotes.put("Entreprenariat 2", new ArrayList<>(List.of(13)));
            moduleNotes.put("Gestion de projet & Génie logiciel", new ArrayList<>(List.of(18)));
            moduleNotes.put("Machine Learning", new ArrayList<>(List.of(17)));
            moduleNotes.put("Crypto-systèmes & sécurité Informatique", new ArrayList<>(List.of(17)));
            moduleNotes.put("Frameworks Java EE avancés & .Net", new ArrayList<>(List.of(17)));
            moduleNotes.put("Web 2 : Applications Web modernes", new ArrayList<>(List.of(17)));
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCne() {
            return cne;
        }

        public void setCne(String cne) {
            this.cne = cne;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

    }

    public void InsertModulesToHeader() {
        int numberOfModules = personDao.getNumberOfModules(niveau);
        List<String> modules = personDao.getModules(niveau);

        System.out.println("modules = " + modules);

        for(int i = 4; i <= numberOfModules*4; i+=4) {
            Cell cell = row.createCell(i);
            cell.setCellValue(modules.get(i/4 - 1));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i+3));
            CellStyle cellStyle = workbook.createCellStyle();
            cell.setCellStyle(cellStyle);
        }

        int boundry =  numberOfModules*4+ 4;
        System.out.println(boundry);

        Cell cell = row.createCell(boundry);
        cell.setCellValue("Moyenne");
        sheet.addMergedRegion(new CellRangeAddress(0, 3, boundry, boundry));

        cell = row.createCell(boundry+1);
        cell.setCellValue("Rang");
        sheet.addMergedRegion(new CellRangeAddress(0, 3, boundry+1, boundry+1));
    }

    public void InsertModuleInfos(int numberOfModules) {
        List<String> modules = personDao.getModules(niveau);

        row = sheet.createRow(3);
        int i = 4;
        for (String module: modules){
            List<String> elements = personDao.elementsPerModule(module);
            if(elements.size() == 2) {
                row.createCell(i).setCellValue("Element 1");
                row.createCell(i+1).setCellValue("Element 2");
            } else {
                row.createCell(i).setCellValue("Module");
                sheet.addMergedRegion(new CellRangeAddress(3, 3, i, i + 1));
            }
            row.createCell(i+2).setCellValue("Moyenne");
            row.createCell(i+3).setCellValue("Validation");
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

    }

    public void mergeCells() {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 27));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 28, 51));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 3, 3));
    }

    public void insertEtudiant(List<String> etudiants) {
        row.createCell(0).setCellValue("x");
        row.createCell(1).setCellValue(etudiants.get(2));
        row.createCell(2).setCellValue(etudiants.get(0));
        row.createCell(3).setCellValue(etudiants.get(1));
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

    public void insertNotes(List<String> moduleList, int rowPosition, int size) {
        int currentColumn = 4;
        Cell cell = null;
        List<String> columnsIndexes = new ArrayList<>();

        for (String module: moduleList) {
            ArrayList<Object> notes = Etudiant.moduleNotes.get(module);

            if(notes.size() > 3) {
                System.out.println();
                cell = row.createCell(currentColumn);
                CellAddress firstMark = cell.getAddress();
                cell.setCellValue(notes.get(0).toString());

                cell = row.createCell(currentColumn + 1);
                CellAddress secondMark = cell.getAddress();
                cell.setCellValue(notes.get(1).toString());

                String formula = "(" +firstMark.toString() + " + " + secondMark + ")/2";
                formula = "ROUND(" + formula + ",2)";

                cell = row.createCell(currentColumn + 2);
                columnsIndexes.add(cell.getAddress().toString());
                cell.setCellFormula(formula);

                formula = "IF("+cell.getAddress()+"< 12, \"NV\", \"V\")";
                row.createCell(currentColumn + 3).setCellFormula(formula);

            } else {
                cell = row.createCell(currentColumn);
                String moduleMarkAdress = cell.getAddress().toString();
                cell.setCellValue(notes.get(0).toString());

                cell = row.createCell(currentColumn + 2);
                columnsIndexes.add(cell.getAddress().toString());
                cell.setCellFormula(moduleMarkAdress);

                String formula = "IF("+cell.getAddress()+"< 12, \"NV\", \"V\")";
                row.createCell(currentColumn + 3).setCellFormula(formula);

                sheet.addMergedRegion(new CellRangeAddress(rowPosition, rowPosition, currentColumn, currentColumn+1));
            }

            currentColumn += 4;
        }

        String formula = prepareGlobalFormula(columnsIndexes);

        cell = row.createCell(currentColumn);
        String rankAdress = cell.getAddress().toString();
        cell.setCellFormula(formula);


        String range = rankAdress.substring(0, rankAdress.length() - 1) + "5:" +
                rankAdress.substring(0, rankAdress.length() - 1) + size;


        formula = "SUMPRODUCT((" + rankAdress + "<=" + range + " )/COUNTIF( " + range + "," + range + "))";
        row.createCell(++currentColumn).setCellFormula(formula);

    }

    public void writeHeader() throws IOException {
        workbook = new XSSFWorkbook();

        sheet = (XSSFSheet) workbook.createSheet("Délibration des notes");

        List<String> moduleList = new ArrayList<> (Arrays.asList("Python pour les sciences de données",
                "Programmation Java Avancée",
                "Langues et Communication Professionnelle 2 \n Soft Skils",
                "Linux et programmation système",
                "Administration des Bases de données Avancées",
                "Administration réseaux et systèmes",
                "Entreprenariat 2",
                "Machine Learning",
                "Gestion de projet & Génie logiciel",
                "Crypto-systèmes & sécurité Informatique",
                "Frameworks Java EE avancés & .Net",
                "Web 2 : Applications Web modernes"));

        row = sheet.createRow(0);
        row.createCell(0).setCellValue("Année Universitaire");
        row.createCell(1).setCellValue(year);
        row.createCell(2).setCellValue("Date de délibération");
        row.createCell(3).setCellValue("22/07/2021");
        row.createCell(4).setCellValue("Semestre 1");
        row.createCell(28).setCellValue("Semestre 2");

        row = sheet.createRow(1);
        row.createCell(0).setCellValue("Classe");
        row.createCell(2).setCellValue(niveau);

        InsertModulesToHeader();

        row = sheet.createRow(2);
        row.createCell(0).setCellValue("ID ETUDIANT");
        row.createCell(1).setCellValue("CNE");
        row.createCell(2).setCellValue("NOM");
        row.createCell(3).setCellValue("PRENOM");

        InsertModuleInfos(moduleList.size());

        mergeCells();

        List<List<String>> etudiants = personDao.getStudents(niveau, year.split("/")[1]);

        System.out.println("etudiants = " + etudiants);

        for(int i = 0; i < etudiants.size(); i++) {
            System.out.println("i = " + i);
            row = sheet.createRow(i+4);

            System.out.println("etd = " + etudiants.get(i));
            insertEtudiant(etudiants.get(i));
            //insertNotes(moduleList, i + 4, size + 4);
        }

        AddStylesToCells();
    }

    public void export(HttpServletResponse response) throws IOException {
        this.writeHeader();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}