package com.coherentsolutions.pot.insurance_service.model;

import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Company Model Tests")
class CompanyTest {

    private Company company;
    private Address address;
    private Phone phone;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setCountry("USA");
        address.setCity("New York");
        address.setStreet("123 Main St");
        address.setBuilding("Building A");
        address.setRoom("Room 101");

        phone = new Phone();
        phone.setCode("+1");
        phone.setNumber("555-1234");

        company = new Company();
        company.setId(UUID.randomUUID());
        company.setName("Test Company");
        company.setCountryCode("USA");
        company.setEmail("test@company.com");
        company.setWebsite("https://testcompany.com");
        company.setStatus(CompanyStatus.ACTIVE);
        company.setAddressData(List.of(address));
        company.setPhoneData(List.of(phone));
        company.setCreatedBy(UUID.randomUUID());
        company.setCreatedAt(Instant.now());
        company.setUpdatedBy(UUID.randomUUID());
        company.setUpdatedAt(Instant.now());
    }

    @Test
    @DisplayName("Should create company with all fields")
    void shouldCreateCompanyWithAllFields() {
        // Then
        assertThat(company).isNotNull();
        assertThat(company.getId()).isNotNull();
        assertThat(company.getName()).isEqualTo("Test Company");
        assertThat(company.getCountryCode()).isEqualTo("USA");
        assertThat(company.getEmail()).isEqualTo("test@company.com");
        assertThat(company.getWebsite()).isEqualTo("https://testcompany.com");
        assertThat(company.getStatus()).isEqualTo(CompanyStatus.ACTIVE);
        assertThat(company.getAddressData()).hasSize(1);
        assertThat(company.getPhoneData()).hasSize(1);
        assertThat(company.getCreatedBy()).isNotNull();
        assertThat(company.getCreatedAt()).isNotNull();
        assertThat(company.getUpdatedBy()).isNotNull();
        assertThat(company.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should update company fields")
    void shouldUpdateCompanyFields() {
        // Given
        UUID newId = UUID.randomUUID();
        String newName = "Updated Company";
        String newCountryCode = "CAN";
        String newEmail = "updated@company.com";
        String newWebsite = "https://updatedcompany.com";
        CompanyStatus newStatus = CompanyStatus.DEACTIVATED;
        UUID newCreatedBy = UUID.randomUUID();
        UUID newUpdatedBy = UUID.randomUUID();
        Instant newCreatedAt = Instant.now().minusSeconds(3600);
        Instant newUpdatedAt = Instant.now();

        // When
        company.setId(newId);
        company.setName(newName);
        company.setCountryCode(newCountryCode);
        company.setEmail(newEmail);
        company.setWebsite(newWebsite);
        company.setStatus(newStatus);
        company.setCreatedBy(newCreatedBy);
        company.setUpdatedBy(newUpdatedBy);
        company.setCreatedAt(newCreatedAt);
        company.setUpdatedAt(newUpdatedAt);

        // Then
        assertThat(company.getId()).isEqualTo(newId);
        assertThat(company.getName()).isEqualTo(newName);
        assertThat(company.getCountryCode()).isEqualTo(newCountryCode);
        assertThat(company.getEmail()).isEqualTo(newEmail);
        assertThat(company.getWebsite()).isEqualTo(newWebsite);
        assertThat(company.getStatus()).isEqualTo(newStatus);
        assertThat(company.getCreatedBy()).isEqualTo(newCreatedBy);
        assertThat(company.getUpdatedBy()).isEqualTo(newUpdatedBy);
        assertThat(company.getCreatedAt()).isEqualTo(newCreatedAt);
        assertThat(company.getUpdatedAt()).isEqualTo(newUpdatedAt);
    }

    @Test
    @DisplayName("Should handle null fields")
    void shouldHandleNullFields() {
        // When
        company.setEmail(null);
        company.setWebsite(null);
        company.setStatus(null);
        company.setAddressData(null);
        company.setPhoneData(null);
        company.setCreatedBy(null);
        company.setUpdatedBy(null);
        company.setCreatedAt(null);
        company.setUpdatedAt(null);

        // Then
        assertThat(company.getEmail()).isNull();
        assertThat(company.getWebsite()).isNull();
        assertThat(company.getStatus()).isNull();
        assertThat(company.getAddressData()).isNull();
        assertThat(company.getPhoneData()).isNull();
        assertThat(company.getCreatedBy()).isNull();
        assertThat(company.getUpdatedBy()).isNull();
        assertThat(company.getCreatedAt()).isNull();
        assertThat(company.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should handle empty address and phone data")
    void shouldHandleEmptyAddressAndPhoneData() {
        // When
        company.setAddressData(List.of());
        company.setPhoneData(List.of());

        // Then
        assertThat(company.getAddressData()).isEmpty();
        assertThat(company.getPhoneData()).isEmpty();
    }

    @Test
    @DisplayName("Should handle multiple addresses and phones")
    void shouldHandleMultipleAddressesAndPhones() {
        // Given
        Address address2 = new Address();
        address2.setCountry("Canada");
        address2.setCity("Toronto");
        address2.setStreet("456 Oak Ave");

        Phone phone2 = new Phone();
        phone2.setCode("+1");
        phone2.setNumber("555-5678");

        // When
        company.setAddressData(List.of(address, address2));
        company.setPhoneData(List.of(phone, phone2));

        // Then
        assertThat(company.getAddressData()).hasSize(2);
        assertThat(company.getPhoneData()).hasSize(2);
        assertThat(company.getAddressData().get(0).getCity()).isEqualTo("New York");
        assertThat(company.getAddressData().get(1).getCity()).isEqualTo("Toronto");
        assertThat(company.getPhoneData().get(0).getNumber()).isEqualTo("555-1234");
        assertThat(company.getPhoneData().get(1).getNumber()).isEqualTo("555-5678");
    }

    @Test
    @DisplayName("Should handle different company statuses")
    void shouldHandleDifferentCompanyStatuses() {
        // When
        company.setStatus(CompanyStatus.DEACTIVATED);

        // Then
        assertThat(company.getStatus()).isEqualTo(CompanyStatus.DEACTIVATED);

        // When
        company.setStatus(CompanyStatus.ACTIVE);

        // Then
        assertThat(company.getStatus()).isEqualTo(CompanyStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should handle long company name")
    void shouldHandleLongCompanyName() {
        // Given
        String longName = "This is a very long company name that might exceed normal expectations";

        // When
        company.setName(longName);

        // Then
        assertThat(company.getName()).isEqualTo(longName);
    }

    @Test
    @DisplayName("Should handle special characters in company name")
    void shouldHandleSpecialCharactersInCompanyName() {
        // Given
        String specialName = "Company & Sons, Inc. - Special Characters: @#$%^&*()";

        // When
        company.setName(specialName);

        // Then
        assertThat(company.getName()).isEqualTo(specialName);
    }

    @Test
    @DisplayName("Should handle different country codes")
    void shouldHandleDifferentCountryCodes() {
        // When
        company.setCountryCode("CAN");

        // Then
        assertThat(company.getCountryCode()).isEqualTo("CAN");

        // When
        company.setCountryCode("GBR");

        // Then
        assertThat(company.getCountryCode()).isEqualTo("GBR");
    }

    @Test
    @DisplayName("Should handle complex email addresses")
    void shouldHandleComplexEmailAddresses() {
        // Given
        String complexEmail = "test+tag@company-domain.co.uk";

        // When
        company.setEmail(complexEmail);

        // Then
        assertThat(company.getEmail()).isEqualTo(complexEmail);
    }

    @Test
    @DisplayName("Should handle complex website URLs")
    void shouldHandleComplexWebsiteUrls() {
        // Given
        String complexWebsite = "https://www.company-name.com/path/to/page?param=value#section";

        // When
        company.setWebsite(complexWebsite);

        // Then
        assertThat(company.getWebsite()).isEqualTo(complexWebsite);
    }

    @Test
    @DisplayName("Should handle instant timestamps correctly")
    void shouldHandleInstantTimestampsCorrectly() {
        // Given
        Instant pastTime = Instant.now().minusSeconds(86400); // 1 day ago
        Instant futureTime = Instant.now().plusSeconds(86400); // 1 day in future

        // When
        company.setCreatedAt(pastTime);
        company.setUpdatedAt(futureTime);

        // Then
        assertThat(company.getCreatedAt()).isEqualTo(pastTime);
        assertThat(company.getUpdatedAt()).isEqualTo(futureTime);
        assertThat(company.getCreatedAt()).isBefore(company.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle UUID fields correctly")
    void shouldHandleUuidFieldsCorrectly() {
        // Given
        UUID newId = UUID.randomUUID();
        UUID newCreatedBy = UUID.randomUUID();
        UUID newUpdatedBy = UUID.randomUUID();

        // When
        company.setId(newId);
        company.setCreatedBy(newCreatedBy);
        company.setUpdatedBy(newUpdatedBy);

        // Then
        assertThat(company.getId()).isEqualTo(newId);
        assertThat(company.getCreatedBy()).isEqualTo(newCreatedBy);
        assertThat(company.getUpdatedBy()).isEqualTo(newUpdatedBy);
    }
} 