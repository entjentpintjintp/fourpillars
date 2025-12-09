package com.kolloseum.fourpillars.common.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExcelPreprocessorTest {

    @Test
    void exportForProofreading() throws IOException {
        // 1. Load split file
        File sourceFile = new File("src/main/resources/data/fortune_split.xlsx");
        if (!sourceFile.exists()) {
            sourceFile = new File("build/resources/main/data/fortune_split.xlsx");
        }

        StringBuilder sb = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(sourceFile);
                Workbook workbook = new XSSFWorkbook(fis)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                sb.append("=== Sheet: ").append(sheet.getSheetName()).append(" ===\n");

                for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null)
                        continue;

                    for (int c = 0; c < row.getLastCellNum(); c++) {
                        Cell cell = row.getCell(c);
                        String value = getCellValueAsString(cell).trim();

                        // Skip empty or numeric-only cells (likely IDs)
                        if (!value.isEmpty() && !value.matches("\\d+")) {
                            sb.append(String.format("[Row:%d Col:%d] %s\n", r + 1, c + 1, value));
                        }
                    }
                }
                sb.append("\n");
            }
        }

        // 2. Save to text file
        File outputFile = new File(sourceFile.getParent(), "fortune_content.txt");
        try (FileOutputStream fos = new FileOutputStream(outputFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            osw.write(sb.toString());
        }

        System.out.println("Content exported to: " + outputFile.getAbsolutePath());
    }

    @Test
    void inspectExcelFile() throws IOException {
        File sourceFile = new File("src/main/resources/data/fortune_split.xlsx");
        if (!sourceFile.exists()) {
            sourceFile = new File("build/resources/main/data/fortune_split.xlsx");
        }

        // Define typos mapping
        java.util.Map<String, String> typos = new java.util.HashMap<>();
        typos.put("심혀을", "심혈을");
        typos.put("기회과", "기회가");
        typos.put("몰라나", "몰리나");
        typos.put("풀리리다", "풀리리라");
        typos.put("앟는다", "않는다");
        typos.put("펼처", "펼쳐");
        typos.put("마나는", "만나는");
        typos.put("옩아", "혼자");
        typos.put("부리한", "불리한");
        typos.put("남족은", "남쪽은");
        typos.put("헹운", "행운");
        typos.put("상황고", "상황과");
        typos.put("내공이", "내 공이");

        try (FileInputStream fis = new FileInputStream(sourceFile);
                Workbook workbook = new XSSFWorkbook(fis)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null)
                        continue;

                    for (int c = 0; c < row.getLastCellNum(); c++) {
                        Cell cell = row.getCell(c);
                        if (cell == null || cell.getCellType() != CellType.STRING)
                            continue;

                        String original = cell.getStringCellValue();
                        String modified = original;

                        for (java.util.Map.Entry<String, String> entry : typos.entrySet()) {
                            if (modified.contains(entry.getKey())) {
                                modified = modified.replace(entry.getKey(), entry.getValue());
                            }
                        }

                        if (!original.equals(modified)) {
                            cell.setCellValue(modified);
                            System.out.println("Fixed typo at [Sheet:" + sheet.getSheetName() + " Row:" + (r + 1)
                                    + " Col:" + (c + 1) + "]");
                            System.out.println("  " + original + " -> " + modified);
                        }
                    }
                }
            }

            // 2. Save back to file (overwrite)
            try (FileOutputStream fos = new FileOutputStream(sourceFile)) {
                workbook.write(fos);
            }

            System.out.println("Typos fixed in: " + sourceFile.getAbsolutePath());
        }
    }

    @Test
    void checkSheet2Types() throws IOException {
        File sourceFile = new File("src/main/resources/data/fortune.xlsx");
        if (!sourceFile.exists()) {
            sourceFile = new File("build/resources/main/data/fortune.xlsx");
        }

        try (FileInputStream fis = new FileInputStream(sourceFile);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(1); // Sheet 2
            System.out.println("Sheet 2 Types:");
            java.util.Set<String> types = new java.util.HashSet<>();
            for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null)
                    continue;
                Cell cell = row.getCell(1); // Col B
                if (cell != null) {
                    types.add(cell.toString().trim());
                }
            }
            for (String type : types) {
                System.out.println("- " + type);
            }
        }
    }

    @Test
    void splitExcelFile() throws IOException {
        // 1. Load normalized file
        File sourceFile = new File("src/main/resources/data/fortune_normalized.xlsx");
        if (!sourceFile.exists()) {
            // Fallback to build directory if not found in src
            sourceFile = new File("build/resources/main/data/fortune_normalized.xlsx");
        }

        try (FileInputStream fis = new FileInputStream(sourceFile);
                Workbook sourceWorkbook = new XSSFWorkbook(fis);
                Workbook targetWorkbook = new XSSFWorkbook()) {

            System.out.println("Splitting source: " + sourceFile.getAbsolutePath());
            System.out.println("Source sheets: " + sourceWorkbook.getNumberOfSheets());

            // Iterate through all sheets in the source workbook
            for (int i = 0; i < sourceWorkbook.getNumberOfSheets(); i++) {
                Sheet sourceSheet = sourceWorkbook.getSheetAt(i);
                System.out.println("Processing sheet [" + i + "]: " + sourceSheet.getSheetName());

                if (i == 0) {
                    // --- Process Main Sheet (Split into Master/Detail) ---
                    Sheet masterSheet = targetWorkbook.createSheet("Master");
                    Sheet detailSheet = targetWorkbook.createSheet("Detail");

                    List<String> seenIds = new ArrayList<>();
                    int masterRowIdx = 0;
                    int detailRowIdx = 0;

                    for (int r = 0; r <= sourceSheet.getLastRowNum(); r++) {
                        Row sourceRow = sourceSheet.getRow(r);
                        if (sourceRow == null)
                            continue;

                        // Column A (Index)
                        Cell idCell = sourceRow.getCell(0);
                        String id = getCellValueAsString(idCell);

                        // --- Master Sheet (A, B columns, Unique ID) ---
                        if (!seenIds.contains(id)) {
                            seenIds.add(id);
                            Row masterRow = masterSheet.createRow(masterRowIdx++);

                            // Copy Col A
                            copyCell(idCell, masterRow.createCell(0));
                            // Copy Col B
                            copyCell(sourceRow.getCell(1), masterRow.createCell(1));
                        }

                        // --- Detail Sheet (A, C~End columns, All rows) ---
                        Row detailRow = detailSheet.createRow(detailRowIdx++);

                        // Copy Col A (Foreign Key)
                        copyCell(idCell, detailRow.createCell(0));

                        // Copy Col C to End
                        int targetColIdx = 1;
                        for (int c = 2; c < sourceRow.getLastCellNum(); c++) {
                            copyCell(sourceRow.getCell(c), detailRow.createCell(targetColIdx++));
                        }
                    }
                } else {
                    // --- Copy other sheets as is ---
                    Sheet targetSheet = targetWorkbook.createSheet(sourceSheet.getSheetName());
                    copySheet(sourceSheet, targetSheet);
                }
            }

            // 2. Save to new file
            File outputFile = new File(sourceFile.getParent(), "fortune_split.xlsx");
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                targetWorkbook.write(fos);
            }

            System.out.println("Split Excel file created at: " + outputFile.getAbsolutePath());
        }
    }

    private void copySheet(Sheet source, Sheet target) {
        for (int i = 0; i <= source.getLastRowNum(); i++) {
            Row sourceRow = source.getRow(i);
            if (sourceRow == null)
                continue;
            Row targetRow = target.createRow(i);

            for (int c = 0; c < sourceRow.getLastCellNum(); c++) {
                Cell sourceCell = sourceRow.getCell(c);
                if (sourceCell == null)
                    continue;
                Cell targetCell = targetRow.createCell(c);
                copyCell(sourceCell, targetCell);
            }
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null)
            return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            default -> cell.toString();
        };
    }

    private void copyCell(Cell source, Cell target) {
        if (source == null)
            return;

        switch (source.getCellType()) {
            case STRING -> target.setCellValue(source.getStringCellValue());
            case NUMERIC -> target.setCellValue(source.getNumericCellValue());
            case BOOLEAN -> target.setCellValue(source.getBooleanCellValue());
            case FORMULA -> target.setCellFormula(source.getCellFormula());
            default -> target.setCellValue("");
        }
    }

    @Test
    void normalizeExcelFile() throws IOException {
        // 1. Load source file
        ClassPathResource resource = new ClassPathResource("data/fortune.xlsx");
        File sourceFile = resource.getFile();
        System.out.println("Normalizing source: " + sourceFile.getAbsolutePath());

        try (FileInputStream fis = new FileInputStream(sourceFile);
                Workbook workbook = new XSSFWorkbook(fis)) {

            System.out.println("Source sheets: " + workbook.getNumberOfSheets());
            // Process all sheets
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                System.out.println("Processing sheet: " + sheet.getSheetName());
                processSheet(sheet);
            }

            // 2. Save to new file
            File outputFile = new File(sourceFile.getParent(), "fortune_normalized.xlsx");
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                workbook.write(fos);
            }

            System.out.println("Normalized Excel file created at: " + outputFile.getAbsolutePath());
        }
    }

    private void processSheet(Sheet sheet) {
        List<CellRangeAddress> mergedRegions = new ArrayList<>(sheet.getMergedRegions());

        // Clear merged regions from sheet first to avoid conflicts
        for (int i = 0; i < mergedRegions.size(); i++) {
            sheet.removeMergedRegion(0); // Always remove 0th as list shrinks
        }

        // Fill data
        for (CellRangeAddress region : mergedRegions) {
            int firstRow = region.getFirstRow();
            int firstCol = region.getFirstColumn();

            Row sourceRow = sheet.getRow(firstRow);
            if (sourceRow == null)
                continue;

            Cell sourceCell = sourceRow.getCell(firstCol);
            if (sourceCell == null)
                continue;

            Object value = getCellValue(sourceCell);
            CellStyle style = sourceCell.getCellStyle();

            for (int r = region.getFirstRow(); r <= region.getLastRow(); r++) {
                Row row = sheet.getRow(r);
                if (row == null)
                    row = sheet.createRow(r);

                for (int c = region.getFirstColumn(); c <= region.getLastColumn(); c++) {
                    Cell cell = row.getCell(c);
                    if (cell == null)
                        cell = row.createCell(c);

                    setCellValue(cell, value);
                    cell.setCellStyle(style); // Copy style (optional)
                }
            }
        }
    }

    private Object getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    private void setCellValue(Cell cell, Object value) {
        if (value instanceof String s) {
            cell.setCellValue(s);
        } else if (value instanceof Double d) {
            cell.setCellValue(d);
        } else if (value instanceof Boolean b) {
            cell.setCellValue(b);
        }
    }
}
