package com.ztl.countryservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {
    private Name name;
    private Map<String, Currency> currencies;

    @JsonIgnore
    public String getCommonName() {
        return name.getCommon();
    }

    @JsonIgnore
    public String getFirstCurrencyName() {
        return currencies.values().stream().findFirst().map(Currency::getName).orElse("");
    }

}
