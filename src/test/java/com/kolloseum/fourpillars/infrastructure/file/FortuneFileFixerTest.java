package com.kolloseum.fourpillars.infrastructure.file;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FortuneFileFixerTest {

    private static final String FILE_PATH = "src/main/resources/data/fortune.xlsx";

    @Test
    @Disabled
    public void inspectSheet2() throws Exception {
        try (InputStream is = new FileInputStream(FILE_PATH);
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet2 = workbook.getSheetAt(1); // Monthly Sheet

            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            StringBuilder sb = new StringBuilder();
            sb.append("Sheet 2 Last Row: ").append(sheet2.getLastRowNum()).append("\n");
            sb.append("--- Rows 50 to 70 ---\n");
            for (int i = 50; i <= 70; i++) {
                Row row = sheet2.getRow(i);
                if (row == null)
                    continue;

                String code = formatter.formatCellValue(row.getCell(0), evaluator);
                String type = formatter.formatCellValue(row.getCell(1), evaluator);
                sb.append(String.format("Row %d: Code=[%s], Type=[%s]%n", i, code, type));
            }

            java.nio.file.Files.write(java.nio.file.Paths.get("inspection_result.txt"), sb.toString().getBytes());
        }
    }

    @Test
    public void fixSheet2() throws Exception {
        try (InputStream is = new FileInputStream(FILE_PATH);
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet2 = workbook.getSheetAt(1); // Monthly Sheet
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            String lastValidCode = null;
            int fixedCount = 0;

            for (int i = 0; i <= sheet2.getLastRowNum(); i++) {
                Row row = sheet2.getRow(i);
                if (row == null)
                    continue;

                Cell codeCell = row.getCell(0);
                Cell typeCell = row.getCell(1);

                String code = formatter.formatCellValue(codeCell, evaluator).trim();
                String type = formatter.formatCellValue(typeCell, evaluator).trim();

                if ("연애운".equals(type)) {
                    if (code.matches("\\d{3}")) {
                        lastValidCode = code;
                    } else {
                        System.out.println("Warning: Invalid code for Love luck at row " + i + ": " + code);
                    }
                } else if (("재물운".equals(type) || "기타운".equals(type)) && lastValidCode != null) {
                    if (!code.equals(lastValidCode)) {
                        System.out.println("Fixing row " + i + ": " + code + " -> " + lastValidCode);
                        if (codeCell == null)
                            codeCell = row.createCell(0);
                        codeCell.setCellValue(lastValidCode);
                        fixedCount++;
                    }
                }
            }

            System.out.println("Total fixed rows: " + fixedCount);

            try (FileOutputStream os = new FileOutputStream(FILE_PATH)) {
                workbook.write(os);
            }
        }
    }
}
