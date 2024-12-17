package com.ztl.countryservice.service;

import com.ztl.countryservice.controller.SortDirection;
import com.ztl.countryservice.domain.Country;
import com.ztl.countryservice.domain.Currency;
import com.ztl.countryservice.service.externalservice.RestCountriesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CountryService {

    private final RestCountriesService restCountriesService;

    //Cache response with caffeine
    public List<Country> getEuropeanCountries(final SortDirection sortDirection) {
        List<Country> countries = restCountriesService.getEuropeanCountries();
        if (countries != null && SortDirection.ASC.equals(sortDirection)) {
            countries.sort(getCountryComparator());
        }
        if (countries != null && SortDirection.DESC.equals(sortDirection)) {
            countries.sort(getCountryComparator().reversed());
        }
        return countries;
    }

    private static Comparator<Country> getCountryComparator() {
        return Comparator.comparing(Country::getCommonName).thenComparing(Country::getFirstCurrencyName);
    }

    public Map<String, Set<String>> getCountriesByCurrency() {
        List<Country> countries = restCountriesService.getAllCountries();
        if (countries != null && !countries.isEmpty()) {
            return groupCountriesByCurrency(countries);
        }
        throw new RuntimeException("Failed to get countries by country");
    }

    protected static Map<String, Set<String>> groupCountriesByCurrency(List<Country> countries) {
        Map<String, Set<String>> countriesByCurrency = new HashMap<>();
        for (Country country : countries) {
            Map<String, Currency> currencies = country.getCurrencies();
            for (String currencyCode : currencies.keySet()) {
                // If the currency already exists in the map, add the country to its list
                countriesByCurrency.computeIfAbsent(currencyCode, k -> new HashSet<>()) // If not present, create a new list
                    .add(country.getCommonName());
            }
        }
        // Return the map, where key is currency code and value is list of countries
        return countriesByCurrency;
    }
}
