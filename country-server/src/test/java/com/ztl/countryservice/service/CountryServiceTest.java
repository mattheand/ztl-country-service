package com.ztl.countryservice.service;

import com.ztl.countryservice.domain.Country;
import com.ztl.countryservice.domain.Currency;
import com.ztl.countryservice.domain.Name;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CountryServiceTest {

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