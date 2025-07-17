package com.coherentsolutions.pot.insurance_service.unit.dto;

import com.coherentsolutions.pot.insurance_service.dto.CompanyFilter;
import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Company Filter Tests")
class CompanyFilterTest {

    private CompanyFilter filter;

    @BeforeEach
    void setUp() {
        filter = new CompanyFilter();
    }

    @Test
    @DisplayName("Should create empty filter")
    void shouldCreateEmptyFilter() {
        // Then
        assertThat(filter.getName()).isNull();
        assertThat(filter.getCountryCode()).isNull();
        assertThat(filter.getStatus()).isNull();
        assertThat(filter.getCreatedFrom()).isNull();
        assertThat(filter.getCreatedTo()).isNull();
        assertThat(filter.getUpdatedFrom()).isNull();
        assertThat(filter.getUpdatedTo()).isNull();
    }

    @Test
    @DisplayName("Should set and get name filter")
    void shouldSetAndGetNameFilter() {
        // Given
        String name = "Test Company";

        // When
        filter.setName(name);

        // Then
        assertThat(filter.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Should set and get country code filter")
    void shouldSetAndGetCountryCodeFilter() {
        // Given
        String countryCode = "USA";

        // When
        filter.setCountryCode(countryCode);

        // Then
        assertThat(filter.getCountryCode()).isEqualTo(countryCode);
    }

    @Test
    @DisplayName("Should set and get status filter")
    void shouldSetAndGetStatusFilter() {
        // Given
        CompanyStatus status = CompanyStatus.ACTIVE;

        // When
        filter.setStatus(status);

        // Then
        assertThat(filter.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should set and get date range filters")
    void shouldSetAndGetDateRangeFilters() {
        // Given
        Instant createdFrom = Instant.now().minusSeconds(3600);
        Instant createdTo = Instant.now();
        Instant updatedFrom = Instant.now().minusSeconds(1800);
        Instant updatedTo = Instant.now();

        // When
        filter.setCreatedFrom(createdFrom);
        filter.setCreatedTo(createdTo);
        filter.setUpdatedFrom(updatedFrom);
        filter.setUpdatedTo(updatedTo);

        // Then
        assertThat(filter.getCreatedFrom()).isEqualTo(createdFrom);
        assertThat(filter.getCreatedTo()).isEqualTo(createdTo);
        assertThat(filter.getUpdatedFrom()).isEqualTo(updatedFrom);
        assertThat(filter.getUpdatedTo()).isEqualTo(updatedTo);
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        // When
        filter.setName(null);
        filter.setCountryCode(null);
        filter.setStatus(null);
        filter.setCreatedFrom(null);
        filter.setCreatedTo(null);
        filter.setUpdatedFrom(null);
        filter.setUpdatedTo(null);

        // Then
        assertThat(filter.getName()).isNull();
        assertThat(filter.getCountryCode()).isNull();
        assertThat(filter.getStatus()).isNull();
        assertThat(filter.getCreatedFrom()).isNull();
        assertThat(filter.getCreatedTo()).isNull();
        assertThat(filter.getUpdatedFrom()).isNull();
        assertThat(filter.getUpdatedTo()).isNull();
    }

    @Test
    @DisplayName("Should handle different country codes")
    void shouldHandleDifferentCountryCodes() {
        // When
        filter.setCountryCode("USA");
        assertThat(filter.getCountryCode()).isEqualTo("USA");

        filter.setCountryCode("CAN");
        assertThat(filter.getCountryCode()).isEqualTo("CAN");

        filter.setCountryCode("GBR");
        assertThat(filter.getCountryCode()).isEqualTo("GBR");
    }

    @Test
    @DisplayName("Should handle different company statuses")
    void shouldHandleDifferentCompanyStatuses() {
        // When
        filter.setStatus(CompanyStatus.ACTIVE);
        assertThat(filter.getStatus()).isEqualTo(CompanyStatus.ACTIVE);

        filter.setStatus(CompanyStatus.DEACTIVATED);
        assertThat(filter.getStatus()).isEqualTo(CompanyStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("Should handle empty string values")
    void shouldHandleEmptyStringValues() {
        // When
        filter.setName("");
        filter.setCountryCode("");

        // Then
        assertThat(filter.getName()).isEmpty();
        assertThat(filter.getCountryCode()).isEmpty();
    }

    @Test
    @DisplayName("Should handle whitespace values")
    void shouldHandleWhitespaceValues() {
        // When
        filter.setName("  ");
        filter.setCountryCode("  ");

        // Then
        assertThat(filter.getName()).isEqualTo("  ");
        assertThat(filter.getCountryCode()).isEqualTo("  ");
    }

    @Test
    @DisplayName("Should handle case sensitive values")
    void shouldHandleCaseSensitiveValues() {
        // When
        filter.setName("Test Company");
        filter.setCountryCode("usa");

        // Then
        assertThat(filter.getName()).isEqualTo("Test Company");
        assertThat(filter.getCountryCode()).isEqualTo("usa");
    }

    @Test
    @DisplayName("Should update filter values")
    void shouldUpdateFilterValues() {
        // Given
        String initialName = "Initial Company";
        String updatedName = "Updated Company";
        String initialCountryCode = "USA";
        String updatedCountryCode = "CAN";

        // When
        filter.setName(initialName);
        filter.setCountryCode(initialCountryCode);

        // Then
        assertThat(filter.getName()).isEqualTo(initialName);
        assertThat(filter.getCountryCode()).isEqualTo(initialCountryCode);

        // When
        filter.setName(updatedName);
        filter.setCountryCode(updatedCountryCode);

        // Then
        assertThat(filter.getName()).isEqualTo(updatedName);
        assertThat(filter.getCountryCode()).isEqualTo(updatedCountryCode);
    }

    @Test
    @DisplayName("Should handle complex search scenarios")
    void shouldHandleComplexSearchScenarios() {
        // Given
        String companyName = "Test Company";
        String countryCode = "USA";
        CompanyStatus status = CompanyStatus.ACTIVE;
        Instant createdFrom = Instant.now().minusSeconds(7200);
        Instant createdTo = Instant.now();

        // When
        filter.setName(companyName);
        filter.setCountryCode(countryCode);
        filter.setStatus(status);
        filter.setCreatedFrom(createdFrom);
        filter.setCreatedTo(createdTo);

        // Then
        assertThat(filter.getName()).isEqualTo(companyName);
        assertThat(filter.getCountryCode()).isEqualTo(countryCode);
        assertThat(filter.getStatus()).isEqualTo(status);
        assertThat(filter.getCreatedFrom()).isEqualTo(createdFrom);
        assertThat(filter.getCreatedTo()).isEqualTo(createdTo);
    }
} 