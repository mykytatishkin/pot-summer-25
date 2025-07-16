package com.coherentsolutions.pot.insurance_service.repository;

import com.coherentsolutions.pot.insurance_service.containers.PostgresTestContainer;
import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import com.coherentsolutions.pot.insurance_service.model.Address;
import com.coherentsolutions.pot.insurance_service.model.Company;
import com.coherentsolutions.pot.insurance_service.model.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Company Repository Tests")
class CompanyRepositoryTest extends PostgresTestContainer {

    @Autowired
    private CompanyRepository companyRepository;

    private Company testCompany1;
    private Company testCompany2;
    private Address testAddress;
    private Phone testPhone;

    @BeforeEach
    void setUp() {
        companyRepository.deleteAll();

        testAddress = new Address();
        testAddress.setCountry("USA");
        testAddress.setCity("New York");
        testAddress.setStreet("123 Main St");

        testPhone = new Phone();
        testPhone.setCode("+1");
        testPhone.setNumber("555-1234");

        testCompany1 = new Company();
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

    @Test
    @DisplayName("Should save company successfully")
    void shouldSaveCompanySuccessfully() {
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
    }

    @Test
    @DisplayName("Should find company by id when company exists")
    void shouldFindCompanyByIdWhenCompanyExists() {
        // Given
        Company savedCompany = companyRepository.save(testCompany1);

        // When
        Optional<Company> foundCompany = companyRepository.findById(savedCompany.getId());

        // Then
        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getName()).isEqualTo("Test Company 1");
        assertThat(foundCompany.get().getCountryCode()).isEqualTo("USA");
    }

    @Test
    @DisplayName("Should return empty optional when company not found")
    void shouldReturnEmptyOptionalWhenCompanyNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        Optional<Company> foundCompany = companyRepository.findById(nonExistentId);

        // Then
        assertThat(foundCompany).isEmpty();
    }

    @Test
    @DisplayName("Should find all companies")
    void shouldFindAllCompanies() {
        // Given
        companyRepository.save(testCompany1);
        companyRepository.save(testCompany2);

        // When
        List<Company> allCompanies = companyRepository.findAll();

        // Then
        assertThat(allCompanies).hasSize(2);
        assertThat(allCompanies).extracting("name")
                .containsExactlyInAnyOrder("Test Company 1", "Test Company 2");
    }

    @Test
    @DisplayName("Should find companies with pagination")
    void shouldFindCompaniesWithPagination() {
        // Given
        companyRepository.save(testCompany1);
        companyRepository.save(testCompany2);
        Pageable pageable = PageRequest.of(0, 1);

        // When
        Page<Company> companyPage = companyRepository.findAll(pageable);

        // Then
        assertThat(companyPage.getContent()).hasSize(1);
        assertThat(companyPage.getTotalElements()).isEqualTo(2);
        assertThat(companyPage.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should update company successfully")
    void shouldUpdateCompanySuccessfully() {
        // Given
        Company savedCompany = companyRepository.save(testCompany1);
        savedCompany.setName("Updated Company Name");
        savedCompany.setEmail("updated@company.com");

        // When
        Company updatedCompany = companyRepository.save(savedCompany);

        // Then
        assertThat(updatedCompany.getName()).isEqualTo("Updated Company Name");
        assertThat(updatedCompany.getEmail()).isEqualTo("updated@company.com");
        assertThat(updatedCompany.getId()).isEqualTo(savedCompany.getId());
    }

    @Test
    @DisplayName("Should delete company successfully")
    void shouldDeleteCompanySuccessfully() {
        // Given
        Company savedCompany = companyRepository.save(testCompany1);

        // When
        companyRepository.deleteById(savedCompany.getId());

        // Then
        Optional<Company> deletedCompany = companyRepository.findById(savedCompany.getId());
        assertThat(deletedCompany).isEmpty();
    }

    @Test
    @DisplayName("Should count companies correctly")
    void shouldCountCompaniesCorrectly() {
        // Given
        companyRepository.save(testCompany1);
        companyRepository.save(testCompany2);

        // When
        long count = companyRepository.count();

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should check if company exists")
    void shouldCheckIfCompanyExists() {
        // Given
        Company savedCompany = companyRepository.save(testCompany1);

        // When
        boolean exists = companyRepository.existsById(savedCompany.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should check if company does not exist")
    void shouldCheckIfCompanyDoesNotExist() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        boolean exists = companyRepository.existsById(nonExistentId);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should save all companies")
    void shouldSaveAllCompanies() {
        // Given
        List<Company> companies = List.of(testCompany1, testCompany2);

        // When
        List<Company> savedCompanies = companyRepository.saveAll(companies);

        // Then
        assertThat(savedCompanies).hasSize(2);
        assertThat(savedCompanies).allMatch(company -> company.getId() != null);
    }

    @Test
    @DisplayName("Should delete all companies")
    void shouldDeleteAllCompanies() {
        // Given
        companyRepository.save(testCompany1);
        companyRepository.save(testCompany2);

        // When
        companyRepository.deleteAll();

        // Then
        assertThat(companyRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle JSON data correctly")
    void shouldHandleJsonDataCorrectly() {
        // Given
        Address address1 = new Address();
        address1.setCountry("USA");
        address1.setCity("New York");
        address1.setStreet("123 Main St");

        Address address2 = new Address();
        address2.setCountry("USA");
        address2.setCity("Los Angeles");
        address2.setStreet("456 Oak Ave");

        Phone phone1 = new Phone();
        phone1.setCode("+1");
        phone1.setNumber("555-1234");

        Phone phone2 = new Phone();
        phone2.setCode("+1");
        phone2.setNumber("555-5678");

        testCompany1.setAddressData(List.of(address1, address2));
        testCompany1.setPhoneData(List.of(phone1, phone2));

        // When
        Company savedCompany = companyRepository.save(testCompany1);
        Optional<Company> foundCompany = companyRepository.findById(savedCompany.getId());

        // Then
        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getAddressData()).hasSize(2);
        assertThat(foundCompany.get().getPhoneData()).hasSize(2);
        assertThat(foundCompany.get().getAddressData().get(0).getCity()).isEqualTo("New York");
        assertThat(foundCompany.get().getAddressData().get(1).getCity()).isEqualTo("Los Angeles");
        assertThat(foundCompany.get().getPhoneData().get(0).getNumber()).isEqualTo("555-1234");
        assertThat(foundCompany.get().getPhoneData().get(1).getNumber()).isEqualTo("555-5678");
    }
} 