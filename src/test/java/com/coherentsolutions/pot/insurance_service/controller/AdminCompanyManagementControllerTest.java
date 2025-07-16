package com.coherentsolutions.pot.insurance_service.controller;

import com.coherentsolutions.pot.insurance_service.dto.AddressDto;
import com.coherentsolutions.pot.insurance_service.dto.CompanyDto;
import com.coherentsolutions.pot.insurance_service.dto.CompanyFilter;
import com.coherentsolutions.pot.insurance_service.dto.PhoneDto;
import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import com.coherentsolutions.pot.insurance_service.service.CompanyManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Admin Company Management Controller Tests")
class AdminCompanyManagementControllerTest {

    @Mock
    private CompanyManagementService companyManagementService;

    @InjectMocks
    private AdminCompanyManagementController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID testCompanyId;
    private CompanyDto testCompanyDto;
    private AddressDto testAddressDto;
    private PhoneDto testPhoneDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        testCompanyId = UUID.randomUUID();
        testAddressDto = AddressDto.builder()
                .country("USA")
                .city("New York")
                .street("123 Main St")
                .build();

        testPhoneDto = PhoneDto.builder()
                .code("+1")
                .number("555-1234")
                .build();

        testCompanyDto = CompanyDto.builder()
                .id(testCompanyId)
                .name("Test Company")
                .countryCode("USA")
                .email("test@company.com")
                .website("https://testcompany.com")
                .status(CompanyStatus.ACTIVE)
                .addressData(List.of(testAddressDto))
                .phoneData(List.of(testPhoneDto))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Nested
    @DisplayName("GET /v1/companies")
    class GetCompanies {

        @Test
        @DisplayName("Should return paginated companies with filters")
        void shouldReturnPaginatedCompaniesWithFilters() throws Exception {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Page<CompanyDto> companyPage = new PageImpl<>(List.of(testCompanyDto), pageable, 1);

            when(companyManagementService.getCompaniesWithFilters(any(CompanyFilter.class), eq(pageable)))
                    .thenReturn(companyPage);

            // When & Then
            mockMvc.perform(get("/v1/companies")
                            .param("page", "0")
                            .param("size", "10")
                            .param("name", "Test")
                            .param("countryCode", "USA")
                            .param("status", "ACTIVE"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].id").value(testCompanyId.toString()))
                    .andExpect(jsonPath("$.content[0].name").value("Test Company"))
                    .andExpect(jsonPath("$.content[0].countryCode").value("USA"))
                    .andExpect(jsonPath("$.content[0].email").value("test@company.com"))
                    .andExpect(jsonPath("$.content[0].website").value("https://testcompany.com"))
                    .andExpect(jsonPath("$.content[0].status").value("ACTIVE"))
                    .andExpect(jsonPath("$.content[0].addressData").isArray())
                    .andExpect(jsonPath("$.content[0].phoneData").isArray())
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.totalPages").value(1));

            verify(companyManagementService).getCompaniesWithFilters(any(CompanyFilter.class), eq(pageable));
        }

        @Test
        @DisplayName("Should return empty page when no companies found")
        void shouldReturnEmptyPageWhenNoCompaniesFound() throws Exception {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Page<CompanyDto> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(companyManagementService.getCompaniesWithFilters(any(CompanyFilter.class), eq(pageable)))
                    .thenReturn(emptyPage);

            // When & Then
            mockMvc.perform(get("/v1/companies")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.totalPages").value(0));
        }
    }

    @Nested
    @DisplayName("POST /v1/companies")
    class CreateCompany {

        @Test
        @DisplayName("Should create company successfully")
        void shouldCreateCompanySuccessfully() throws Exception {
            // Given
            CompanyDto createRequest = CompanyDto.builder()
                    .name("New Company")
                    .countryCode("USA")
                    .email("new@company.com")
                    .addressData(List.of(testAddressDto))
                    .phoneData(List.of(testPhoneDto))
                    .build();

            when(companyManagementService.createCompany(any(CompanyDto.class)))
                    .thenReturn(testCompanyDto);

            // When & Then
            mockMvc.perform(post("/v1/companies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(testCompanyId.toString()))
                    .andExpect(jsonPath("$.name").value("Test Company"))
                    .andExpect(jsonPath("$.countryCode").value("USA"))
                    .andExpect(jsonPath("$.email").value("test@company.com"))
                    .andExpect(jsonPath("$.website").value("https://testcompany.com"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"))
                    .andExpect(jsonPath("$.addressData").isArray())
                    .andExpect(jsonPath("$.phoneData").isArray());

            verify(companyManagementService).createCompany(any(CompanyDto.class));
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
            // Given
            String invalidJson = "{\"name\": \"Test\", \"invalidField\": \"value\"";

            // When & Then
            mockMvc.perform(post("/v1/companies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /v1/companies/{id}")
    class GetCompanyDetails {

        @Test
        @DisplayName("Should return company details when company exists")
        void shouldReturnCompanyDetailsWhenCompanyExists() throws Exception {
            // Given
            when(companyManagementService.getCompanyDetails(testCompanyId))
                    .thenReturn(testCompanyDto);

            // When & Then
            mockMvc.perform(get("/v1/companies/{id}", testCompanyId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(testCompanyId.toString()))
                    .andExpect(jsonPath("$.name").value("Test Company"))
                    .andExpect(jsonPath("$.countryCode").value("USA"))
                    .andExpect(jsonPath("$.email").value("test@company.com"))
                    .andExpect(jsonPath("$.website").value("https://testcompany.com"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"))
                    .andExpect(jsonPath("$.addressData").isArray())
                    .andExpect(jsonPath("$.phoneData").isArray());

            verify(companyManagementService).getCompanyDetails(testCompanyId);
        }

        @Test
        @DisplayName("Should return 404 when company not found")
        void shouldReturn404WhenCompanyNotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(companyManagementService.getCompanyDetails(nonExistentId))
                    .thenThrow(new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.NOT_FOUND, "Company not found"));

            // When & Then
            mockMvc.perform(get("/v1/companies/{id}", nonExistentId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /v1/companies/{id}")
    class UpdateCompany {

        @Test
        @DisplayName("Should update company successfully")
        void shouldUpdateCompanySuccessfully() throws Exception {
            // Given
            CompanyDto updateRequest = CompanyDto.builder()
                    .name("Updated Company")
                    .email("updated@company.com")
                    .status(CompanyStatus.DEACTIVATED)
                    .build();

            CompanyDto updatedCompanyDto = CompanyDto.builder()
                    .id(testCompanyId)
                    .name("Updated Company")
                    .email("updated@company.com")
                    .status(CompanyStatus.DEACTIVATED)
                    .build();

            when(companyManagementService.updateCompany(eq(testCompanyId), any(CompanyDto.class)))
                    .thenReturn(updatedCompanyDto);

            // When & Then
            mockMvc.perform(put("/v1/companies/{id}", testCompanyId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(testCompanyId.toString()))
                    .andExpect(jsonPath("$.name").value("Updated Company"))
                    .andExpect(jsonPath("$.email").value("updated@company.com"))
                    .andExpect(jsonPath("$.status").value("DEACTIVATED"));

            verify(companyManagementService).updateCompany(eq(testCompanyId), any(CompanyDto.class));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent company")
        void shouldReturn404WhenUpdatingNonExistentCompany() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            CompanyDto updateRequest = CompanyDto.builder()
                    .name("Updated Company")
                    .build();

            when(companyManagementService.updateCompany(eq(nonExistentId), any(CompanyDto.class)))
                    .thenThrow(new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.NOT_FOUND, "Company not found"));

            // When & Then
            mockMvc.perform(put("/v1/companies/{id}", nonExistentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
            // Given
            String invalidJson = "{\"name\": \"Test\", \"invalidField\": \"value\"";

            // When & Then
            mockMvc.perform(put("/v1/companies/{id}", testCompanyId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());
        }
    }
} 