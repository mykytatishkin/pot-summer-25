package com.coherentsolutions.pot.insurance_service.repository;

import com.coherentsolutions.pot.insurance_service.containers.PostgresTestContainer;
import com.coherentsolutions.pot.insurance_service.dto.CompanyFilter;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Company Specification Tests")
class CompanySpecificationTest extends PostgresTestContainer {

    @Autowired
    private CompanyRepository companyRepository;

    private Company activeCompany;
    private Company deactivatedCompany;
    private Company usaCompany;
    private Company canadaCompany;
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

        // Active company in USA
        activeCompany = new Company();
        activeCompany.setName("Active USA Company");
        activeCompany.setCountryCode("USA");
        activeCompany.setEmail("active@usacompany.com");
        activeCompany.setStatus(CompanyStatus.ACTIVE);
        activeCompany.setAddressData(List.of(testAddress));
        activeCompany.setPhoneData(List.of(testPhone));
        activeCompany.setCreatedAt(Instant.now().minusSeconds(3600)); // 1 hour ago
        activeCompany.setUpdatedAt(Instant.now());

        // Deactivated company in USA
        deactivatedCompany = new Company();
        deactivatedCompany.setName("Deactivated USA Company");
        deactivatedCompany.setCountryCode("USA");
        deactivatedCompany.setEmail("deactivated@usacompany.com");
        deactivatedCompany.setStatus(CompanyStatus.DEACTIVATED);
        deactivatedCompany.setAddressData(List.of(testAddress));
        deactivatedCompany.setPhoneData(List.of(testPhone));
        deactivatedCompany.setCreatedAt(Instant.now().minusSeconds(7200)); // 2 hours ago
        deactivatedCompany.setUpdatedAt(Instant.now().minusSeconds(3600)); // 1 hour ago

        // Active company in Canada
        canadaCompany = new Company();
        canadaCompany.setName("Active Canada Company");
        canadaCompany.setCountryCode("CAN");
        canadaCompany.setEmail("active@canadacompany.com");
        canadaCompany.setStatus(CompanyStatus.ACTIVE);
        canadaCompany.setAddressData(List.of(testAddress));
        canadaCompany.setPhoneData(List.of(testPhone));
        canadaCompany.setCreatedAt(Instant.now().minusSeconds(1800)); // 30 minutes ago
        canadaCompany.setUpdatedAt(Instant.now());

        companyRepository.saveAll(List.of(activeCompany, deactivatedCompany, canadaCompany));
    }

    @Test
    @DisplayName("Should filter by name")
    void shouldFilterByName() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setName("Active");
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("name")
                .allMatch(name -> name.toString().contains("Active"));
    }

    @Test
    @DisplayName("Should filter by country code")
    void shouldFilterByCountryCode() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setCountryCode("USA");
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("countryCode")
                .allMatch(code -> code.equals("USA"));
    }

    @Test
    @DisplayName("Should filter by status")
    void shouldFilterByStatus() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setStatus(CompanyStatus.ACTIVE);
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("status")
                .allMatch(status -> status.equals(CompanyStatus.ACTIVE));
    }

    @Test
    @DisplayName("Should filter by created date range")
    void shouldFilterByCreatedDateRange() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setCreatedFrom(Instant.now().minusSeconds(5400)); // 1.5 hours ago
        filter.setCreatedTo(Instant.now().minusSeconds(1800)); // 30 minutes ago
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Active USA Company");
    }

    @Test
    @DisplayName("Should filter by updated date range")
    void shouldFilterByUpdatedDateRange() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setUpdatedFrom(Instant.now().minusSeconds(1800)); // 30 minutes ago
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("name")
                .containsExactlyInAnyOrder("Active USA Company", "Active Canada Company");
    }

    @Test
    @DisplayName("Should combine multiple filters")
    void shouldCombineMultipleFilters() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setName("Active");
        filter.setCountryCode("USA");
        filter.setStatus(CompanyStatus.ACTIVE);
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Active USA Company");
        assertThat(result.getContent().get(0).getCountryCode()).isEqualTo("USA");
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(CompanyStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should return all companies when no filters provided")
    void shouldReturnAllCompaniesWhenNoFiltersProvided() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("Should return empty result when no companies match filters")
    void shouldReturnEmptyResultWhenNoCompaniesMatchFilters() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setName("NonExistent");
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("Should filter by name case insensitive")
    void shouldFilterByNameCaseInsensitive() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setName("active");
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("name")
                .allMatch(name -> name.toString().toLowerCase().contains("active"));
    }

    @Test
    @DisplayName("Should filter by country code case insensitive")
    void shouldFilterByCountryCodeCaseInsensitive() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setCountryCode("usa");
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("countryCode")
                .allMatch(code -> code.equals("USA"));
    }

    @Test
    @DisplayName("Should handle null filter values")
    void shouldHandleNullFilterValues() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setName(null);
        filter.setCountryCode(null);
        filter.setStatus(null);
        filter.setCreatedFrom(null);
        filter.setCreatedTo(null);
        filter.setUpdatedFrom(null);
        filter.setUpdatedTo(null);
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("Should handle empty string filter values")
    void shouldHandleEmptyStringFilterValues() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setName("");
        filter.setCountryCode("");
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("Should filter by partial name match")
    void shouldFilterByPartialNameMatch() {
        // Given
        CompanyFilter filter = new CompanyFilter();
        filter.setName("USA");
        Specification<Company> spec = CompanySpecification.withFilters(filter);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Company> result = companyRepository.findAll(spec, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("name")
                .allMatch(name -> name.toString().contains("USA"));
    }
} 