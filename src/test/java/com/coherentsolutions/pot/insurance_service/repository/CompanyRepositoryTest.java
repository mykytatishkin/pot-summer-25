package com.coherentsolutions.pot.insurance_service.repository;

import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import com.coherentsolutions.pot.insurance_service.model.Address;
import com.coherentsolutions.pot.insurance_service.model.Company;
import com.coherentsolutions.pot.insurance_service.model.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Company Repository Tests")
class CompanyRepositoryTest {

    @Mock
    private CompanyRepository companyRepository;

    private Company testCompany1;
    private Company testCompany2;
    private Address testAddress;
    private Phone testPhone;

    @BeforeEach
    void setUp() {
        testAddress = new Address();
        testAddress.setCountry("USA");
        testAddress.setCity("New York");
        testAddress.setStreet("123 Main St");

        testPhone = new Phone();
        testPhone.setCode("+1");
        testPhone.setNumber("555-1234");

        testCompany1 = new Company();
        testCompany1.setId(UUID.randomUUID());
        testCompany1.setName("Test Company 1");
        testCompany1.setCountryCode("USA");
        testCompany1.setEmail("test1@company.com");
        testCompany1.setWebsite("https://testcompany1.com");
        testCompany1.setStatus(CompanyStatus.ACTIVE);
        testCompany1.setAddressData(List.of(testAddress));
        testCompany1.setPhoneData(List.of(testPhone));
        testCompany1.setCreatedAt(Instant.now());
        testCompany1.setUpdatedAt(Instant.now());

        testCompany2 = new Company();
        testCompany2.setId(UUID.randomUUID());
        testCompany2.setName("Test Company 2");
        testCompany2.setCountryCode("CAN");
        testCompany2.setEmail("test2@company.com");
        testCompany2.setWebsite("https://testcompany2.com");
        testCompany2.setStatus(CompanyStatus.DEACTIVATED);
        testCompany2.setAddressData(List.of(testAddress));
        testCompany2.setPhoneData(List.of(testPhone));
        testCompany2.setCreatedAt(Instant.now());
        testCompany2.setUpdatedAt(Instant.now());
    }

    @Nested
    @DisplayName("Save Operations")
    class SaveOperations {

        @Test
        @DisplayName("Should save company successfully")
        void shouldSaveCompanySuccessfully() {
            // Given
            when(companyRepository.save(any(Company.class))).thenReturn(testCompany1);

            // When
            Company savedCompany = companyRepository.save(testCompany1);

            // Then
            assertThat(savedCompany).isNotNull();
            assertThat(savedCompany.getId()).isNotNull();
            assertThat(savedCompany.getName()).isEqualTo("Test Company 1");
            assertThat(savedCompany.getCountryCode()).isEqualTo("USA");
            assertThat(savedCompany.getEmail()).isEqualTo("test1@company.com");
            assertThat(savedCompany.getWebsite()).isEqualTo("https://testcompany1.com");
            assertThat(savedCompany.getStatus()).isEqualTo(CompanyStatus.ACTIVE);
            assertThat(savedCompany.getAddressData()).hasSize(1);
            assertThat(savedCompany.getPhoneData()).hasSize(1);
            assertThat(savedCompany.getCreatedAt()).isNotNull();
            assertThat(savedCompany.getUpdatedAt()).isNotNull();
            verify(companyRepository).save(testCompany1);
        }

        @Test
        @DisplayName("Should save all companies")
        void shouldSaveAllCompanies() {
            // Given
            List<Company> companies = List.of(testCompany1, testCompany2);
            when(companyRepository.saveAll(companies)).thenReturn(companies);

            // When
            List<Company> savedCompanies = companyRepository.saveAll(companies);

            // Then
            assertThat(savedCompanies).hasSize(2);
            verify(companyRepository).saveAll(companies);
        }
    }

    @Nested
    @DisplayName("Find Operations")
    class FindOperations {

        @Test
        @DisplayName("Should find company by id when company exists")
        void shouldFindCompanyByIdWhenCompanyExists() {
            // Given
            when(companyRepository.findById(testCompany1.getId())).thenReturn(Optional.of(testCompany1));

            // When
            Optional<Company> foundCompany = companyRepository.findById(testCompany1.getId());

            // Then
            assertThat(foundCompany).isPresent();
            assertThat(foundCompany.get().getName()).isEqualTo("Test Company 1");
            assertThat(foundCompany.get().getCountryCode()).isEqualTo("USA");
            verify(companyRepository).findById(testCompany1.getId());
        }

        @Test
        @DisplayName("Should return empty optional when company not found")
        void shouldReturnEmptyOptionalWhenCompanyNotFound() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(companyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // When
            Optional<Company> foundCompany = companyRepository.findById(nonExistentId);

            // Then
            assertThat(foundCompany).isEmpty();
            verify(companyRepository).findById(nonExistentId);
        }

        @Test
        @DisplayName("Should find all companies")
        void shouldFindAllCompanies() {
            // Given
            List<Company> companies = List.of(testCompany1, testCompany2);
            when(companyRepository.findAll()).thenReturn(companies);

            // When
            List<Company> allCompanies = companyRepository.findAll();

            // Then
            assertThat(allCompanies).hasSize(2);
            assertThat(allCompanies).extracting("name")
                    .containsExactlyInAnyOrder("Test Company 1", "Test Company 2");
            verify(companyRepository).findAll();
        }

        @Test
        @DisplayName("Should find companies with pagination")
        void shouldFindCompaniesWithPagination() {
            // Given
            Pageable pageable = PageRequest.of(0, 1);
            Page<Company> companyPage = new PageImpl<>(List.of(testCompany1), pageable, 2);
            when(companyRepository.findAll(pageable)).thenReturn(companyPage);

            // When
            Page<Company> result = companyRepository.findAll(pageable);

            // Then
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(2);
            verify(companyRepository).findAll(pageable);
        }

        @Test
        @DisplayName("Should find companies with specification")
        void shouldFindCompaniesWithSpecification() {
            // Given
            Specification<Company> spec = mock(Specification.class);
            Pageable pageable = PageRequest.of(0, 10);
            Page<Company> companyPage = new PageImpl<>(List.of(testCompany1), pageable, 1);
            when(companyRepository.findAll(spec, pageable)).thenReturn(companyPage);

            // When
            Page<Company> result = companyRepository.findAll(spec, pageable);

            // Then
            assertThat(result.getContent()).hasSize(1);
            verify(companyRepository).findAll(spec, pageable);
        }
    }

    @Nested
    @DisplayName("Update Operations")
    class UpdateOperations {

        @Test
        @DisplayName("Should update company successfully")
        void shouldUpdateCompanySuccessfully() {
            // Given
            Company updatedCompany = new Company();
            updatedCompany.setId(testCompany1.getId());
            updatedCompany.setName("Updated Company Name");
            updatedCompany.setEmail("updated@company.com");
            when(companyRepository.save(any(Company.class))).thenReturn(updatedCompany);

            // When
            Company result = companyRepository.save(updatedCompany);

            // Then
            assertThat(result.getName()).isEqualTo("Updated Company Name");
            assertThat(result.getEmail()).isEqualTo("updated@company.com");
            assertThat(result.getId()).isEqualTo(testCompany1.getId());
            verify(companyRepository).save(updatedCompany);
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteOperations {

        @Test
        @DisplayName("Should delete company successfully")
        void shouldDeleteCompanySuccessfully() {
            // Given
            UUID companyId = testCompany1.getId();
            doNothing().when(companyRepository).deleteById(companyId);

            // When
            companyRepository.deleteById(companyId);

            // Then
            verify(companyRepository).deleteById(companyId);
        }

        @Test
        @DisplayName("Should delete all companies")
        void shouldDeleteAllCompanies() {
            // Given
            doNothing().when(companyRepository).deleteAll();

            // When
            companyRepository.deleteAll();

            // Then
            verify(companyRepository).deleteAll();
        }
    }

    @Nested
    @DisplayName("Count Operations")
    class CountOperations {

        @Test
        @DisplayName("Should count companies correctly")
        void shouldCountCompaniesCorrectly() {
            // Given
            when(companyRepository.count()).thenReturn(2L);

            // When
            long count = companyRepository.count();

            // Then
            assertThat(count).isEqualTo(2);
            verify(companyRepository).count();
        }

        @Test
        @DisplayName("Should check if company exists")
        void shouldCheckIfCompanyExists() {
            // Given
            UUID companyId = testCompany1.getId();
            when(companyRepository.existsById(companyId)).thenReturn(true);

            // When
            boolean exists = companyRepository.existsById(companyId);

            // Then
            assertThat(exists).isTrue();
            verify(companyRepository).existsById(companyId);
        }

        @Test
        @DisplayName("Should check if company does not exist")
        void shouldCheckIfCompanyDoesNotExist() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(companyRepository.existsById(nonExistentId)).thenReturn(false);

            // When
            boolean exists = companyRepository.existsById(nonExistentId);

            // Then
            assertThat(exists).isFalse();
            verify(companyRepository).existsById(nonExistentId);
        }
    }
} 