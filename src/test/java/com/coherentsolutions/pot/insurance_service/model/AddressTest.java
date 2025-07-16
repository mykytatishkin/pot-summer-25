package com.coherentsolutions.pot.insurance_service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Address Model Tests")
class AddressTest {

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setCountry("USA");
        address.setCity("New York");
        address.setState("NY");
        address.setStreet("123 Main St");
        address.setBuilding("Building A");
        address.setRoom("Room 101");
    }

    @Test
    @DisplayName("Should create address with all fields")
    void shouldCreateAddressWithAllFields() {
        // Then
        assertThat(address).isNotNull();
        assertThat(address.getCountry()).isEqualTo("USA");
        assertThat(address.getCity()).isEqualTo("New York");
        assertThat(address.getState()).isEqualTo("NY");
        assertThat(address.getStreet()).isEqualTo("123 Main St");
        assertThat(address.getBuilding()).isEqualTo("Building A");
        assertThat(address.getRoom()).isEqualTo("Room 101");
    }

    @Test
    @DisplayName("Should update address fields")
    void shouldUpdateAddressFields() {
        // Given
        String newCountry = "Canada";
        String newCity = "Toronto";
        String newState = "ON";
        String newStreet = "456 Oak Ave";
        String newBuilding = "Building B";
        String newRoom = "Room 202";

        // When
        address.setCountry(newCountry);
        address.setCity(newCity);
        address.setState(newState);
        address.setStreet(newStreet);
        address.setBuilding(newBuilding);
        address.setRoom(newRoom);

        // Then
        assertThat(address.getCountry()).isEqualTo(newCountry);
        assertThat(address.getCity()).isEqualTo(newCity);
        assertThat(address.getState()).isEqualTo(newState);
        assertThat(address.getStreet()).isEqualTo(newStreet);
        assertThat(address.getBuilding()).isEqualTo(newBuilding);
        assertThat(address.getRoom()).isEqualTo(newRoom);
    }

    @Test
    @DisplayName("Should handle null fields")
    void shouldHandleNullFields() {
        // When
        address.setCountry(null);
        address.setCity(null);
        address.setState(null);
        address.setStreet(null);
        address.setBuilding(null);
        address.setRoom(null);

        // Then
        assertThat(address.getCountry()).isNull();
        assertThat(address.getCity()).isNull();
        assertThat(address.getState()).isNull();
        assertThat(address.getStreet()).isNull();
        assertThat(address.getBuilding()).isNull();
        assertThat(address.getRoom()).isNull();
    }

    @Test
    @DisplayName("Should handle empty string fields")
    void shouldHandleEmptyStringFields() {
        // When
        address.setCountry("");
        address.setCity("");
        address.setState("");
        address.setStreet("");
        address.setBuilding("");
        address.setRoom("");

        // Then
        assertThat(address.getCountry()).isEmpty();
        assertThat(address.getCity()).isEmpty();
        assertThat(address.getState()).isEmpty();
        assertThat(address.getStreet()).isEmpty();
        assertThat(address.getBuilding()).isEmpty();
        assertThat(address.getRoom()).isEmpty();
    }

    @Test
    @DisplayName("Should handle long address fields")
    void shouldHandleLongAddressFields() {
        // Given
        String longCountry = "United States of America";
        String longCity = "New York City Metropolitan Area";
        String longState = "New York State";
        String longStreet = "This is a very long street name that might exceed normal expectations";
        String longBuilding = "This is a very long building name that might exceed normal expectations";
        String longRoom = "This is a very long room description that might exceed normal expectations";

        // When
        address.setCountry(longCountry);
        address.setCity(longCity);
        address.setState(longState);
        address.setStreet(longStreet);
        address.setBuilding(longBuilding);
        address.setRoom(longRoom);

        // Then
        assertThat(address.getCountry()).isEqualTo(longCountry);
        assertThat(address.getCity()).isEqualTo(longCity);
        assertThat(address.getState()).isEqualTo(longState);
        assertThat(address.getStreet()).isEqualTo(longStreet);
        assertThat(address.getBuilding()).isEqualTo(longBuilding);
        assertThat(address.getRoom()).isEqualTo(longRoom);
    }

    @Test
    @DisplayName("Should handle special characters in address fields")
    void shouldHandleSpecialCharactersInAddressFields() {
        // Given
        String specialCountry = "Côte d'Ivoire";
        String specialCity = "São Paulo";
        String specialState = "New York";
        String specialStreet = "123 Main St. & Oak Ave.";
        String specialBuilding = "Building A & B";
        String specialRoom = "Room 101-A";

        // When
        address.setCountry(specialCountry);
        address.setCity(specialCity);
        address.setState(specialState);
        address.setStreet(specialStreet);
        address.setBuilding(specialBuilding);
        address.setRoom(specialRoom);

        // Then
        assertThat(address.getCountry()).isEqualTo(specialCountry);
        assertThat(address.getCity()).isEqualTo(specialCity);
        assertThat(address.getState()).isEqualTo(specialState);
        assertThat(address.getStreet()).isEqualTo(specialStreet);
        assertThat(address.getBuilding()).isEqualTo(specialBuilding);
        assertThat(address.getRoom()).isEqualTo(specialRoom);
    }

    @Test
    @DisplayName("Should handle different country formats")
    void shouldHandleDifferentCountryFormats() {
        // When
        address.setCountry("USA");

        // Then
        assertThat(address.getCountry()).isEqualTo("USA");

        // When
        address.setCountry("CAN");

        // Then
        assertThat(address.getCountry()).isEqualTo("CAN");

        // When
        address.setCountry("GBR");

        // Then
        assertThat(address.getCountry()).isEqualTo("GBR");
    }

    @Test
    @DisplayName("Should handle different city formats")
    void shouldHandleDifferentCityFormats() {
        // When
        address.setCity("New York");

        // Then
        assertThat(address.getCity()).isEqualTo("New York");

        // When
        address.setCity("Los Angeles");

        // Then
        assertThat(address.getCity()).isEqualTo("Los Angeles");

        // When
        address.setCity("San Francisco");

        // Then
        assertThat(address.getCity()).isEqualTo("San Francisco");
    }

    @Test
    @DisplayName("Should handle different state formats")
    void shouldHandleDifferentStateFormats() {
        // When
        address.setState("NY");

        // Then
        assertThat(address.getState()).isEqualTo("NY");

        // When
        address.setState("CA");

        // Then
        assertThat(address.getState()).isEqualTo("CA");

        // When
        address.setState("TX");

        // Then
        assertThat(address.getState()).isEqualTo("TX");
    }

    @Test
    @DisplayName("Should handle different street formats")
    void shouldHandleDifferentStreetFormats() {
        // When
        address.setStreet("123 Main St");

        // Then
        assertThat(address.getStreet()).isEqualTo("123 Main St");

        // When
        address.setStreet("456 Oak Avenue");

        // Then
        assertThat(address.getStreet()).isEqualTo("456 Oak Avenue");

        // When
        address.setStreet("789 Pine Blvd.");

        // Then
        assertThat(address.getStreet()).isEqualTo("789 Pine Blvd.");
    }

    @Test
    @DisplayName("Should handle different building formats")
    void shouldHandleDifferentBuildingFormats() {
        // When
        address.setBuilding("Building A");

        // Then
        assertThat(address.getBuilding()).isEqualTo("Building A");

        // When
        address.setBuilding("Tower 1");

        // Then
        assertThat(address.getBuilding()).isEqualTo("Tower 1");

        // When
        address.setBuilding("Floor 5");

        // Then
        assertThat(address.getBuilding()).isEqualTo("Floor 5");
    }

    @Test
    @DisplayName("Should handle different room formats")
    void shouldHandleDifferentRoomFormats() {
        // When
        address.setRoom("Room 101");

        // Then
        assertThat(address.getRoom()).isEqualTo("Room 101");

        // When
        address.setRoom("Suite 200");

        // Then
        assertThat(address.getRoom()).isEqualTo("Suite 200");

        // When
        address.setRoom("Office 3A");

        // Then
        assertThat(address.getRoom()).isEqualTo("Office 3A");
    }
} 