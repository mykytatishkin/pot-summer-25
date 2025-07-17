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
    @DisplayName("Should handle optional fields as null")
    void shouldHandleOptionalFieldsAsNull() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .countryCode("USA")
                .email(null)
                .website(null)
                .addressData(null)
                .phoneData(null)
                .build();

        // Then
        assertThat(companyDto.getEmail()).isNull();
        assertThat(companyDto.getWebsite()).isNull();
        assertThat(companyDto.getAddressData()).isNull();
        assertThat(companyDto.getPhoneData()).isNull();
    }

    @Test
    @DisplayName("Should create DTO with null name - no validation at DTO level")
    void shouldCreateDtoWithNullName() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name(null)
                .countryCode("USA")
                .build();

        // Then
        assertThat(companyDto).isNotNull();
        assertThat(companyDto.getName()).isNull();
        assertThat(companyDto.getCountryCode()).isEqualTo("USA");
    }

    @Test
    @DisplayName("Should create DTO with null countryCode - no validation at DTO level")
    void shouldCreateDtoWithNullCountryCode() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .countryCode(null)
                .build();

        // Then
        assertThat(companyDto).isNotNull();
        assertThat(companyDto.getName()).isEqualTo("Test Company");
        assertThat(companyDto.getCountryCode()).isNull();
    }

    @Test
    @DisplayName("Should create DTO with empty name - no validation at DTO level")
    void shouldCreateDtoWithEmptyName() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("")
                .countryCode("USA")
                .build();

        // Then
        assertThat(companyDto).isNotNull();
        assertThat(companyDto.getName()).isEmpty();
        assertThat(companyDto.getCountryCode()).isEqualTo("USA");
    }

    @Test
    @DisplayName("Should create DTO with empty countryCode - no validation at DTO level")
    void shouldCreateDtoWithEmptyCountryCode() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .countryCode("")
                .build();

        // Then
        assertThat(companyDto).isNotNull();
        assertThat(companyDto.getName()).isEqualTo("Test Company");
        assertThat(companyDto.getCountryCode()).isEmpty();
    }

    @Test
    @DisplayName("Should create DTO with invalid email format - no validation at DTO level")
    void shouldCreateDtoWithInvalidEmailFormat() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name("Test Company")
                .countryCode("USA")
                .email("invalid-email-format")
                .build();

        // Then
        assertThat(companyDto).isNotNull();
        assertThat(companyDto.getName()).isEqualTo("Test Company");
        assertThat(companyDto.getCountryCode()).isEqualTo("USA");
        assertThat(companyDto.getEmail()).isEqualTo("invalid-email-format");
    }

    @Test
    @DisplayName("Should create DTO with all null required fields - no validation at DTO level")
    void shouldCreateDtoWithAllNullRequiredFields() {
        // When
        CompanyDto companyDto = CompanyDto.builder()
                .name(null)
                .countryCode(null)
                .build();

        // Then
        assertThat(companyDto).isNotNull();
        assertThat(companyDto.getName()).isNull();
        assertThat(companyDto.getCountryCode()).isNull();
    }

    @Test
    @DisplayName("Should demonstrate validation strategy - DTO has no validation, Entity has @NotBlank")
    void shouldDemonstrateValidationStrategy() {
        // Given: CompanyDto has no validation annotations
        CompanyDto dtoWithNullName = CompanyDto.builder()
                .name(null)  // This is allowed at DTO level
                .countryCode("USA")
                .build();

        CompanyDto dtoWithEmptyName = CompanyDto.builder()
                .name("")    // This is allowed at DTO level
                .countryCode("USA")
                .build();

        CompanyDto dtoWithValidName = CompanyDto.builder()
                .name("Valid Company")  // This is allowed at DTO level
                .countryCode("USA")
                .build();

        // Then: All DTOs can be created without validation errors
        assertThat(dtoWithNullName).isNotNull();
        assertThat(dtoWithNullName.getName()).isNull();

        assertThat(dtoWithEmptyName).isNotNull();
        assertThat(dtoWithEmptyName.getName()).isEmpty();

        assertThat(dtoWithValidName).isNotNull();
        assertThat(dtoWithValidName.getName()).isEqualTo("Valid Company");
    }
} 