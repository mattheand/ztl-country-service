package com.ztl.countryservice.controller;

import com.ztl.countryservice.domain.Country;
import com.ztl.countryservice.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
@Slf4j
public class CountriesController {

    private final CountryService countryService;

    @GetMapping("europe")
    public List<Country> getEuropeanCountries(@RequestParam(defaultValue = "NO_SORT") final SortDirection sortDirection) {
        return countryService.getEuropeanCountries(sortDirection);
    }

    //A list of all currencies in the world, with information in which countries each currency is used
    @GetMapping("countriesByCurrency")
    public Map<String, Country> getCountriesByCurrency() {
        //return countryService.getCountriesByCurrency();
        return null;
    }
}