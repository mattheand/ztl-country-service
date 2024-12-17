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

import java.util.List;
import java.util.Map;

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

    @Test
    void when_invalidPath_ErrorMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/invalidUrl").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.code").value("NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("No static resource api/invalidUrl."));
    }


    @Test
    void when_serviceThrowsError_ErrorMessageWith500() throws Exception {
        when(countryService.getCountriesByCurrency()).thenThrow(new RuntimeException("Failed to fetch countries by currency"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries/currency/all").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.message").value("Failed to fetch countries by currency"));
    }
}