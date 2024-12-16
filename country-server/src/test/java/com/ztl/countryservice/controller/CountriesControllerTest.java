package com.ztl.countryservice.controller;

import com.ztl.countryservice.domain.Country;
import com.ztl.countryservice.domain.Currency;
import com.ztl.countryservice.domain.Name;
import com.ztl.countryservice.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CountriesController.class)
class CountriesControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    CountryService countryService;

    @Test
    void when_getListEuropeanCountries_correctPathsArePresent() throws Exception {
        Country givenCountry = Country.builder()
            .name(Name.builder().common("norway").build())
            .currencies(Map.of("NOK", Currency.builder().name("Norwegian krone").symbol("kr").build()))
            .build();
        when(countryService.getEuropeanCountries(any(SortDirection.class))).thenReturn(List.of(givenCountry));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries/europe").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.[0].name.common").value("norway"))
            .andExpect(jsonPath("$.[0].currencies.NOK.name").value(("Norwegian krone")))
            .andExpect(jsonPath("$.[0].currencies.NOK.symbol").value(("kr")));
    }
}