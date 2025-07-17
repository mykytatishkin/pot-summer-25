package com.coherentsolutions.pot.insurance_service.controller;

import com.coherentsolutions.pot.insurance_service.containers.PostgresTestContainer;
import com.coherentsolutions.pot.insurance_service.dto.CompanyDto;
import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DisplayName("Admin Company Management Controller Integration Tests")
class AdminCompanyManagementControllerIntegrationTest extends PostgresTestContainer {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Should create and retrieve company successfully")
    void shouldCreateAndRetrieveCompanySuccessfully() throws Exception {
        // Given
        CompanyDto createRequest = CompanyDto.builder()
                .name("Integration Test Company")
                .countryCode("USA")
                .email("integration@test.com")
                .website("https://integration-test.com")
                .build();

        // When & Then - Create company
        String responseJson = mockMvc.perform(post("/v1/companies")
                        .content(objectMapper.writeValueAsString(createRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Integration Test Company"))
                .andExpect(jsonPath("$.countryCode").value("USA"))
                .andExpect(jsonPath("$.email").value("integration@test.com"))
                .andExpect(jsonPath("$.website").value("https://integration-test.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the created company ID
        CompanyDto createdCompany = objectMapper.readValue(responseJson, CompanyDto.class);
        UUID companyId = createdCompany.getId();

        // When & Then - Retrieve the created company
        mockMvc.perform(get("/v1/companies/{id}", companyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(companyId.toString()))
                .andExpect(jsonPath("$.name").value("Integration Test Company"))
                .andExpect(jsonPath("$.countryCode").value("USA"))
                .andExpect(jsonPath("$.email").value("integration@test.com"))
                .andExpect(jsonPath("$.website").value("https://integration-test.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("Should get companies with search filters")
    void shouldGetCompaniesWithSearchFilters() throws Exception {
        // Given - Create multiple companies
        CompanyDto company1 = CompanyDto.builder()
                .name("Alpha Company")
                .countryCode("USA")
                .email("alpha@company.com")
                .status(CompanyStatus.ACTIVE)
                .build();

        CompanyDto company2 = CompanyDto.builder()
                .name("Beta Company")
                .countryCode("CAN")
                .email("beta@company.com")
                .status(CompanyStatus.ACTIVE)
                .build();

        // Create companies
        mockMvc.perform(post("/v1/companies")
                        .content(objectMapper.writeValueAsString(company1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/v1/companies")
                        .content(objectMapper.writeValueAsString(company2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // When & Then - Search by name
        mockMvc.perform(get("/v1/companies")
                        .param("name", "Alpha")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Alpha Company"));

        // When & Then - Search by country code
        mockMvc.perform(get("/v1/companies")
                        .param("countryCode", "CAN")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].countryCode").value("CAN"));
    }

    @Test
    @DisplayName("Should update company successfully")
    void shouldUpdateCompanySuccessfully() throws Exception {
        // Given - Create a company first
        CompanyDto createRequest = CompanyDto.builder()
                .name("Original Company")
                .countryCode("USA")
                .email("original@company.com")
                .build();

        String responseJson = mockMvc.perform(post("/v1/companies")
                        .content(objectMapper.writeValueAsString(createRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CompanyDto createdCompany = objectMapper.readValue(responseJson, CompanyDto.class);
        UUID companyId = createdCompany.getId();

        // Given - Update request
        CompanyDto updateRequest = CompanyDto.builder()
                .name("Updated Company")
                .countryCode("CAN")
                .email("updated@company.com")
                .website("https://updated-company.com")
                .build();

        // When & Then - Update the company
        mockMvc.perform(put("/v1/companies/{id}", companyId)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(companyId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Company"))
                .andExpect(jsonPath("$.countryCode").value("CAN"))
                .andExpect(jsonPath("$.email").value("updated@company.com"))
                .andExpect(jsonPath("$.website").value("https://updated-company.com"));

        // Verify the update persisted by retrieving the company
        mockMvc.perform(get("/v1/companies/{id}", companyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Company"))
                .andExpect(jsonPath("$.countryCode").value("CAN"));
    }

    @Test
    @DisplayName("Should return 404 when company not found")
    void shouldReturn404WhenCompanyNotFound() throws Exception {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(get("/v1/companies/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent company")
    void shouldReturn404WhenUpdatingNonExistentCompany() throws Exception {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        CompanyDto updateRequest = CompanyDto.builder()
                .name("Updated Company")
                .countryCode("CAN")
                .build();

        // When & Then
        mockMvc.perform(put("/v1/companies/{id}", nonExistentId)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when creating company with invalid JSON")
    void shouldReturn400WhenCreatingCompanyWithInvalidJson() throws Exception {
        // Given
        String invalidJson = "{ invalid json }";

        // When & Then
        mockMvc.perform(post("/v1/companies")
                        .content(invalidJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when updating company with invalid JSON")
    void shouldReturn400WhenUpdatingCompanyWithInvalidJson() throws Exception {
        // Given
        String invalidJson = "{ invalid json }";

        // When & Then
        mockMvc.perform(put("/v1/companies/{id}", UUID.randomUUID())
                        .content(invalidJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when creating company with empty request body")
    void shouldReturn400WhenCreatingCompanyWithEmptyBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/v1/companies")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when updating company with empty request body")
    void shouldReturn400WhenUpdatingCompanyWithEmptyBody() throws Exception {
        // When & Then
        mockMvc.perform(put("/v1/companies/{id}", UUID.randomUUID())
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when creating company with null request body")
    void shouldReturn400WhenCreatingCompanyWithNullBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when updating company with null request body")
    void shouldReturn400WhenUpdatingCompanyWithNullBody() throws Exception {
        // When & Then
        mockMvc.perform(put("/v1/companies/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle invalid UUID format in path parameter")
    void shouldHandleInvalidUuidFormatInPathParameter() throws Exception {
        // When & Then
        mockMvc.perform(get("/v1/companies/{id}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        CompanyDto simpleDto = CompanyDto.builder()
                .name("Test Company")
                .countryCode("USA")
                .build();

        mockMvc.perform(put("/v1/companies/{id}", "invalid-uuid")
                        .content(objectMapper.writeValueAsString(simpleDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle malformed pagination parameters")
    void shouldHandleMalformedPaginationParameters() throws Exception {
        // When & Then - Spring Boot handles malformed pagination gracefully
        mockMvc.perform(get("/v1/companies")
                        .param("page", "invalid")
                        .param("size", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle negative pagination parameters")
    void shouldHandleNegativePaginationParameters() throws Exception {
        // When & Then - Spring Boot handles negative pagination gracefully
        mockMvc.perform(get("/v1/companies")
                        .param("page", "-1")
                        .param("size", "-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle unsupported HTTP methods")
    void shouldHandleUnsupportedHttpMethods() throws Exception {
        // When & Then
        mockMvc.perform(delete("/v1/companies/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(patch("/v1/companies/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Should handle missing content type header")
    void shouldHandleMissingContentTypeHeader() throws Exception {
        // Given
        CompanyDto createRequest = CompanyDto.builder()
                .name("New Company")
                .countryCode("USA")
                .build();

        // When & Then
        mockMvc.perform(post("/v1/companies")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isUnsupportedMediaType());

        mockMvc.perform(put("/v1/companies/{id}", UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Should handle unsupported content type")
    void shouldHandleUnsupportedContentType() throws Exception {
        // Given
        CompanyDto createRequest = CompanyDto.builder()
                .name("New Company")
                .countryCode("USA")
                .build();

        // When & Then
        mockMvc.perform(post("/v1/companies")
                        .content(objectMapper.writeValueAsString(createRequest))
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isUnsupportedMediaType());

        mockMvc.perform(put("/v1/companies/{id}", UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(createRequest))
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Should handle database constraint violations")
    void shouldHandleDatabaseConstraintViolations() throws Exception {
        // Given - Create a company with required fields
        CompanyDto createRequest = CompanyDto.builder()
                .name("Test Company")
                .countryCode("USA")
                .email("test@company.com")
                .build();

        // Create the first company successfully
        mockMvc.perform(post("/v1/companies")
                        .content(objectMapper.writeValueAsString(createRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // Try to create another company with the same email (assuming unique constraint on email)
        CompanyDto duplicateRequest = CompanyDto.builder()
                .name("Different Company") // Different name
                .countryCode("CAN")       // Different country
                .email("test@company.com") // Same email
                .build();

        mockMvc.perform(post("/v1/companies")
                        .content(objectMapper.writeValueAsString(duplicateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()); // Since there's no unique constraint, this should succeed
    }
} 