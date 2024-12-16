package com.ztl.countryservice.service;

import com.ztl.countryservice.controller.SortDirection;
import com.ztl.countryservice.domain.Country;
import com.ztl.countryservice.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CountryService {

    private static final String REST_COUNTRIES_EUROPE_API = "https://restcountries.com/v3.1/region/europe?fields=name,currencies";
    private static final String REST_COUNTRIES_ALL = "https://restcountries.com/v3.1/all?fields=name,currencies";


    //TODO: check for downstream status codes before returning
    //Cache response with caffeine
    public List<Country> getEuropeanCountries(final SortDirection sortDirection) {
        WebClient webClient = WebClient.create();

        //TODO: extract this method to another "external
        List<Country> countries = webClient.get()
            .uri(REST_COUNTRIES_EUROPE_API)
            .retrieve()
            .bodyToFlux(Country.class)  // Get the data as a Flux (stream of elements)
            .collectList()  // Collect the stream into a List
            .block();

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

    public Map<String, List<Country>> getCountriesByCurrency() {
        WebClient webClient = WebClient.create();

        List<Country> countries = webClient.get()
            .uri(REST_COUNTRIES_ALL)
            .retrieve()
            .bodyToFlux(Country.class)  // Get the data as a Flux (stream of elements)
            .collectList()  // Collect the stream into a List
            .block();

        // Create a Map to hold the currencies and corresponding countries
        Map<String, List<Country>> countriesByCurrency = new HashMap<>();

        // Iterate through the countries and process the currencies
        for (Country country : countries) {
            // Assuming country.getCurrencies() is a Map of currency codes to Currency objects
            Map<String, Currency> currencies = country.getCurrencies();

            // Iterate through each currency in the country
            for (String currencyCode : currencies.keySet()) {
                // If the currency already exists in the map, add the country to its list
                countriesByCurrency.computeIfAbsent(currencyCode,
                        k -> new ArrayList<>()) // If not present, create a new list
                    .add(country);
            }
        }

        // Return the map, where key is currency code and value is list of countries
        return countriesByCurrency;
    }
}
