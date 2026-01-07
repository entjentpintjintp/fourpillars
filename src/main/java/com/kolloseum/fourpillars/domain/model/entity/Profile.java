package com.kolloseum.fourpillars.domain.model.entity;

import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.domain.model.enums.TimeBranch;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Profile {

    private final UUID id;
    private final LocalDate birthdate;
    private final TimeBranch birthTime;
    private final LocalDateTime updatedAt;

    public static Profile create(LocalDate birthdate, TimeBranch birthTime) {
        validateBirthDate(birthdate);
        validateBirthTime(birthTime);
        return new Profile(null, birthdate, birthTime, LocalDateTime.now());
    }

    public static Profile restore(UUID id, LocalDate birthdate, TimeBranch birthTime, LocalDateTime updatedAt) {
        return new Profile(id, birthdate, birthTime, updatedAt);
    }

    public Profile update(LocalDate birthdate, TimeBranch birthTime) {
        validateBirthDate(birthdate);
        validateBirthTime(birthTime);
        return new Profile(this.id, birthdate, birthTime, LocalDateTime.now());
    }

    public boolean isComplete() {
        return birthdate != null && birthTime != null;
    }

    private static void validateBirthDate(LocalDate birthdate) {
        if (birthdate.isBefore(LocalDate.of(1900, 1, 1))) {
            throw BusinessException.invalidBirthDate("Cannot calculate fortune for dates before 1900");
        }
    }

    private static void validateBirthTime(TimeBranch birthTime) {
        if (birthTime == null) {
            throw BusinessException.invalidRequest("Birth time is mandatory");
        }
    }
}