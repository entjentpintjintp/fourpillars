package com.kolloseum.fourpillars.infrastructure.file;

import com.kolloseum.fourpillars.domain.model.vo.Fortune;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FortuneExcelReaderTest {

    @Test
    public void testReadFortunes() throws Exception {
        FortuneExcelReader reader = new FortuneExcelReader();
        List<Fortune> fortunes = reader.readFortunesFromExcel();

        System.out.println("Loaded " + fortunes.size() + " fortunes.");
        assertTrue(fortunes.size() > 32, "Should load more than 32 fortunes");

        // Check if code 752 is present (the one that caused the error)
        boolean has752 = fortunes.stream().anyMatch(f -> "752".equals(f.getFortuneCode()));
        assertTrue(has752, "Should contain fortune code 752");
    }
}
