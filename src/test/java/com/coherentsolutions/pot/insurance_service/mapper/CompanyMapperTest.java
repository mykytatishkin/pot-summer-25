package com.coherentsolutions.pot.insurance_service.mapper;

import com.coherentsolutions.pot.insurance_service.dto.AddressDto;
import com.coherentsolutions.pot.insurance_service.dto.CompanyDto;
import com.coherentsolutions.pot.insurance_service.dto.PhoneDto;
import com.coherentsolutions.pot.insurance_service.enums.CompanyStatus;
import com.coherentsolutions.pot.insurance_service.model.Address;
import com.coherentsolutions.pot.insurance_service.model.Company;
import com.coherentsolutions.pot.insurance_service.model.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Company Mapper Tests")
class CompanyMapperTest {

    private CompanyMapper companyMapper;

    private UUID testCompanyId;
    private Company testCompany;
    private CompanyDto testCompanyDto;
    private Address testAddress;
    private Phone testPhone;
    private AddressDto testAddressDto;
    private PhoneDto testPhoneDto;

    @BeforeEach
    void setUp() {
        companyMapper = Mappers.getMapper(CompanyMapper.class);

        testCompanyId = UUID.randomUUID();
        testAddress = new Address();
        testAddress.setCountry("USA");
        testAddress.setCity("New York");
        testAddress.setStreet("123 Main St");
        testAddress.setBuilding("Building A");
        testAddress.setRoom("Room 101");

        testPhone = new Phone();
        testPhone.setCode("+1");
        testPhone.setNumber("555-1234");

        testAddressDto = AddressDto.builder()
                .country("USA")
                .city("New York")
                .street("123 Main St")
                .building("Building A")
                .room("Room 101")
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
        testCompany.setCreatedBy(UUID.randomUUID());
        testCompany.setCreatedAt(Instant.now());
        testCompany.setUpdatedBy(UUID.randomUUID());
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
                .createdBy(UUID.randomUUID())
                .createdAt(Instant.now())
                .updatedBy(UUID.randomUUID())
                .updatedAt(Instant.now())
                .build();
    }

    @Nested
    @DisplayName("Entity to DTO Mapping")
    class EntityToDtoMapping {

        @Test
        @DisplayName("Should map company entity to DTO correctly")
        void shouldMapCompanyEntityToDtoCorrectly() {
            // When
            CompanyDto result = companyMapper.toCompanyDto(testCompany);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testCompany.getId());
            assertThat(result.getName()).isEqualTo(testCompany.getName());
            assertThat(result.getCountryCode()).isEqualTo(testCompany.getCountryCode());
            assertThat(result.getEmail()).isEqualTo(testCompany.getEmail());
            assertThat(result.getWebsite()).isEqualTo(testCompany.getWebsite());
            assertThat(result.getStatus()).isEqualTo(testCompany.getStatus());
            assertThat(result.getCreatedBy()).isEqualTo(testCompany.getCreatedBy());
            assertThat(result.getCreatedAt()).isEqualTo(testCompany.getCreatedAt());
            assertThat(result.getUpdatedBy()).isEqualTo(testCompany.getUpdatedBy());
            assertThat(result.getUpdatedAt()).isEqualTo(testCompany.getUpdatedAt());
        }

        @Test
        @DisplayName("Should map address data correctly")
        void shouldMapAddressDataCorrectly() {
            // When
            CompanyDto result = companyMapper.toCompanyDto(testCompany);

            // Then
            assertThat(result.getAddressData()).hasSize(1);
            AddressDto mappedAddress = result.getAddressData().get(0);
            assertThat(mappedAddress.getCountry()).isEqualTo(testAddress.getCountry());
            assertThat(mappedAddress.getCity()).isEqualTo(testAddress.getCity());
            assertThat(mappedAddress.getStreet()).isEqualTo(testAddress.getStreet());
            assertThat(mappedAddress.getBuilding()).isEqualTo(testAddress.getBuilding());
            assertThat(mappedAddress.getRoom()).isEqualTo(testAddress.getRoom());
        }

        @Test
        @DisplayName("Should map phone data correctly")
        void shouldMapPhoneDataCorrectly() {
            // When
            CompanyDto result = companyMapper.toCompanyDto(testCompany);

            // Then
            assertThat(result.getPhoneData()).hasSize(1);
            PhoneDto mappedPhone = result.getPhoneData().get(0);
            assertThat(mappedPhone.getCode()).isEqualTo(testPhone.getCode());
            assertThat(mappedPhone.getNumber()).isEqualTo(testPhone.getNumber());
        }

        @Test
        @DisplayName("Should handle null address data")
        void shouldHandleNullAddressData() {
            // Given
            testCompany.setAddressData(null);

            // When
            CompanyDto result = companyMapper.toCompanyDto(testCompany);

            // Then
            assertThat(result.getAddressData()).isNull();
        }

        @Test
        @DisplayName("Should handle null phone data")
        void shouldHandleNullPhoneData() {
            // Given
            testCompany.setPhoneData(null);

            // When
            CompanyDto result = companyMapper.toCompanyDto(testCompany);

            // Then
            assertThat(result.getPhoneData()).isNull();
        }

        @Test
        @DisplayName("Should handle empty address data")
        void shouldHandleEmptyAddressData() {
            // Given
            testCompany.setAddressData(List.of());

            // When
            CompanyDto result = companyMapper.toCompanyDto(testCompany);

            // Then
            assertThat(result.getAddressData()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty phone data")
        void shouldHandleEmptyPhoneData() {
            // Given
            testCompany.setPhoneData(List.of());

            // When
            CompanyDto result = companyMapper.toCompanyDto(testCompany);

            // Then
            assertThat(result.getPhoneData()).isEmpty();
        }
    }

    @Nested
    @DisplayName("DTO to Entity Mapping")
    class DtoToEntityMapping {

        @Test
        @DisplayName("Should map company DTO to entity correctly")
        void shouldMapCompanyDtoToEntityCorrectly() {
            // When
            Company result = companyMapper.toEntity(testCompanyDto);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isNull(); // Should be ignored as per mapping
            assertThat(result.getName()).isEqualTo(testCompanyDto.getName());
            assertThat(result.getCountryCode()).isEqualTo(testCompanyDto.getCountryCode());
            assertThat(result.getEmail()).isEqualTo(testCompanyDto.getEmail());
            assertThat(result.getWebsite()).isEqualTo(testCompanyDto.getWebsite());
            assertThat(result.getStatus()).isEqualTo(testCompanyDto.getStatus());
            assertThat(result.getCreatedAt()).isNull(); // Should be ignored as per mapping
            assertThat(result.getUpdatedAt()).isNull(); // Should be ignored as per mapping
        }

        @Test
        @DisplayName("Should map address DTO list to entity list correctly")
        void shouldMapAddressDtoListToEntityListCorrectly() {
            // Given
            List<AddressDto> addressDtos = List.of(testAddressDto);

            // When
            List<Address> result = companyMapper.toAddressList(addressDtos);

            // Then
            assertThat(result).hasSize(1);
            Address mappedAddress = result.get(0);
            assertThat(mappedAddress.getCountry()).isEqualTo(testAddressDto.getCountry());
            assertThat(mappedAddress.getCity()).isEqualTo(testAddressDto.getCity());
            assertThat(mappedAddress.getStreet()).isEqualTo(testAddressDto.getStreet());
            assertThat(mappedAddress.getBuilding()).isEqualTo(testAddressDto.getBuilding());
            assertThat(mappedAddress.getRoom()).isEqualTo(testAddressDto.getRoom());
        }

        @Test
        @DisplayName("Should map phone DTO list to entity list correctly")
        void shouldMapPhoneDtoListToEntityListCorrectly() {
            // Given
            List<PhoneDto> phoneDtos = List.of(testPhoneDto);

            // When
            List<Phone> result = companyMapper.toPhoneList(phoneDtos);

            // Then
            assertThat(result).hasSize(1);
            Phone mappedPhone = result.get(0);
            assertThat(mappedPhone.getCode()).isEqualTo(testPhoneDto.getCode());
            assertThat(mappedPhone.getNumber()).isEqualTo(testPhoneDto.getNumber());
        }

        @Test
        @DisplayName("Should handle null address DTO list")
        void shouldHandleNullAddressDtoList() {
            // When
            List<Address> result = companyMapper.toAddressList(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle null phone DTO list")
        void shouldHandleNullPhoneDtoList() {
            // When
            List<Phone> result = companyMapper.toPhoneList(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle empty address DTO list")
        void shouldHandleEmptyAddressDtoList() {
            // When
            List<Address> result = companyMapper.toAddressList(List.of());

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty phone DTO list")
        void shouldHandleEmptyPhoneDtoList() {
            // When
            List<Phone> result = companyMapper.toPhoneList(List.of());

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Address List Mapping")
    class AddressListMapping {

        @Test
        @DisplayName("Should map address entity list to DTO list correctly")
        void shouldMapAddressEntityListToDtoListCorrectly() {
            // Given
            List<Address> addresses = List.of(testAddress);

            // When
            List<AddressDto> result = companyMapper.toAddressDtoList(addresses);

            // Then
            assertThat(result).hasSize(1);
            AddressDto mappedAddressDto = result.get(0);
            assertThat(mappedAddressDto.getCountry()).isEqualTo(testAddress.getCountry());
            assertThat(mappedAddressDto.getCity()).isEqualTo(testAddress.getCity());
            assertThat(mappedAddressDto.getStreet()).isEqualTo(testAddress.getStreet());
            assertThat(mappedAddressDto.getBuilding()).isEqualTo(testAddress.getBuilding());
            assertThat(mappedAddressDto.getRoom()).isEqualTo(testAddress.getRoom());
        }

        @Test
        @DisplayName("Should handle null address entity list")
        void shouldHandleNullAddressEntityList() {
            // When
            List<AddressDto> result = companyMapper.toAddressDtoList(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle empty address entity list")
        void shouldHandleEmptyAddressEntityList() {
            // When
            List<AddressDto> result = companyMapper.toAddressDtoList(List.of());

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Phone List Mapping")
    class PhoneListMapping {

        @Test
        @DisplayName("Should map phone entity list to DTO list correctly")
        void shouldMapPhoneEntityListToDtoListCorrectly() {
            // Given
            List<Phone> phones = List.of(testPhone);

            // When
            List<PhoneDto> result = companyMapper.toPhoneDtoList(phones);

            // Then
            assertThat(result).hasSize(1);
            PhoneDto mappedPhoneDto = result.get(0);
            assertThat(mappedPhoneDto.getCode()).isEqualTo(testPhone.getCode());
            assertThat(mappedPhoneDto.getNumber()).isEqualTo(testPhone.getNumber());
        }

        @Test
        @DisplayName("Should handle null phone entity list")
        void shouldHandleNullPhoneEntityList() {
            // When
            List<PhoneDto> result = companyMapper.toPhoneDtoList(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle empty phone entity list")
        void shouldHandleEmptyPhoneEntityList() {
            // When
            List<PhoneDto> result = companyMapper.toPhoneDtoList(List.of());

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null company entity")
        void shouldHandleNullCompanyEntity() {
            // When
            CompanyDto result = companyMapper.toCompanyDto(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle null company DTO")
        void shouldHandleNullCompanyDto() {
            // When
            Company result = companyMapper.toEntity(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle company with null fields")
        void shouldHandleCompanyWithNullFields() {
            // Given
            Company companyWithNulls = new Company();
            companyWithNulls.setName("Test");
            companyWithNulls.setCountryCode("USA");

            // When
            CompanyDto result = companyMapper.toCompanyDto(companyWithNulls);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Test");
            assertThat(result.getCountryCode()).isEqualTo("USA");
            assertThat(result.getEmail()).isNull();
            assertThat(result.getWebsite()).isNull();
            assertThat(result.getStatus()).isNull();
        }

        @Test
        @DisplayName("Should handle DTO with null fields")
        void shouldHandleDtoWithNullFields() {
            // Given
            CompanyDto dtoWithNulls = CompanyDto.builder()
                    .name("Test")
                    .countryCode("USA")
                    .build();

            // When
            Company result = companyMapper.toEntity(dtoWithNulls);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Test");
            assertThat(result.getCountryCode()).isEqualTo("USA");
            assertThat(result.getEmail()).isNull();
            assertThat(result.getWebsite()).isNull();
            assertThat(result.getStatus()).isNull();
        }
    }
} 