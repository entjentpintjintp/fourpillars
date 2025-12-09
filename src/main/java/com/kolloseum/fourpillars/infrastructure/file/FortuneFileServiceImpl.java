package com.kolloseum.fourpillars.infrastructure.file;

import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.domain.model.vo.Fortune;
import com.kolloseum.fourpillars.domain.service.FortuneFileService;
import com.kolloseum.fourpillars.infrastructure.file.FortuneExcelReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FortuneFileServiceImpl implements FortuneFileService {

    private final FortuneExcelReader excelReader;

    @Override
    public List<Fortune> getAllFortunes() {
        try {
            List<Fortune> fortunes = excelReader.readFortunesFromExcel();
            log.debug("Successfully cached {} fortune entries", fortunes.size());
            return fortunes;
        } catch (IOException e) {
            throw BusinessException.fileReadError("Failed to read fortune file");
        } catch (Exception e) {
            throw BusinessException.excelProcessError("Failed to process fortune file");
        }
    }

    @Override
    public Optional<Fortune> getFortuneByCode(String fortuneCode) {
        if (fortuneCode == null || fortuneCode.trim().isEmpty()) {
            return Optional.empty();
        }

        return getAllFortunes().stream()
                .filter(fortune -> fortuneCode.equals(fortune.getFortuneCode()))
                .findFirst();
    }

    @Override
    public List<Fortune> getCachedFortunes() {
        return getAllFortunes();
    }
}