package com.ztl.countryservice.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Currency {
    private String name;
    private String symbol;
}
