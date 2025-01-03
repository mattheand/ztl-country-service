package com.ztl.countryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Currency {
    private String name;
    private String symbol;
}
