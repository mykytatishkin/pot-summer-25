package com.coherentsolutions.pot.insurance_service.unit.model;

import com.coherentsolutions.pot.insurance_service.model.Address;
import com.coherentsolutions.pot.insurance_service.model.Company;
import com.coherentsolutions.pot.insurance_service.model.Phone;
import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Company Entity Tests")
class CompanyTest {

    private Company company;
    private Address address;
    private Phone phone;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(UUID.randomUUID());
        company.setName("Test Company");
        company.setCountryCode("USA");
        company.setEmail("test@company.com");
        company.setWebsite("https://testcompany.com");
        company.setStatus(CompanyStatus.ACTIVE);
        company.setCreatedAt(Instant.now());
        company.setUpdatedAt(Instant.now());

        address = new Address();
        address.setCountry("USA");
        address.setCity("New York");
        address.setStreet("123 Main St");
        address.setBuilding("Building A");
        address.setRoom("Room 101");

        phone = new Phone();
        phone.setCode("+1");
        phone.setNumber("555-1234");

        company.setAddressData(List.of(address));
        company.setPhoneData(List.of(phone));
    }

    @Test
    @DisplayName("Should create company with all required fields")
    void shouldCreateCompanyWithAllRequiredFields() {
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
    }

    @Test
    @DisplayName("Should update company fields")
    void shouldUpdateCompanyFields() {
        // Given
        String newName = "Updated Company";
        String newCountryCode = "CAN";
        String newEmail = "updated@company.com";
        String newWebsite = "https://updatedcompany.com";

        // When
        company.setName(newName);
        company.setCountryCode(newCountryCode);
        company.setEmail(newEmail);
        company.setWebsite(newWebsite);

        // Then
        assertThat(company.getName()).isEqualTo(newName);
        assertThat(company.getCountryCode()).isEqualTo(newCountryCode);
        assertThat(company.getEmail()).isEqualTo(newEmail);
        assertThat(company.getWebsite()).isEqualTo(newWebsite);
    }

    @Test
    @DisplayName("Should handle optional fields as null")
    void shouldHandleOptionalFieldsAsNull() {
        // Given
        Company minimalCompany = new Company();
        minimalCompany.setName("Minimal Company");
        minimalCompany.setCountryCode("USA");

        // When & Then
        assertThat(minimalCompany.getEmail()).isNull();
        assertThat(minimalCompany.getWebsite()).isNull();
        assertThat(minimalCompany.getAddressData()).isNull();
        assertThat(minimalCompany.getPhoneData()).isNull();
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
        company.setStatus(CompanyStatus.ACTIVE);
        assertThat(company.getStatus()).isEqualTo(CompanyStatus.ACTIVE);

        company.setStatus(CompanyStatus.DEACTIVATED);
        assertThat(company.getStatus()).isEqualTo(CompanyStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("Should handle audit fields")
    void shouldHandleAuditFields() {
        // Given
        UUID createdBy = UUID.randomUUID();
        UUID updatedBy = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        // When
        company.setCreatedBy(createdBy);
        company.setUpdatedBy(updatedBy);
        company.setCreatedAt(createdAt);
        company.setUpdatedAt(updatedAt);

        // Then
        assertThat(company.getCreatedBy()).isEqualTo(createdBy);
        assertThat(company.getUpdatedBy()).isEqualTo(updatedBy);
        assertThat(company.getCreatedAt()).isEqualTo(createdAt);
        assertThat(company.getUpdatedAt()).isEqualTo(updatedAt);
    }
} 