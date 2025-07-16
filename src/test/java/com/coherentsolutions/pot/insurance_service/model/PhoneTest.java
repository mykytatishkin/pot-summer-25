package com.coherentsolutions.pot.insurance_service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Phone Model Tests")
class PhoneTest {

    private Phone phone;

    @BeforeEach
    void setUp() {
        phone = new Phone();
        phone.setCode("+1");
        phone.setNumber("555-1234");
    }

    @Test
    @DisplayName("Should create phone with all fields")
    void shouldCreatePhoneWithAllFields() {
        // Then
        assertThat(phone).isNotNull();
        assertThat(phone.getCode()).isEqualTo("+1");
        assertThat(phone.getNumber()).isEqualTo("555-1234");
    }

    @Test
    @DisplayName("Should update phone fields")
    void shouldUpdatePhoneFields() {
        // Given
        String newCode = "+44";
        String newNumber = "123-4567";

        // When
        phone.setCode(newCode);
        phone.setNumber(newNumber);

        // Then
        assertThat(phone.getCode()).isEqualTo(newCode);
        assertThat(phone.getNumber()).isEqualTo(newNumber);
    }

    @Test
    @DisplayName("Should handle null fields")
    void shouldHandleNullFields() {
        // When
        phone.setCode(null);
        phone.setNumber(null);

        // Then
        assertThat(phone.getCode()).isNull();
        assertThat(phone.getNumber()).isNull();
    }

    @Test
    @DisplayName("Should handle empty string fields")
    void shouldHandleEmptyStringFields() {
        // When
        phone.setCode("");
        phone.setNumber("");

        // Then
        assertThat(phone.getCode()).isEmpty();
        assertThat(phone.getNumber()).isEmpty();
    }

    @Test
    @DisplayName("Should handle different country codes")
    void shouldHandleDifferentCountryCodes() {
        // When
        phone.setCode("+1");

        // Then
        assertThat(phone.getCode()).isEqualTo("+1");

        // When
        phone.setCode("+44");

        // Then
        assertThat(phone.getCode()).isEqualTo("+44");

        // When
        phone.setCode("+33");

        // Then
        assertThat(phone.getCode()).isEqualTo("+33");

        // When
        phone.setCode("+49");

        // Then
        assertThat(phone.getCode()).isEqualTo("+49");
    }

    @Test
    @DisplayName("Should handle different phone number formats")
    void shouldHandleDifferentPhoneNumberFormats() {
        // When
        phone.setNumber("555-1234");

        // Then
        assertThat(phone.getNumber()).isEqualTo("555-1234");

        // When
        phone.setNumber("555.1234");

        // Then
        assertThat(phone.getNumber()).isEqualTo("555.1234");

        // When
        phone.setNumber("555 1234");

        // Then
        assertThat(phone.getNumber()).isEqualTo("555 1234");

        // When
        phone.setNumber("(555) 123-4567");

        // Then
        assertThat(phone.getNumber()).isEqualTo("(555) 123-4567");

        // When
        phone.setNumber("+1-555-123-4567");

        // Then
        assertThat(phone.getNumber()).isEqualTo("+1-555-123-4567");
    }

    @Test
    @DisplayName("Should handle international phone numbers")
    void shouldHandleInternationalPhoneNumbers() {
        // When
        phone.setCode("+44");
        phone.setNumber("20 7946 0958");

        // Then
        assertThat(phone.getCode()).isEqualTo("+44");
        assertThat(phone.getNumber()).isEqualTo("20 7946 0958");

        // When
        phone.setCode("+33");
        phone.setNumber("1 42 86 42 42");

        // Then
        assertThat(phone.getCode()).isEqualTo("+33");
        assertThat(phone.getNumber()).isEqualTo("1 42 86 42 42");

        // When
        phone.setCode("+49");
        phone.setNumber("30 2270");

        // Then
        assertThat(phone.getCode()).isEqualTo("+49");
        assertThat(phone.getNumber()).isEqualTo("30 2270");
    }

    @Test
    @DisplayName("Should handle long phone numbers")
    void shouldHandleLongPhoneNumbers() {
        // Given
        String longNumber = "123456789012345678901234567890";

        // When
        phone.setNumber(longNumber);

        // Then
        assertThat(phone.getNumber()).isEqualTo(longNumber);
    }

    @Test
    @DisplayName("Should handle special characters in phone numbers")
    void shouldHandleSpecialCharactersInPhoneNumbers() {
        // When
        phone.setNumber("555-123-4567 ext. 123");

        // Then
        assertThat(phone.getNumber()).isEqualTo("555-123-4567 ext. 123");

        // When
        phone.setNumber("555-123-4567 #123");

        // Then
        assertThat(phone.getNumber()).isEqualTo("555-123-4567 #123");

        // When
        phone.setNumber("555-123-4567 x123");

        // Then
        assertThat(phone.getNumber()).isEqualTo("555-123-4567 x123");
    }

    @Test
    @DisplayName("Should handle mobile phone numbers")
    void shouldHandleMobilePhoneNumbers() {
        // When
        phone.setCode("+1");
        phone.setNumber("555-123-4567");

        // Then
        assertThat(phone.getCode()).isEqualTo("+1");
        assertThat(phone.getNumber()).isEqualTo("555-123-4567");

        // When
        phone.setCode("+44");
        phone.setNumber("7700 900123");

        // Then
        assertThat(phone.getCode()).isEqualTo("+44");
        assertThat(phone.getNumber()).isEqualTo("7700 900123");
    }

    @Test
    @DisplayName("Should handle toll-free numbers")
    void shouldHandleTollFreeNumbers() {
        // When
        phone.setCode("+1");
        phone.setNumber("800-123-4567");

        // Then
        assertThat(phone.getCode()).isEqualTo("+1");
        assertThat(phone.getNumber()).isEqualTo("800-123-4567");

        // When
        phone.setNumber("888-123-4567");

        // Then
        assertThat(phone.getNumber()).isEqualTo("888-123-4567");

        // When
        phone.setNumber("877-123-4567");

        // Then
        assertThat(phone.getNumber()).isEqualTo("877-123-4567");
    }

    @Test
    @DisplayName("Should handle emergency numbers")
    void shouldHandleEmergencyNumbers() {
        // When
        phone.setCode("+1");
        phone.setNumber("911");

        // Then
        assertThat(phone.getCode()).isEqualTo("+1");
        assertThat(phone.getNumber()).isEqualTo("911");

        // When
        phone.setCode("+44");
        phone.setNumber("999");

        // Then
        assertThat(phone.getCode()).isEqualTo("+44");
        assertThat(phone.getNumber()).isEqualTo("999");
    }

    @Test
    @DisplayName("Should handle fax numbers")
    void shouldHandleFaxNumbers() {
        // When
        phone.setCode("+1");
        phone.setNumber("555-123-4567");

        // Then
        assertThat(phone.getCode()).isEqualTo("+1");
        assertThat(phone.getNumber()).isEqualTo("555-123-4567");
    }

    @Test
    @DisplayName("Should handle extension numbers")
    void shouldHandleExtensionNumbers() {
        // When
        phone.setNumber("555-123-4567 ext. 123");

        // Then
        assertThat(phone.getNumber()).isEqualTo("555-123-4567 ext. 123");

        // When
        phone.setNumber("555-123-4567 x123");

        // Then
        assertThat(phone.getNumber()).isEqualTo("555-123-4567 x123");

        // When
        phone.setNumber("555-123-4567 #123");

        // Then
        assertThat(phone.getNumber()).isEqualTo("555-123-4567 #123");
    }
} 