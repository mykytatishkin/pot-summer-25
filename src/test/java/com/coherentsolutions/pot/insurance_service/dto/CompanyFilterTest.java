package com.coherentsolutions.pot.insurance_service.dto;

import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

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
        assertThat(filter).isNotNull();
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
    @DisplayName("Should set and get created date range filters")
    void shouldSetAndGetCreatedDateRangeFilters() {
        // Given
        Instant createdFrom = Instant.now().minusSeconds(86400); // 1 day ago
        Instant createdTo = Instant.now();

        // When
        filter.setCreatedFrom(createdFrom);
        filter.setCreatedTo(createdTo);

        // Then
        assertThat(filter.getCreatedFrom()).isEqualTo(createdFrom);
        assertThat(filter.getCreatedTo()).isEqualTo(createdTo);
    }

    @Test
    @DisplayName("Should set and get updated date range filters")
    void shouldSetAndGetUpdatedDateRangeFilters() {
        // Given
        Instant updatedFrom = Instant.now().minusSeconds(3600); // 1 hour ago
        Instant updatedTo = Instant.now();

        // When
        filter.setUpdatedFrom(updatedFrom);
        filter.setUpdatedTo(updatedTo);

        // Then
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
    @DisplayName("Should handle different company statuses")
    void shouldHandleDifferentCompanyStatuses() {
        // When
        filter.setStatus(CompanyStatus.ACTIVE);

        // Then
        assertThat(filter.getStatus()).isEqualTo(CompanyStatus.ACTIVE);

        // When
        filter.setStatus(CompanyStatus.DEACTIVATED);

        // Then
        assertThat(filter.getStatus()).isEqualTo(CompanyStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("Should handle different country codes")
    void shouldHandleDifferentCountryCodes() {
        // When
        filter.setCountryCode("USA");

        // Then
        assertThat(filter.getCountryCode()).isEqualTo("USA");

        // When
        filter.setCountryCode("CAN");

        // Then
        assertThat(filter.getCountryCode()).isEqualTo("CAN");

        // When
        filter.setCountryCode("GBR");

        // Then
        assertThat(filter.getCountryCode()).isEqualTo("GBR");
    }

    @Test
    @DisplayName("Should handle long company names")
    void shouldHandleLongCompanyNames() {
        // Given
        String longName = "This is a very long company name that might exceed normal expectations";

        // When
        filter.setName(longName);

        // Then
        assertThat(filter.getName()).isEqualTo(longName);
    }

    @Test
    @DisplayName("Should handle special characters in company name")
    void shouldHandleSpecialCharactersInCompanyName() {
        // Given
        String specialName = "Company & Sons, Inc. - Special Characters: @#$%^&*()";

        // When
        filter.setName(specialName);

        // Then
        assertThat(filter.getName()).isEqualTo(specialName);
    }

    @Test
    @DisplayName("Should handle instant timestamps correctly")
    void shouldHandleInstantTimestampsCorrectly() {
        // Given
        Instant pastTime = Instant.now().minusSeconds(86400); // 1 day ago
        Instant futureTime = Instant.now().plusSeconds(86400); // 1 day in future

        // When
        filter.setCreatedFrom(pastTime);
        filter.setCreatedTo(futureTime);
        filter.setUpdatedFrom(pastTime);
        filter.setUpdatedTo(futureTime);

        // Then
        assertThat(filter.getCreatedFrom()).isEqualTo(pastTime);
        assertThat(filter.getCreatedTo()).isEqualTo(futureTime);
        assertThat(filter.getUpdatedFrom()).isEqualTo(pastTime);
        assertThat(filter.getUpdatedTo()).isEqualTo(futureTime);
        assertThat(filter.getCreatedFrom()).isBefore(filter.getCreatedTo());
        assertThat(filter.getUpdatedFrom()).isBefore(filter.getUpdatedTo());
    }

    @Test
    @DisplayName("Should handle edge case timestamps")
    void shouldHandleEdgeCaseTimestamps() {
        // Given
        Instant epoch = Instant.EPOCH;
        Instant maxTime = Instant.MAX;

        // When
        filter.setCreatedFrom(epoch);
        filter.setCreatedTo(maxTime);
        filter.setUpdatedFrom(epoch);
        filter.setUpdatedTo(maxTime);

        // Then
        assertThat(filter.getCreatedFrom()).isEqualTo(epoch);
        assertThat(filter.getCreatedTo()).isEqualTo(maxTime);
        assertThat(filter.getUpdatedFrom()).isEqualTo(epoch);
        assertThat(filter.getUpdatedTo()).isEqualTo(maxTime);
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
        filter.setName("   ");
        filter.setCountryCode("  ");

        // Then
        assertThat(filter.getName()).isEqualTo("   ");
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
    @DisplayName("Should handle multiple filter updates")
    void shouldHandleMultipleFilterUpdates() {
        // Given
        String name1 = "Company 1";
        String name2 = "Company 2";
        String countryCode1 = "USA";
        String countryCode2 = "CAN";
        CompanyStatus status1 = CompanyStatus.ACTIVE;
        CompanyStatus status2 = CompanyStatus.DEACTIVATED;

        // When
        filter.setName(name1);
        filter.setCountryCode(countryCode1);
        filter.setStatus(status1);

        // Then
        assertThat(filter.getName()).isEqualTo(name1);
        assertThat(filter.getCountryCode()).isEqualTo(countryCode1);
        assertThat(filter.getStatus()).isEqualTo(status1);

        // When
        filter.setName(name2);
        filter.setCountryCode(countryCode2);
        filter.setStatus(status2);

        // Then
        assertThat(filter.getName()).isEqualTo(name2);
        assertThat(filter.getCountryCode()).isEqualTo(countryCode2);
        assertThat(filter.getStatus()).isEqualTo(status2);
    }

    @Test
    @DisplayName("Should handle complex filter combinations")
    void shouldHandleComplexFilterCombinations() {
        // Given
        String name = "Test Company";
        String countryCode = "USA";
        CompanyStatus status = CompanyStatus.ACTIVE;
        Instant createdFrom = Instant.now().minusSeconds(86400);
        Instant createdTo = Instant.now();
        Instant updatedFrom = Instant.now().minusSeconds(3600);
        Instant updatedTo = Instant.now();

        // When
        filter.setName(name);
        filter.setCountryCode(countryCode);
        filter.setStatus(status);
        filter.setCreatedFrom(createdFrom);
        filter.setCreatedTo(createdTo);
        filter.setUpdatedFrom(updatedFrom);
        filter.setUpdatedTo(updatedTo);

        // Then
        assertThat(filter.getName()).isEqualTo(name);
        assertThat(filter.getCountryCode()).isEqualTo(countryCode);
        assertThat(filter.getStatus()).isEqualTo(status);
        assertThat(filter.getCreatedFrom()).isEqualTo(createdFrom);
        assertThat(filter.getCreatedTo()).isEqualTo(createdTo);
        assertThat(filter.getUpdatedFrom()).isEqualTo(updatedFrom);
        assertThat(filter.getUpdatedTo()).isEqualTo(updatedTo);
    }
} 