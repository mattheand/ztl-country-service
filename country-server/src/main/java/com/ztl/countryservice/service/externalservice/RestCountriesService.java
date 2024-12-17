package com.ztl.countryservice.service.externalservice;

import com.ztl.countryservice.domain.Country;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RestCountriesService {
    private static final String REST_COUNTRIES_EUROPE_API = "https://restcountries.com/v3.1/region/europe?fields=name,currencies";
    private static final String REST_COUNTRIES_ALL = "https://restcountries.com/v3.1/all?fields=name,currencies";

    private final WebClient webClient = WebClient.create();

    public List<Country> getEuropeanCountries() {
        return makeCall(REST_COUNTRIES_EUROPE_API);
    }

    public List<Country> getAllCountries() {
        return makeCall(REST_COUNTRIES_ALL);
    }

    //TODO: check for downstream status codes before returning
    private List<Country> makeCall(String restCountriesEuropeApi) {
        try {
            return webClient.get()
                .uri(restCountriesEuropeApi)
                .retrieve()
                .bodyToFlux(Country.class)  // Get the data as a Flux (stream of elements)
                .collectList()
                .block();
        }
        catch (Exception e) {
            log.error("Failed to make downstream call", e);
            throw new RuntimeException("Failed to call rest countries api", e);
        }
    }
}
