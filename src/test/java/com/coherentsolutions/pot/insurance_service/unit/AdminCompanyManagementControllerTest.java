package com.coherentsolutions.pot.insurance_service.unit;

import com.coherentsolutions.pot.insurance_service.unit.AbstractControllerTest;
import com.coherentsolutions.pot.insurance_service.controller.AdminCompanyManagementController;
import com.coherentsolutions.pot.insurance_service.dto.CompanyDto;
import com.coherentsolutions.pot.insurance_service.dto.CompanyFilter;
import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import com.coherentsolutions.pot.insurance_service.service.CompanyManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.coherentsolutions.pot.insurance_service.exception.GlobalExceptionHandler;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Admin Company Management Controller Tests")
class AdminCompanyManagementControllerTest extends AbstractControllerTest {

    private static CompanyManagementService companyManagementService;
    private static AdminCompanyManagementController controller;

    @BeforeAll
    static void setUpClass() {
        companyManagementService = mock(CompanyManagementService.class);
        controller = new AdminCompanyManagementController(companyManagementService);
        initializeCommonObjects(controller);
    }

    @Test
    @DisplayName("Should get companies with search filters")
    void shouldGetCompaniesWithSearchFilters() throws Exception {
        // Reset mocks to ensure clean state
        resetMocks(companyManagementService);
        
        // Given
        CompanyDto testCompanyDto = createTestCompanyDto();
        UUID testCompanyId = testCompanyDto.getId();
        Pageable pageable = PageRequest.of(0, 10);
        Page<CompanyDto> companyPage = new PageImpl<>(List.of(testCompanyDto), pageable, 1);

        when(companyManagementService.getCompaniesWithFilters(any(CompanyFilter.class), eq(pageable)))
                .thenReturn(companyPage);

        // When & Then
        getMockMvc().perform(get("/v1/companies")
                        .param("page", "0")
                        .param("size", "10")
                        .param("name", "Test")
                        .param("countryCode", "USA")
                        .param("status", "ACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testCompanyId.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Test Company"))
                .andExpect(jsonPath("$.content[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(companyManagementService).getCompaniesWithFilters(any(CompanyFilter.class), eq(pageable));
    }

    @Test
    @DisplayName("Should create new company")
    void shouldCreateNewCompany() throws Exception {
        // Reset mocks to ensure clean state
        resetMocks(companyManagementService);
        
        // Given
        CompanyDto testCompanyDto = createTestCompanyDto();
        UUID testCompanyId = testCompanyDto.getId();
        CompanyDto createRequest = CompanyDto.builder()
                .name("New Company")
                .countryCode("USA")
                .email("new@company.com")
                .website("https://newcompany.com")
                .build();

        when(companyManagementService.createCompany(any(CompanyDto.class)))
                .thenReturn(testCompanyDto);

        // When & Then
        getMockMvc().perform(post("/v1/companies")
                        .content(getObjectMapper().writeValueAsString(createRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCompanyId.toString()))
                .andExpect(jsonPath("$.name").value("Test Company"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(companyManagementService).createCompany(any(CompanyDto.class));
    }

    @Test
    @DisplayName("Should get company details by ID")
    void shouldGetCompanyDetailsById() throws Exception {
        // Given
        CompanyDto testCompanyDto = createTestCompanyDto();
        UUID testCompanyId = testCompanyDto.getId();
        when(companyManagementService.getCompanyDetails(testCompanyId))
                .thenReturn(testCompanyDto);

        // When & Then
        getMockMvc().perform(get("/v1/companies/{id}", testCompanyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCompanyId.toString()))
                .andExpect(jsonPath("$.name").value("Test Company"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.countryCode").value("USA"))
                .andExpect(jsonPath("$.email").value("test@company.com"))
                .andExpect(jsonPath("$.website").value("https://testcompany.com"));

        verify(companyManagementService).getCompanyDetails(testCompanyId);
    }

    @Test
    @DisplayName("Should update company by ID")
    void shouldUpdateCompanyById() throws Exception {
        // Given
        CompanyDto updateRequest = CompanyDto.builder()
                .name("Updated Company")
                .countryCode("CAN")
                .email("updated@company.com")
                .website("https://updatedcompany.com")
                .build();

        UUID testCompanyId = createTestCompanyId();
        CompanyDto updatedCompany = CompanyDto.builder()
                .id(testCompanyId)
                .name("Updated Company")
                .countryCode("CAN")
                .email("updated@company.com")
                .website("https://updatedcompany.com")
                .status(CompanyStatus.ACTIVE)
                .build();

        when(companyManagementService.updateCompany(eq(testCompanyId), any(CompanyDto.class)))
                .thenReturn(updatedCompany);

        // When & Then
        getMockMvc().perform(put("/v1/companies/{id}", testCompanyId)
                        .content(getObjectMapper().writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCompanyId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Company"))
                .andExpect(jsonPath("$.countryCode").value("CAN"))
                .andExpect(jsonPath("$.email").value("updated@company.com"))
                .andExpect(jsonPath("$.website").value("https://updatedcompany.com"));

        verify(companyManagementService).updateCompany(eq(testCompanyId), any(CompanyDto.class));
    }

    @Test
    @DisplayName("Should return 404 when company not found by ID")
    void shouldReturn404WhenCompanyNotFound() throws Exception {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(companyManagementService.getCompanyDetails(nonExistentId))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Company not found"));

        // When & Then
        getMockMvc().perform(get("/v1/companies/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(companyManagementService).getCompanyDetails(nonExistentId);
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

        when(companyManagementService.updateCompany(eq(nonExistentId), any(CompanyDto.class)))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Company not found"));

        // When & Then
        getMockMvc().perform(put("/v1/companies/{id}", nonExistentId)
                        .content(getObjectMapper().writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(companyManagementService).updateCompany(eq(nonExistentId), any(CompanyDto.class));
    }

    @Test
    @DisplayName("Should return 400 when creating company with invalid JSON")
    void shouldReturn400WhenCreatingCompanyWithInvalidJson() throws Exception {
        // Given
        String invalidJson = "{ invalid json }";

        // When & Then
        getMockMvc().perform(post("/v1/companies")
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
        UUID testCompanyId = createTestCompanyId();
        getMockMvc().perform(put("/v1/companies/{id}", testCompanyId)
                        .content(invalidJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when creating company with empty request body")
    void shouldReturn400WhenCreatingCompanyWithEmptyBody() throws Exception {
        // When & Then
        getMockMvc().perform(post("/v1/companies")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when updating company with empty request body")
    void shouldReturn400WhenUpdatingCompanyWithEmptyBody() throws Exception {
        // When & Then
        UUID testCompanyId = createTestCompanyId();
        getMockMvc().perform(put("/v1/companies/{id}", testCompanyId)
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when creating company with null request body")
    void shouldReturn400WhenCreatingCompanyWithNullBody() throws Exception {
        // When & Then
        getMockMvc().perform(post("/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when updating company with null request body")
    void shouldReturn400WhenUpdatingCompanyWithNullBody() throws Exception {
        // When & Then
        UUID testCompanyId = createTestCompanyId();
        getMockMvc().perform(put("/v1/companies/{id}", testCompanyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 500 when service throws unexpected exception during creation")
    void shouldReturn500WhenServiceThrowsUnexpectedExceptionDuringCreation() throws Exception {
        // Given
        CompanyDto createRequest = CompanyDto.builder()
                .name("New Company")
                .countryCode("USA")
                .build();

        when(companyManagementService.createCompany(any(CompanyDto.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        getMockMvc().perform(post("/v1/companies")
                        .content(getObjectMapper().writeValueAsString(createRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(companyManagementService).createCompany(any(CompanyDto.class));
    }

    @Test
    @DisplayName("Should return 500 when service throws unexpected exception during update")
    void shouldReturn500WhenServiceThrowsUnexpectedExceptionDuringUpdate() throws Exception {
        // Given
        CompanyDto updateRequest = CompanyDto.builder()
                .name("Updated Company")
                .countryCode("CAN")
                .build();

        UUID testCompanyId = createTestCompanyId();
        when(companyManagementService.updateCompany(eq(testCompanyId), any(CompanyDto.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        getMockMvc().perform(put("/v1/companies/{id}", testCompanyId)
                        .content(getObjectMapper().writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(companyManagementService).updateCompany(eq(testCompanyId), any(CompanyDto.class));
    }

    @Test
    @DisplayName("Should return 500 when service throws unexpected exception during get details")
    void shouldReturn500WhenServiceThrowsUnexpectedExceptionDuringGetDetails() throws Exception {
        // Given
        UUID testCompanyId = createTestCompanyId();
        when(companyManagementService.getCompanyDetails(testCompanyId))
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        getMockMvc().perform(get("/v1/companies/{id}", testCompanyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(companyManagementService).getCompanyDetails(testCompanyId);
    }

    @Test
    @DisplayName("Should return 500 when service throws unexpected exception during get companies")
    void shouldReturn500WhenServiceThrowsUnexpectedExceptionDuringGetCompanies() throws Exception {
        // Reset mocks to ensure clean state
        resetMocks(companyManagementService);
        
        // Given
        when(companyManagementService.getCompaniesWithFilters(any(CompanyFilter.class), any(Pageable.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        getMockMvc().perform(get("/v1/companies")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(companyManagementService).getCompaniesWithFilters(any(CompanyFilter.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle invalid UUID format in path parameter")
    void shouldHandleInvalidUuidFormatInPathParameter() throws Exception {
        // When & Then
        getMockMvc().perform(get("/v1/companies/{id}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Create a simple DTO without Instant fields to avoid serialization issues
        CompanyDto simpleDto = CompanyDto.builder()
                .name("Test Company")
                .countryCode("USA")
                .build();

        getMockMvc().perform(put("/v1/companies/{id}", "invalid-uuid")
                        .content(getObjectMapper().writeValueAsString(simpleDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle malformed pagination parameters")
    void shouldHandleMalformedPaginationParameters() throws Exception {
        // When & Then - Spring Boot handles malformed pagination gracefully, so we expect 200
        getMockMvc().perform(get("/v1/companies")
                        .param("page", "invalid")
                        .param("size", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle negative pagination parameters")
    void shouldHandleNegativePaginationParameters() throws Exception {
        // When & Then - Spring Boot handles negative pagination gracefully, so we expect 200
        getMockMvc().perform(get("/v1/companies")
                        .param("page", "-1")
                        .param("size", "-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle extremely large pagination parameters")
    void shouldHandleExtremelyLargePaginationParameters() throws Exception {
        // When & Then - Spring Boot handles large pagination gracefully, so we expect 200
        getMockMvc().perform(get("/v1/companies")
                        .param("page", "999999999")
                        .param("size", "999999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle unsupported HTTP methods")
    void shouldHandleUnsupportedHttpMethods() throws Exception {
        // When & Then
        UUID testCompanyId = createTestCompanyId();
        getMockMvc().perform(delete("/v1/companies/{id}", testCompanyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        getMockMvc().perform(patch("/v1/companies/{id}", testCompanyId)
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
        getMockMvc().perform(post("/v1/companies")
                        .content(getObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isUnsupportedMediaType());

        UUID testCompanyId = createTestCompanyId();
        getMockMvc().perform(put("/v1/companies/{id}", testCompanyId)
                        .content(getObjectMapper().writeValueAsString(createRequest)))
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
        getMockMvc().perform(post("/v1/companies")
                        .content(getObjectMapper().writeValueAsString(createRequest))
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isUnsupportedMediaType());

        UUID testCompanyId = createTestCompanyId();
        getMockMvc().perform(put("/v1/companies/{id}", testCompanyId)
                        .content(getObjectMapper().writeValueAsString(createRequest))
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isUnsupportedMediaType());
    }
} 