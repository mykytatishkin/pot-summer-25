package com.coherentsolutions.pot.insurance_service.model;

import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Company Entity Tests")
class CompanyEntityTest {

    @Nested
    @DisplayName("Company Creation Tests")
    class CompanyCreationTests {

        @Test
        @DisplayName("Should create company with all fields")
        void shouldCreateCompanyWithAllFields() {
            // Given
            UUID id = UUID.randomUUID();
            String name = "Test Company";
            CompanyStatus status = CompanyStatus.ACTIVE;
            Address address = new Address();
            address.setStreet("123 Test St");
            address.setCity("Test City");
            address.setState("TS");
            address.setCountry("Test Country");

            Phone phone = new Phone();
            phone.setCode("+1");
            phone.setNumber("234567890");

            // When
            Company company = new Company();
            company.setId(id);
            company.setName(name);
            company.setStatus(status);
            company.setAddressData(List.of(address));
            company.setPhoneData(List.of(phone));

            // Then
            assertThat(company.getId()).isEqualTo(id);
            assertThat(company.getName()).isEqualTo(name);
            assertThat(company.getStatus()).isEqualTo(status);
            assertThat(company.getAddressData()).contains(address);
            assertThat(company.getPhoneData()).contains(phone);
        }

        @Test
        @DisplayName("Should create company with minimal fields")
        void shouldCreateCompanyWithMinimalFields() {
            // Given
            String name = "Minimal Company";

            // When
            Company company = new Company();
            company.setName(name);

            // Then
            assertThat(company.getName()).isEqualTo(name);
            assertThat(company.getId()).isNull();
            assertThat(company.getStatus()).isNull();
            assertThat(company.getAddressData()).isNull();
            assertThat(company.getPhoneData()).isNull();
        }
    }

    @Nested
    @DisplayName("Company Equality Tests")
    class CompanyEqualityTests {

        @Test
        @DisplayName("Should not be equal when same ID but different objects")
        void shouldNotBeEqualWhenSameIdButDifferentObjects() {
            // Given
            UUID id = UUID.randomUUID();
            Company company1 = new Company();
            company1.setId(id);
            company1.setName("Company 1");

            Company company2 = new Company();
            company2.setId(id);
            company2.setName("Company 2");

            // When & Then
            assertThat(company1).isNotEqualTo(company2);
            assertThat(company1.hashCode()).isNotEqualTo(company2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different IDs")
        void shouldNotBeEqualWhenDifferentIds() {
            // Given
            Company company1 = new Company();
            company1.setId(UUID.randomUUID());
            company1.setName("Company 1");

            Company company2 = new Company();
            company2.setId(UUID.randomUUID());
            company2.setName("Company 1");

            // When & Then
            assertThat(company1).isNotEqualTo(company2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToString() {
            // Given
            Company company = new Company();
            company.setId(UUID.randomUUID());

            // When & Then
            assertThat(company).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            Company company = new Company();
            company.setId(UUID.randomUUID());

            // When & Then
            assertThat(company).isNotEqualTo("String");
        }
    }

    @Nested
    @DisplayName("Company ToString Tests")
    class CompanyToStringTests {

        @Test
        @DisplayName("Should return default string representation")
        void shouldReturnDefaultStringRepresentation() {
            // Given
            UUID id = UUID.randomUUID();
            String name = "Test Company";
            Company company = new Company();
            company.setId(id);
            company.setName(name);

            // When
            String result = company.toString();

            // Then
            assertThat(result).contains("Company");
            assertThat(result).contains("@");
        }
    }

    @Nested
    @DisplayName("Company Status Tests")
    class CompanyStatusTests {

        @Test
        @DisplayName("Should set and get active status")
        void shouldSetAndGetActiveStatus() {
            // Given
            Company company = new Company();

            // When
            company.setStatus(CompanyStatus.ACTIVE);

            // Then
            assertThat(company.getStatus()).isEqualTo(CompanyStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should set and get deactivated status")
        void shouldSetAndGetDeactivatedStatus() {
            // Given
            Company company = new Company();

            // When
            company.setStatus(CompanyStatus.DEACTIVATED);

            // Then
            assertThat(company.getStatus()).isEqualTo(CompanyStatus.DEACTIVATED);
        }
    }

    @Nested
    @DisplayName("Company Address Tests")
    class CompanyAddressTests {

        @Test
        @DisplayName("Should set and get address data")
        void shouldSetAndGetAddressData() {
            // Given
            Company company = new Company();
            Address address = new Address();
            address.setStreet("123 Test St");
            address.setCity("Test City");

            // When
            company.setAddressData(List.of(address));

            // Then
            assertThat(company.getAddressData()).contains(address);
        }

        @Test
        @DisplayName("Should handle null address data")
        void shouldHandleNullAddressData() {
            // Given
            Company company = new Company();

            // When
            company.setAddressData(null);

            // Then
            assertThat(company.getAddressData()).isNull();
        }
    }

    @Nested
    @DisplayName("Company Phone Tests")
    class CompanyPhoneTests {

        @Test
        @DisplayName("Should set and get phone data")
        void shouldSetAndGetPhoneData() {
            // Given
            Company company = new Company();
            Phone phone = new Phone();
            phone.setCode("+1");
            phone.setNumber("234567890");

            // When
            company.setPhoneData(List.of(phone));

            // Then
            assertThat(company.getPhoneData()).contains(phone);
        }

        @Test
        @DisplayName("Should handle null phone data")
        void shouldHandleNullPhoneData() {
            // Given
            Company company = new Company();

            // When
            company.setPhoneData(null);

            // Then
            assertThat(company.getPhoneData()).isNull();
        }
    }
} 