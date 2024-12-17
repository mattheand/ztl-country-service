package com.ztl.countryservice.service;

import com.ztl.countryservice.controller.SortDirection;
import com.ztl.countryservice.domain.Country;
import com.ztl.countryservice.domain.Currency;
import com.ztl.countryservice.domain.Name;
import com.ztl.countryservice.service.externalservice.RestCountriesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {
    @Mock
    RestCountriesService restCountriesService;

    @InjectMocks
    CountryService service;


    @Test
    void when_groupingCountries_correctlyGrouped() {
        Map<String, Set<String>> countriesByCurrency = CountryService.groupCountriesByCurrency(List.of(norway(),
            usa(),
            panama(),
            caribbeanNetherlands()
            ));

        assertThat(countriesByCurrency).isEqualTo(Map.of("NOK", Set.of("Norway"),
            "USD", Set.of("United States", "Caribbean Netherlands", "Panama"),
            "PAB", Set.of("Panama")));
    }

    @Test
    void when_sortingEuropeanCountries_asc_correctlySorted() {
        List<Country> givenCountries = new ArrayList<>(List.of(norway(), usa(), panama(), caribbeanNetherlands()));
        when(restCountriesService.getEuropeanCountries()).thenReturn(givenCountries);

        List<Country> sortedCountries = service.getEuropeanCountries(SortDirection.ASC);

        assertThat(sortedCountries).isEqualTo(List.of(caribbeanNetherlands(), norway(), panama(), usa()));
    }

    @Test
    void when_sortingEuropeanCountries_desc_correctlySorted() {
        List<Country> givenCountries = new ArrayList<>(List.of(norway(), usa(), panama(), caribbeanNetherlands()));
        when(restCountriesService.getEuropeanCountries()).thenReturn(givenCountries);

        List<Country> sortedCountries = service.getEuropeanCountries(SortDirection.DESC);

        assertThat(sortedCountries).isEqualTo(List.of(usa(), panama(), norway(), caribbeanNetherlands()));
    }

    @Test
    void when_sortingEuropeanCountries_noSort_sameOrderAsReturned() {
        List<Country> givenCountries = new ArrayList<>(List.of(norway(), usa(), panama(), caribbeanNetherlands()));
        when(restCountriesService.getEuropeanCountries()).thenReturn(givenCountries);

        List<Country> sortedCountries = service.getEuropeanCountries(SortDirection.NO_SORT);

        assertThat(sortedCountries).isEqualTo(List.of(norway(), usa(), panama(), caribbeanNetherlands()));
    }


    private Country norway() {
        return Country.builder()
            .name(Name.builder().common("Norway").build())
            .currencies(Map.of("NOK", Currency.builder().name("Norwegian krone").symbol("kr").build()))
            .build();
    }

    private Country usa() {
        return Country.builder()
            .name(Name.builder().common("United States").build())
            .currencies(Map.of("USD", Currency.builder().name("United States dollar").symbol("$").build()))
            .build();
    }

    private Country caribbeanNetherlands() {
        return Country.builder()
            .name(Name.builder().common("Caribbean Netherlands").build())
            .currencies(Map.of("USD", Currency.builder().name("United States dollar").symbol("$").build()))
            .build();
    }

    private Country panama() {
        return Country.builder()
            .name(Name.builder().common("Panama").build())
            .currencies(Map.of("USD",
                Currency.builder().name("United States dollar").symbol("$").build(),
                "PAB",
                Currency.builder().name("Panamanian balboa").symbol("B/.").build()))
            .build();
    }
}