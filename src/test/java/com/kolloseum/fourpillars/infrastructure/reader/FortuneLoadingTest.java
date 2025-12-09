package com.kolloseum.fourpillars.infrastructure.reader;

import com.kolloseum.fourpillars.domain.model.vo.Fortune;
import com.kolloseum.fourpillars.infrastructure.file.FortuneExcelReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FortuneLoadingTest {

    private final FortuneExcelReader fortuneExcelReader = new FortuneExcelReader();

    @Test
    void testFortuneLoading() throws IOException {
        List<Fortune> fortunes = fortuneExcelReader.readFortunesFromExcel();
        assertNotNull(fortunes);
        assertFalse(fortunes.isEmpty());

        // Find a specific fortune (e.g., 111)
        Optional<Fortune> fortuneOpt = fortunes.stream()
                .filter(f -> "111".equals(f.getFortuneCode()))
                .findFirst();

        assertTrue(fortuneOpt.isPresent());
        Fortune fortune = fortuneOpt.get();

        System.out.println("Loaded Fortune: " + fortune);

        // Verify Main Sheet Data
        assertNotNull(fortune.getFortuneContent());
        assertFalse(fortune.getFortuneContent().isEmpty());

        // Verify Monthly Luck (Sheet 2)
        assertNotNull(fortune.getMonthlyFortunes());
        assertEquals(12, fortune.getMonthlyFortunes().size());
        assertNotNull(fortune.getMonthlyFortunes().get(0).getAffection());
        assertNotNull(fortune.getMonthlyFortunes().get(0).getWealth());

        // Verify Metadata (Sheet 3)
        assertNotNull(fortune.getAdviceBox());
        assertNotNull(fortune.getSymbol());
        assertNotNull(fortune.getSymbolChn());

        java.io.File outputFile = new java.io.File("fortune_load_test_result.txt");
        try (java.io.FileWriter writer = new java.io.FileWriter(outputFile)) {
            writer.write("Loaded Fortune: " + fortune + "\n");
            writer.write("Content: " + fortune.getFortuneContent() + "\n");
            writer.write("Monthly Fortunes: " + fortune.getMonthlyFortunes() + "\n");
            writer.write("AdviceBox: " + fortune.getAdviceBox() + "\n");
            writer.write("Symbol: " + fortune.getSymbol() + "\n");
            writer.write("SymbolChn: " + fortune.getSymbolChn() + "\n");
        }
    }
}
