package com.coherentsolutions.pot.insurance_service.controller;

import com.coherentsolutions.pot.insurance_service.dto.CompanyDto;
import com.coherentsolutions.pot.insurance_service.dto.CompanyFilter;
import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import com.coherentsolutions.pot.insurance_service.service.CompanyManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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

    private CompanyDto testCompanyDto;
    private UUID testCompanyId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
        
        testCompanyId = UUID.randomUUID();
        testCompanyDto = CompanyDto.builder()
                .id(testCompanyId)
                .name("Test Company")
                .status(CompanyStatus.ACTIVE)
                .countryCode("USA")
                .email("test@company.com")
                .website("https://testcompany.com")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should get companies with search filters")
    void shouldGetCompaniesWithSearchFilters() throws Exception {
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
        // Given
        CompanyDto createRequest = CompanyDto.builder()
                .name("New Company")
                .countryCode("USA")
                .email("new@company.com")
                .website("https://newcompany.com")
                .build();

        when(companyManagementService.createCompany(any(CompanyDto.class)))
                .thenReturn(testCompanyDto);

        // When & Then
        mockMvc.perform(post("/v1/companies")
                        .content(objectMapper.writeValueAsString(createRequest))
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
        when(companyManagementService.getCompanyDetails(testCompanyId))
                .thenReturn(testCompanyDto);

        // When & Then
        mockMvc.perform(get("/v1/companies/{id}", testCompanyId)
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
        mockMvc.perform(put("/v1/companies/{id}", testCompanyId)
                        .content(objectMapper.writeValueAsString(updateRequest))
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
} 