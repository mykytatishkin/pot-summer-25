package com.coherentsolutions.pot.insurance_service.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Company Status Enum Tests")
class CompanyStatusEnumTest {

    @Test
    @DisplayName("Should have correct enum values")
    void shouldHaveCorrectEnumValues() {
        // Then
        assertThat(CompanyStatus.values()).hasSize(2);
        assertThat(CompanyStatus.ACTIVE).isNotNull();
        assertThat(CompanyStatus.DEACTIVATED).isNotNull();
    }

    @Test
    @DisplayName("Should have correct enum names")
    void shouldHaveCorrectEnumNames() {
        // Then
        assertThat(CompanyStatus.ACTIVE.name()).isEqualTo("ACTIVE");
        assertThat(CompanyStatus.DEACTIVATED.name()).isEqualTo("DEACTIVATED");
    }

    @Test
    @DisplayName("Should convert string to enum value")
    void shouldConvertStringToEnumValue() {
        // When
        CompanyStatus active = CompanyStatus.valueOf("ACTIVE");
        CompanyStatus deactivated = CompanyStatus.valueOf("DEACTIVATED");

        // Then
        assertThat(active).isEqualTo(CompanyStatus.ACTIVE);
        assertThat(deactivated).isEqualTo(CompanyStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("Should compare enum values correctly")
    void shouldCompareEnumValuesCorrectly() {
        // Then
        assertThat(CompanyStatus.ACTIVE).isEqualTo(CompanyStatus.ACTIVE);
        assertThat(CompanyStatus.DEACTIVATED).isEqualTo(CompanyStatus.DEACTIVATED);
        assertThat(CompanyStatus.ACTIVE).isNotEqualTo(CompanyStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("Should convert enum to string")
    void shouldConvertEnumToString() {
        // When
        String activeString = CompanyStatus.ACTIVE.toString();
        String deactivatedString = CompanyStatus.DEACTIVATED.toString();

        // Then
        assertThat(activeString).isEqualTo("ACTIVE");
        assertThat(deactivatedString).isEqualTo("DEACTIVATED");
    }
} 