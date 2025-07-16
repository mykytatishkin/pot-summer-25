package com.coherentsolutions.pot.insurance_service.service;

import com.coherentsolutions.pot.insurance_service.dto.CompanyDto;
import com.coherentsolutions.pot.insurance_service.dto.CompanyFilter;
import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import com.coherentsolutions.pot.insurance_service.mapper.CompanyMapper;
import com.coherentsolutions.pot.insurance_service.model.Address;
import com.coherentsolutions.pot.insurance_service.model.Company;
import com.coherentsolutions.pot.insurance_service.model.Phone;
import com.coherentsolutions.pot.insurance_service.repository.CompanyRepository;
import com.coherentsolutions.pot.insurance_service.repository.CompanySpecification;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Company Service Tests")
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyManagementService companyService;

    private Company testCompany;
    private CompanyDto testCompanyDto;
    private UUID testCompanyId;

    @BeforeEach
    void setUp() {
        testCompanyId = UUID.randomUUID();
        
        testCompany = new Company();
        testCompany.setId(testCompanyId);
        testCompany.setName("Test Company");
        testCompany.setStatus(CompanyStatus.ACTIVE);
        testCompany.setCountryCode("USA");
        testCompany.setEmail("test@company.com");
        testCompany.setWebsite("https://testcompany.com");
        testCompany.setCreatedAt(Instant.now());
        testCompany.setUpdatedAt(Instant.now());

        Address address = new Address();
        address.setStreet("123 Test St");
        address.setCity("Test City");
        address.setState("TS");
        address.setCountry("Test Country");

        Phone phone = new Phone();
        phone.setCode("+1");
        phone.setNumber("234567890");

        testCompany.setAddressData(List.of(address));
        testCompany.setPhoneData(List.of(phone));

        testCompanyDto = CompanyDto.builder()
                .id(testCompanyId)
                .name("Test Company")
                .status(CompanyStatus.ACTIVE)
                .countryCode("USA")
                .email("test@company.com")
                .website("https://testcompany.com")
                .build();
    }

    @Nested
    @DisplayName("Create Company Tests")
    class CreateCompanyTests {

        @Test
        @DisplayName("Should create company successfully")
        void shouldCreateCompanySuccessfully() {
            // Given
            when(companyMapper.toEntity(any(CompanyDto.class))).thenReturn(testCompany);
            when(companyMapper.toAddressList(any())).thenReturn(List.of());
            when(companyMapper.toPhoneList(any())).thenReturn(List.of());
            when(companyRepository.save(any(Company.class))).thenReturn(testCompany);
            when(companyMapper.toCompanyDto(any(Company.class))).thenReturn(testCompanyDto);

            // When
            CompanyDto result = companyService.createCompany(testCompanyDto);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testCompanyId);
            assertThat(result.getName()).isEqualTo("Test Company");
            assertThat(result.getStatus()).isEqualTo(CompanyStatus.ACTIVE);

            verify(companyMapper).toEntity(testCompanyDto);
            verify(companyMapper).toAddressList(testCompanyDto.getAddressData());
            verify(companyMapper).toPhoneList(testCompanyDto.getPhoneData());
            verify(companyRepository).save(testCompany);
            verify(companyMapper).toCompanyDto(testCompany);
        }
    }

    @Nested
    @DisplayName("Get Company Tests")
    class GetCompanyTests {

        @Test
        @DisplayName("Should get company by ID successfully")
        void shouldGetCompanyByIdSuccessfully() {
            // Given
            when(companyRepository.findById(testCompanyId)).thenReturn(java.util.Optional.of(testCompany));
            when(companyMapper.toCompanyDto(testCompany)).thenReturn(testCompanyDto);

            // When
            CompanyDto result = companyService.getCompanyDetails(testCompanyId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testCompanyId);
            assertThat(result.getName()).isEqualTo("Test Company");

            verify(companyRepository).findById(testCompanyId);
            verify(companyMapper).toCompanyDto(testCompany);
        }

        @Test
        @DisplayName("Should throw exception when company not found")
        void shouldThrowExceptionWhenCompanyNotFound() {
            // Given
            when(companyRepository.findById(testCompanyId)).thenReturn(java.util.Optional.empty());

            // When & Then
            assertThatThrownBy(() -> companyService.getCompanyDetails(testCompanyId))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);

            verify(companyRepository).findById(testCompanyId);
        }
    }

    @Nested
    @DisplayName("Update Company Tests")
    class UpdateCompanyTests {

        @Test
        @DisplayName("Should update company successfully")
        void shouldUpdateCompanySuccessfully() {
            // Given
            CompanyDto updateDto = CompanyDto.builder()
                    .name("Updated Company")
                    .status(CompanyStatus.DEACTIVATED)
                    .email("updated@company.com")
                    .build();

            Company updatedCompany = new Company();
            updatedCompany.setId(testCompanyId);
            updatedCompany.setName("Updated Company");
            updatedCompany.setStatus(CompanyStatus.DEACTIVATED);
            updatedCompany.setEmail("updated@company.com");

            when(companyRepository.findById(testCompanyId)).thenReturn(java.util.Optional.of(testCompany));
            when(companyRepository.save(any(Company.class))).thenReturn(updatedCompany);
            when(companyMapper.toCompanyDto(any(Company.class))).thenReturn(updateDto);

            // When
            CompanyDto result = companyService.updateCompany(testCompanyId, updateDto);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Updated Company");
            assertThat(result.getStatus()).isEqualTo(CompanyStatus.DEACTIVATED);

            verify(companyRepository).findById(testCompanyId);
            verify(companyRepository).save(any(Company.class));
            verify(companyMapper).toCompanyDto(any(Company.class));
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent company")
        void shouldThrowExceptionWhenUpdatingNonExistentCompany() {
            // Given
            when(companyRepository.findById(testCompanyId)).thenReturn(java.util.Optional.empty());

            // When & Then
            assertThatThrownBy(() -> companyService.updateCompany(testCompanyId, testCompanyDto))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);

            verify(companyRepository).findById(testCompanyId);
            verify(companyRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Companies With Filters Tests")
    class GetCompaniesWithFiltersTests {

        @Test
        @DisplayName("Should get companies with filters successfully")
        void shouldGetCompaniesWithFiltersSuccessfully() {
            // Given
            CompanyFilter filter = new CompanyFilter();
            filter.setName("Test");
            filter.setStatus(CompanyStatus.ACTIVE);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Company> companyPage = new PageImpl<>(List.of(testCompany), pageable, 1);

            when(companyRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(companyPage);
            when(companyMapper.toCompanyDto(testCompany)).thenReturn(testCompanyDto);

            // When
            Page<CompanyDto> result = companyService.getCompaniesWithFilters(filter, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getId()).isEqualTo(testCompanyId);

            verify(companyRepository).findAll(any(Specification.class), eq(pageable));
            verify(companyMapper).toCompanyDto(testCompany);
        }
    }
} 