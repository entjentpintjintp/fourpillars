package com.kolloseum.fourpillars.domain.model.entity;

import com.kolloseum.fourpillars.common.exception.BusinessException;
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
    private final LocalDateTime updatedAt;

    public static Profile create(LocalDate birthdate) {
        validateBirthDate(birthdate);
        return new Profile(null, birthdate, LocalDateTime.now());
    }
    
    public static Profile restore(UUID id, LocalDate birthdate, LocalDateTime updatedAt) {
        return new Profile(id, birthdate, updatedAt);
    }

    public Profile update(LocalDate birthdate) {
        validateBirthDate(birthdate);
        return new Profile(this.id, birthdate, LocalDateTime.now());
    }
    
    public boolean isComplete() {
        return birthdate != null;
    }
    
    private static void validateBirthDate(LocalDate birthdate) {
        if (birthdate.isBefore(LocalDate.of(1900, 1, 1))) {
            throw BusinessException.invalidBirthDate("Cannot calculate fortune for dates before 1900");
        }
    }
}