package com.kolloseum.fourpillars.infrastructure.file;

import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.domain.model.vo.Fortune;
import com.kolloseum.fourpillars.domain.model.vo.MonthlyFortune;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Component
public class FortuneExcelReader {

    private static final String FORTUNE_FILE_NAME = "fortune.xlsx";

    // Sheet Indices
    private static final int SHEET_MAIN = 0;
    private static final int SHEET_MONTHLY = 1;
    private static final int SHEET_META = 2;

    @Cacheable("fortunes")
    public List<Fortune> readFortunesFromExcel() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/" + FORTUNE_FILE_NAME);

        if (!resource.exists()) {
            throw BusinessException.fileNotFound("Fortune file not found: " + FORTUNE_FILE_NAME);
        }

        try (InputStream inputStream = resource.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)) {

            return processFortuneWorkbook(workbook);
        }
    }

    private List<Fortune> processFortuneWorkbook(Workbook workbook) {
        Map<String, FortuneData> dataMap = new HashMap<>();
        DataFormatter dataFormatter = new DataFormatter();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        processMainSheet(workbook.getSheetAt(SHEET_MAIN), dataMap, dataFormatter, evaluator);

        if (workbook.getNumberOfSheets() > SHEET_MONTHLY) {
            processMonthlySheet(workbook.getSheetAt(SHEET_MONTHLY), dataMap, dataFormatter, evaluator);
        }
        if (workbook.getNumberOfSheets() > SHEET_META) {
            processMetaSheet(workbook.getSheetAt(SHEET_META), dataMap, dataFormatter, evaluator);
        }

        List<Fortune> fortunes = new ArrayList<>();
        for (FortuneData data : dataMap.values()) {
            fortunes.add(data.toFortune());
        }

        log.info("Successfully loaded {} fortune entries from {}", fortunes.size(), FORTUNE_FILE_NAME);
        return fortunes;
    }

    private void processMainSheet(Sheet sheet, Map<String, FortuneData> dataMap, DataFormatter formatter,
            FormulaEvaluator evaluator) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                continue;

            String code = getCellValueAsString(row.getCell(0), formatter, evaluator);
            String content = getCellValueAsString(row.getCell(1), formatter, evaluator);

            if (isValidCode(code)) {
                dataMap.computeIfAbsent(code, FortuneData::new).content = content;
            }
        }
    }

    private void processMonthlySheet(Sheet sheet, Map<String, FortuneData> dataMap, DataFormatter formatter,
            FormulaEvaluator evaluator) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                continue;

            String code = getCellValueAsString(row.getCell(0), formatter, evaluator);
            if (!isValidCode(code) || !dataMap.containsKey(code))
                continue;

            String type = getCellValueAsString(row.getCell(1), formatter, evaluator);
            List<String> monthlyLuck = new ArrayList<>();
            for (int c = 2; c <= 13; c++) {
                monthlyLuck.add(getCellValueAsString(row.getCell(c), formatter, evaluator));
            }

            if ("연애운".equals(type)) {
                dataMap.get(code).loveLuck = monthlyLuck;
            } else if ("재물운".equals(type)) {
                dataMap.get(code).wealthLuck = monthlyLuck;
            } else if ("기타운".equals(type)) {
                dataMap.get(code).miscLuck = monthlyLuck;
            }
        }
    }

    private void processMetaSheet(Sheet sheet, Map<String, FortuneData> dataMap, DataFormatter formatter,
            FormulaEvaluator evaluator) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                continue;

            String code = getCellValueAsString(row.getCell(0), formatter, evaluator);
            if (!isValidCode(code) || !dataMap.containsKey(code))
                continue;

            FortuneData data = dataMap.get(code);
            data.symbol = getCellValueAsString(row.getCell(1), formatter, evaluator);
            data.symbolChn = getCellValueAsString(row.getCell(2), formatter, evaluator);
            data.adviceBox = getCellValueAsString(row.getCell(3), formatter, evaluator);
        }
    }

    private String getCellValueAsString(Cell cell, DataFormatter dataFormatter, FormulaEvaluator evaluator) {
        if (cell == null) {
            return "";
        }
        return dataFormatter.formatCellValue(cell, evaluator).trim();
    }

    private boolean isValidCode(String code) {
        return code != null && code.matches("\\d{3}");
    }

    private static class FortuneData {
        String code;
        String content;
        List<String> loveLuck = new ArrayList<>();
        List<String> wealthLuck = new ArrayList<>();
        List<String> miscLuck = new ArrayList<>();
        String adviceBox;
        String symbol;
        String symbolChn;

        FortuneData(String code) {
            this.code = code;
        }

        Fortune toFortune() {
            List<MonthlyFortune> monthlyFortunes = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                String love = (loveLuck.size() > i) ? loveLuck.get(i) : "";
                String wealth = (wealthLuck.size() > i) ? wealthLuck.get(i) : "";
                String misc = (miscLuck.size() > i) ? miscLuck.get(i) : "";
                monthlyFortunes.add(MonthlyFortune.create(love, wealth, misc));
            }

            return Fortune.create(
                    code, content, monthlyFortunes, adviceBox, symbol, symbolChn);
        }
    }
}