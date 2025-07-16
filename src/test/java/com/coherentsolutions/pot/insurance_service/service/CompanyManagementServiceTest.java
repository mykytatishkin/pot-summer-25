package com.coherentsolutions.pot.insurance_service.service;

import com.coherentsolutions.pot.insurance_service.dto.AddressDto;
import com.coherentsolutions.pot.insurance_service.dto.CompanyDto;
import com.coherentsolutions.pot.insurance_service.dto.CompanyFilter;
import com.coherentsolutions.pot.insurance_service.dto.PhoneDto;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Company Management Service Tests")
class CompanyManagementServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyManagementService companyManagementService;

    private UUID testCompanyId;
    private Company testCompany;
    private CompanyDto testCompanyDto;
    private Address testAddress;
    private Phone testPhone;
    private AddressDto testAddressDto;
    private PhoneDto testPhoneDto;

    @BeforeEach
    void setUp() {
        testCompanyId = UUID.randomUUID();
        testAddress = new Address();
        testAddress.setCountry("USA");
        testAddress.setCity("New York");
        testAddress.setStreet("123 Main St");

        testPhone = new Phone();
        testPhone.setCode("+1");
        testPhone.setNumber("555-1234");

        testAddressDto = AddressDto.builder()
                .country("USA")
                .city("New York")
                .street("123 Main St")
                .build();

        testPhoneDto = PhoneDto.builder()
                .code("+1")
                .number("555-1234")
                .build();

        testCompany = new Company();
        testCompany.setId(testCompanyId);
        testCompany.setName("Test Company");
        testCompany.setCountryCode("USA");
        testCompany.setEmail("test@company.com");
        testCompany.setWebsite("https://testcompany.com");
        testCompany.setStatus(CompanyStatus.ACTIVE);
        testCompany.setAddressData(List.of(testAddress));
        testCompany.setPhoneData(List.of(testPhone));
        testCompany.setCreatedAt(Instant.now());
        testCompany.setUpdatedAt(Instant.now());

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
    @DisplayName("Get Companies With Filters")
    class GetCompaniesWithFilters {

        @Test
        @DisplayName("Should return paginated companies when filters are provided")
        void shouldReturnPaginatedCompaniesWhenFiltersProvided() {
            // Given
            CompanyFilter filter = new CompanyFilter();
            filter.setName("Test");
            filter.setCountryCode("USA");
            filter.setStatus(CompanyStatus.ACTIVE);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Company> companyPage = new PageImpl<>(List.of(testCompany), pageable, 1);

            when(companyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(companyPage);
            when(companyMapper.toCompanyDto(testCompany)).thenReturn(testCompanyDto);

            // When
            Page<CompanyDto> result = companyManagementService.getCompaniesWithFilters(filter, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0)).isEqualTo(testCompanyDto);
            verify(companyRepository).findAll(any(Specification.class), eq(pageable));
            verify(companyMapper).toCompanyDto(testCompany);
        }

        @Test
        @DisplayName("Should return empty page when no companies match filters")
        void shouldReturnEmptyPageWhenNoCompaniesMatchFilters() {
            // Given
            CompanyFilter filter = new CompanyFilter();
            filter.setName("NonExistent");
            Pageable pageable = PageRequest.of(0, 10);
            Page<Company> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(companyRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(emptyPage);

            // When
            Page<CompanyDto> result = companyManagementService.getCompaniesWithFilters(filter, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(companyRepository).findAll(any(Specification.class), eq(pageable));
            verify(companyMapper, never()).toCompanyDto(any());
        }
    }

    @Nested
    @DisplayName("Create Company")
    class CreateCompany {

        @Test
        @DisplayName("Should create company successfully")
        void shouldCreateCompanySuccessfully() {
            // Given
            CompanyDto createRequest = CompanyDto.builder()
                    .name("New Company")
                    .countryCode("USA")
                    .email("new@company.com")
                    .addressData(List.of(testAddressDto))
                    .phoneData(List.of(testPhoneDto))
                    .build();

            Company newCompany = new Company();
            newCompany.setId(UUID.randomUUID());
            newCompany.setName("New Company");
            newCompany.setStatus(CompanyStatus.ACTIVE);

            when(companyMapper.toEntity(createRequest)).thenReturn(newCompany);
            when(companyMapper.toAddressList(createRequest.getAddressData())).thenReturn(List.of(testAddress));
            when(companyMapper.toPhoneList(createRequest.getPhoneData())).thenReturn(List.of(testPhone));
            when(companyRepository.save(any(Company.class))).thenReturn(newCompany);
            when(companyMapper.toCompanyDto(newCompany)).thenReturn(testCompanyDto);

            // When
            CompanyDto result = companyManagementService.createCompany(createRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(testCompanyDto);
            verify(companyMapper).toEntity(createRequest);
            verify(companyMapper).toAddressList(createRequest.getAddressData());
            verify(companyMapper).toPhoneList(createRequest.getPhoneData());
            verify(companyRepository).save(any(Company.class));
            verify(companyMapper).toCompanyDto(newCompany);
        }

        @Test
        @DisplayName("Should set status to ACTIVE when creating company")
        void shouldSetStatusToActiveWhenCreatingCompany() {
            // Given
            CompanyDto createRequest = CompanyDto.builder()
                    .name("New Company")
                    .countryCode("USA")
                    .build();

            Company newCompany = new Company();
            when(companyMapper.toEntity(createRequest)).thenReturn(newCompany);
            when(companyRepository.save(any(Company.class))).thenReturn(newCompany);
            when(companyMapper.toCompanyDto(newCompany)).thenReturn(testCompanyDto);

            // When
            companyManagementService.createCompany(createRequest);

            // Then
            verify(companyRepository).save(argThat(company -> 
                company.getStatus() == CompanyStatus.ACTIVE));
        }
    }

    @Nested
    @DisplayName("Get Company Details")
    class GetCompanyDetails {

        @Test
        @DisplayName("Should return company details when company exists")
        void shouldReturnCompanyDetailsWhenCompanyExists() {
            // Given
            when(companyRepository.findById(testCompanyId)).thenReturn(Optional.of(testCompany));
            when(companyMapper.toCompanyDto(testCompany)).thenReturn(testCompanyDto);

            // When
            CompanyDto result = companyManagementService.getCompanyDetails(testCompanyId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(testCompanyDto);
            verify(companyRepository).findById(testCompanyId);
            verify(companyMapper).toCompanyDto(testCompany);
        }

        @Test
        @DisplayName("Should throw ResponseStatusException when company not found")
        void shouldThrowResponseStatusExceptionWhenCompanyNotFound() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(companyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> companyManagementService.getCompanyDetails(nonExistentId))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                    .hasMessageContaining("Company not found");

            verify(companyRepository).findById(nonExistentId);
            verify(companyMapper, never()).toCompanyDto(any());
        }
    }

    @Nested
    @DisplayName("Update Company")
    class UpdateCompany {

        @Test
        @DisplayName("Should update company successfully when company exists")
        void shouldUpdateCompanySuccessfullyWhenCompanyExists() {
            // Given
            CompanyDto updateRequest = CompanyDto.builder()
                    .name("Updated Company")
                    .email("updated@company.com")
                    .status(CompanyStatus.DEACTIVATED)
                    .build();

            Company updatedCompany = new Company();
            updatedCompany.setId(testCompanyId);
            updatedCompany.setName("Updated Company");
            updatedCompany.setStatus(CompanyStatus.DEACTIVATED);

            when(companyRepository.findById(testCompanyId)).thenReturn(Optional.of(testCompany));
            when(companyRepository.save(any(Company.class))).thenReturn(updatedCompany);
            when(companyMapper.toCompanyDto(updatedCompany)).thenReturn(testCompanyDto);

            // When
            CompanyDto result = companyManagementService.updateCompany(testCompanyId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(testCompanyDto);
            verify(companyRepository).findById(testCompanyId);
            verify(companyRepository).save(any(Company.class));
            verify(companyMapper).toCompanyDto(updatedCompany);
        }

        @Test
        @DisplayName("Should update only provided fields")
        void shouldUpdateOnlyProvidedFields() {
            // Given
            CompanyDto updateRequest = CompanyDto.builder()
                    .name("Updated Name")
                    .build();

            when(companyRepository.findById(testCompanyId)).thenReturn(Optional.of(testCompany));
            when(companyRepository.save(any(Company.class))).thenReturn(testCompany);
            when(companyMapper.toCompanyDto(testCompany)).thenReturn(testCompanyDto);

            // When
            companyManagementService.updateCompany(testCompanyId, updateRequest);

            // Then
            verify(companyRepository).save(argThat(company -> 
                company.getName().equals("Updated Name") &&
                company.getEmail().equals("test@company.com") && // Original value preserved
                company.getStatus() == CompanyStatus.ACTIVE // Original value preserved
            ));
        }

        @Test
        @DisplayName("Should update address data when provided")
        void shouldUpdateAddressDataWhenProvided() {
            // Given
            List<AddressDto> newAddressData = List.of(
                AddressDto.builder().country("Canada").city("Toronto").build()
            );
            List<Address> newAddressEntities = List.of(new Address());

            CompanyDto updateRequest = CompanyDto.builder()
                    .addressData(newAddressData)
                    .build();

            when(companyRepository.findById(testCompanyId)).thenReturn(Optional.of(testCompany));
            when(companyMapper.toAddressList(newAddressData)).thenReturn(newAddressEntities);
            when(companyRepository.save(any(Company.class))).thenReturn(testCompany);
            when(companyMapper.toCompanyDto(testCompany)).thenReturn(testCompanyDto);

            // When
            companyManagementService.updateCompany(testCompanyId, updateRequest);

            // Then
            verify(companyMapper).toAddressList(newAddressData);
            verify(companyRepository).save(argThat(company -> 
                company.getAddressData().equals(newAddressEntities)
            ));
        }

        @Test
        @DisplayName("Should update phone data when provided")
        void shouldUpdatePhoneDataWhenProvided() {
            // Given
            List<PhoneDto> newPhoneData = List.of(
                PhoneDto.builder().code("+44").number("123-4567").build()
            );
            List<Phone> newPhoneEntities = List.of(new Phone());

            CompanyDto updateRequest = CompanyDto.builder()
                    .phoneData(newPhoneData)
                    .build();

            when(companyRepository.findById(testCompanyId)).thenReturn(Optional.of(testCompany));
            when(companyMapper.toPhoneList(newPhoneData)).thenReturn(newPhoneEntities);
            when(companyRepository.save(any(Company.class))).thenReturn(testCompany);
            when(companyMapper.toCompanyDto(testCompany)).thenReturn(testCompanyDto);

            // When
            companyManagementService.updateCompany(testCompanyId, updateRequest);

            // Then
            verify(companyMapper).toPhoneList(newPhoneData);
            verify(companyRepository).save(argThat(company -> 
                company.getPhoneData().equals(newPhoneEntities)
            ));
        }

        @Test
        @DisplayName("Should throw ResponseStatusException when updating non-existent company")
        void shouldThrowResponseStatusExceptionWhenUpdatingNonExistentCompany() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            CompanyDto updateRequest = CompanyDto.builder()
                    .name("Updated Company")
                    .build();

            when(companyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> companyManagementService.updateCompany(nonExistentId, updateRequest))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                    .hasMessageContaining("Company not found");

            verify(companyRepository).findById(nonExistentId);
            verify(companyRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should update status when provided")
        void shouldUpdateStatusWhenProvided() {
            // Given
            CompanyDto updateRequest = CompanyDto.builder()
                    .status(CompanyStatus.DEACTIVATED)
                    .build();

            when(companyRepository.findById(testCompanyId)).thenReturn(Optional.of(testCompany));
            when(companyRepository.save(any(Company.class))).thenReturn(testCompany);
            when(companyMapper.toCompanyDto(testCompany)).thenReturn(testCompanyDto);

            // When
            companyManagementService.updateCompany(testCompanyId, updateRequest);

            // Then
            verify(companyRepository).save(argThat(company -> 
                company.getStatus() == CompanyStatus.DEACTIVATED
            ));
        }
    }
} 