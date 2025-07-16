package com.coherentsolutions.pot.insurance_service.dto;

import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Company DTO Tests")
class CompanyDtoTest {

    private UUID testCompanyId;
    private AddressDto testAddressDto;
    private PhoneDto testPhoneDto;

    @BeforeEach
    void setUp() {
        testCompanyId = UUID.randomUUID();
        testAddressDto = AddressDto.builder()
                .country("USA")
                .city("New York")
                .street("123 Main St")
                .building("Building A")
                .room("Room 101")
                .build();

        testPhoneDto = PhoneDto.builder()
                .code("+1")
                .number("555-1234")
                .build();
    }

    @Test
    @DisplayName("Should create company DTO with builder pattern")
    void shouldCreateCompanyDtoWithBuilderPattern() {
        // Given
        UUID createdBy = UUID.randomUUID();
        UUID updatedBy = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        // When
        CompanyDto companyDto = CompanyDto.builder()
                .id(testCompanyId)
                .name("Test Company")
                .countryCode("USA")
                .email("test@company.com")
                .website("https://testcompany.com")
                .status(CompanyStatus.ACTIVE)
                .addressData(List.of(testAddressDto))
                .phoneData(List.of(testPhoneDto))
                .createdBy(createdBy)
                .createdAt(createdAt)
                .updatedBy(updatedBy)
                .updatedAt(updatedAt)
                .build();

        // Then
        assertThat(companyDto).isNotNull();
        assertThat(companyDto.getId()).isEqualTo(testCompanyId);
        assertThat(companyDto.getName()).isEqualTo("Test Company");
        assertThat(companyDto.getCountryCode()).isEqualTo("USA");
        assertThat(companyDto.getEmail()).isEqualTo("test@company.com");
        assertThat(companyDto.getWebsite()).isEqualTo("https://testcompany.com");
        assertThat(companyDto.getStatus()).isEqualTo(CompanyStatus.ACTIVE);
        assertThat(companyDto.getAddressData()).hasSize(1);
        assertThat(companyDto.getPhoneData()).hasSize(1);
        assertThat(companyDto.getCreatedBy()).isEqualTo(createdBy);
        assertThat(companyDto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(companyDto.getUpdatedBy()).isEqualTo(updatedBy);
        assertThat(companyDto.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should create company DTO with minimal fields")
    void shouldCreateCompanyDtoWithMinimalFields() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .countryCode("USA")
                .build();

        // Then
        assertThat(companyDto).isNotNull();
        assertThat(companyDto.getName()).isEqualTo("Test Company");
        assertThat(companyDto.getCountryCode()).isEqualTo("USA");
        assertThat(companyDto.getId()).isNull();
        assertThat(companyDto.getEmail()).isNull();
        assertThat(companyDto.getWebsite()).isNull();
        assertThat(companyDto.getStatus()).isNull();
        assertThat(companyDto.getAddressData()).isNull();
        assertThat(companyDto.getPhoneData()).isNull();
        assertThat(companyDto.getCreatedBy()).isNull();
        assertThat(companyDto.getCreatedAt()).isNull();
        assertThat(companyDto.getUpdatedBy()).isNull();
        assertThat(companyDto.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should create company DTO with null values")
    void shouldCreateCompanyDtoWithNullValues() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .id(null)
                .name(null)
                .countryCode(null)
                .email(null)
                .website(null)
                .status(null)
                .addressData(null)
                .phoneData(null)
                .createdBy(null)
                .createdAt(null)
                .updatedBy(null)
                .updatedAt(null)
                .build();

        // Then
        assertThat(companyDto).isNotNull();
        assertThat(companyDto.getId()).isNull();
        assertThat(companyDto.getName()).isNull();
        assertThat(companyDto.getCountryCode()).isNull();
        assertThat(companyDto.getEmail()).isNull();
        assertThat(companyDto.getWebsite()).isNull();
        assertThat(companyDto.getStatus()).isNull();
        assertThat(companyDto.getAddressData()).isNull();
        assertThat(companyDto.getPhoneData()).isNull();
        assertThat(companyDto.getCreatedBy()).isNull();
        assertThat(companyDto.getCreatedAt()).isNull();
        assertThat(companyDto.getUpdatedBy()).isNull();
        assertThat(companyDto.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should handle different company statuses")
    void shouldHandleDifferentCompanyStatuses() {
        // When
        CompanyDto activeCompany = CompanyDto.builder()
                .name("Active Company")
                .status(CompanyStatus.ACTIVE)
                .build();

        CompanyDto deactivatedCompany = CompanyDto.builder()
                .name("Deactivated Company")
                .status(CompanyStatus.DEACTIVATED)
                .build();

        // Then
        assertThat(activeCompany.getStatus()).isEqualTo(CompanyStatus.ACTIVE);
        assertThat(deactivatedCompany.getStatus()).isEqualTo(CompanyStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("Should handle multiple addresses and phones")
    void shouldHandleMultipleAddressesAndPhones() {
        // Given
        AddressDto address2 = AddressDto.builder()
                .country("Canada")
                .city("Toronto")
                .street("456 Oak Ave")
                .build();

        PhoneDto phone2 = PhoneDto.builder()
                .code("+1")
                .number("555-5678")
                .build();

        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .addressData(List.of(testAddressDto, address2))
                .phoneData(List.of(testPhoneDto, phone2))
                .build();

        // Then
        assertThat(companyDto.getAddressData()).hasSize(2);
        assertThat(companyDto.getPhoneData()).hasSize(2);
        assertThat(companyDto.getAddressData().get(0).getCity()).isEqualTo("New York");
        assertThat(companyDto.getAddressData().get(1).getCity()).isEqualTo("Toronto");
        assertThat(companyDto.getPhoneData().get(0).getNumber()).isEqualTo("555-1234");
        assertThat(companyDto.getPhoneData().get(1).getNumber()).isEqualTo("555-5678");
    }

    @Test
    @DisplayName("Should handle empty address and phone lists")
    void shouldHandleEmptyAddressAndPhoneLists() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .addressData(List.of())
                .phoneData(List.of())
                .build();

        // Then
        assertThat(companyDto.getAddressData()).isEmpty();
        assertThat(companyDto.getPhoneData()).isEmpty();
    }

    @Test
    @DisplayName("Should handle long company name")
    void shouldHandleLongCompanyName() {
        // Given
        String longName = "This is a very long company name that might exceed normal expectations";

        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name(longName)
                .build();

        // Then
        assertThat(companyDto.getName()).isEqualTo(longName);
    }

    @Test
    @DisplayName("Should handle special characters in company name")
    void shouldHandleSpecialCharactersInCompanyName() {
        // Given
        String specialName = "Company & Sons, Inc. - Special Characters: @#$%^&*()";

        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name(specialName)
                .build();

        // Then
        assertThat(companyDto.getName()).isEqualTo(specialName);
    }

    @Test
    @DisplayName("Should handle different country codes")
    void shouldHandleDifferentCountryCodes() {
        // When
        CompanyDto usaCompany = CompanyDto.builder()
                .name("USA Company")
                .countryCode("USA")
                .build();

        CompanyDto canadaCompany = CompanyDto.builder()
                .name("Canada Company")
                .countryCode("CAN")
                .build();

        CompanyDto ukCompany = CompanyDto.builder()
                .name("UK Company")
                .countryCode("GBR")
                .build();

        // Then
        assertThat(usaCompany.getCountryCode()).isEqualTo("USA");
        assertThat(canadaCompany.getCountryCode()).isEqualTo("CAN");
        assertThat(ukCompany.getCountryCode()).isEqualTo("GBR");
    }

    @Test
    @DisplayName("Should handle complex email addresses")
    void shouldHandleComplexEmailAddresses() {
        // Given
        String complexEmail = "test+tag@company-domain.co.uk";

        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .email(complexEmail)
                .build();

        // Then
        assertThat(companyDto.getEmail()).isEqualTo(complexEmail);
    }

    @Test
    @DisplayName("Should handle complex website URLs")
    void shouldHandleComplexWebsiteUrls() {
        // Given
        String complexWebsite = "https://www.company-name.com/path/to/page?param=value#section";

        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .website(complexWebsite)
                .build();

        // Then
        assertThat(companyDto.getWebsite()).isEqualTo(complexWebsite);
    }

    @Test
    @DisplayName("Should handle instant timestamps correctly")
    void shouldHandleInstantTimestampsCorrectly() {
        // Given
        Instant pastTime = Instant.now().minusSeconds(86400); // 1 day ago
        Instant futureTime = Instant.now().plusSeconds(86400); // 1 day in future

        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .createdAt(pastTime)
                .updatedAt(futureTime)
                .build();

        // Then
        assertThat(companyDto.getCreatedAt()).isEqualTo(pastTime);
        assertThat(companyDto.getUpdatedAt()).isEqualTo(futureTime);
        assertThat(companyDto.getCreatedAt()).isBefore(companyDto.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle UUID fields correctly")
    void shouldHandleUuidFieldsCorrectly() {
        // Given
        UUID newId = UUID.randomUUID();
        UUID newCreatedBy = UUID.randomUUID();
        UUID newUpdatedBy = UUID.randomUUID();

        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .id(newId)
                .createdBy(newCreatedBy)
                .updatedBy(newUpdatedBy)
                .build();

        // Then
        assertThat(companyDto.getId()).isEqualTo(newId);
        assertThat(companyDto.getCreatedBy()).isEqualTo(newCreatedBy);
        assertThat(companyDto.getUpdatedBy()).isEqualTo(newUpdatedBy);
    }

    @Test
    @DisplayName("Should create equal DTOs with same values")
    void shouldCreateEqualDtosWithSameValues() {
        // Given
        UUID id = UUID.randomUUID();
        Instant timestamp = Instant.now();

        // When
        CompanyDto dto1 = CompanyDto.builder()
                .id(id)
                .name("Test Company")
                .countryCode("USA")
                .email("test@company.com")
                .website("https://testcompany.com")
                .status(CompanyStatus.ACTIVE)
                .addressData(List.of(testAddressDto))
                .phoneData(List.of(testPhoneDto))
                .createdBy(UUID.randomUUID())
                .createdAt(timestamp)
                .updatedBy(UUID.randomUUID())
                .updatedAt(timestamp)
                .build();

        CompanyDto dto2 = CompanyDto.builder()
                .id(id)
                .name("Test Company")
                .countryCode("USA")
                .email("test@company.com")
                .website("https://testcompany.com")
                .status(CompanyStatus.ACTIVE)
                .addressData(List.of(testAddressDto))
                .phoneData(List.of(testPhoneDto))
                .createdBy(UUID.randomUUID())
                .createdAt(timestamp)
                .updatedBy(UUID.randomUUID())
                .updatedAt(timestamp)
                .build();

        // Then
        assertThat(dto1).isNotEqualTo(dto2); // Different UUIDs for createdBy/updatedBy
        assertThat(dto1.getId()).isEqualTo(dto2.getId());
        assertThat(dto1.getName()).isEqualTo(dto2.getName());
        assertThat(dto1.getCountryCode()).isEqualTo(dto2.getCountryCode());
        assertThat(dto1.getEmail()).isEqualTo(dto2.getEmail());
        assertThat(dto1.getWebsite()).isEqualTo(dto2.getWebsite());
        assertThat(dto1.getStatus()).isEqualTo(dto2.getStatus());
    }
} 